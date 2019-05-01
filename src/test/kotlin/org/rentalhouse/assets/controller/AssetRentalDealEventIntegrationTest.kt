package org.rentalhouse.assets.controller

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.rentalhouse.assets.event.channel.RentalDealChannels
import org.rentalhouse.assets.fixture.asset
import org.rentalhouse.assets.model.Status
import org.rentalhouse.assets.repository.AssetRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.messaging.support.GenericMessage
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration-test")
class AssetRentalDealEventIntegrationTest {

    @Autowired
    private lateinit var assetRepository: AssetRepository

    @Autowired
    private lateinit var rentalDealChannels: RentalDealChannels

    @BeforeEach
    fun setUp() {
        assetRepository.deleteAll()
    }

    @Test
    fun `should update the status of an asset to RENTAL_DEAL_INITIALIZED`() {

        val asset = asset {
            plotIdentifier = "B/401"
            address {
                street  = "John's Street"
                city    = "Pune"
                pinCode = "411098"
                state   = "MH"
            }
        }

        val id = assetRepository.save(asset).id

        val rentalDealInitializedEvent = """{"assetId": "$id", "agreedRent": 20000}"""
        rentalDealChannels.rentalDeals().send(GenericMessage(rentalDealInitializedEvent))

        val updatedAsset = assetRepository.findById(id).get()

        assertThat(updatedAsset.status).isEqualTo(Status.RENTAL_DEAL_INITIALIZED)
        assertThat(updatedAsset.id).isEqualTo(id)
    }
}