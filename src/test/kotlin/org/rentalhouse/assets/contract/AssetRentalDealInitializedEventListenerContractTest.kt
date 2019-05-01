package org.rentalhouse.assets.contract

import au.com.dius.pact.consumer.MessagePactBuilder
import au.com.dius.pact.consumer.Pact
import au.com.dius.pact.consumer.dsl.PactDslJsonBody
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt
import au.com.dius.pact.consumer.junit5.PactTestFor
import au.com.dius.pact.consumer.junit5.ProviderType
import au.com.dius.pact.model.v3.messaging.Message
import au.com.dius.pact.model.v3.messaging.MessagePact
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.rentalhouse.assets.event.channel.RentalDealChannels
import org.rentalhouse.assets.fixture.asset
import org.rentalhouse.assets.model.AssetStatus
import org.rentalhouse.assets.repository.AssetRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.messaging.support.GenericMessage
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension


@ExtendWith(SpringExtension::class, PactConsumerTestExt::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("contract-test")
@PactTestFor(providerName = "assetRentalService", providerType = ProviderType.ASYNCH)
class AssetRentalDealInitializedEventListenerContractTest {

    @Autowired
    private lateinit var rentalDealChannels: RentalDealChannels

    @Autowired
    private lateinit var assetRepository: AssetRepository

    @BeforeEach
    fun setUp() {
        assetRepository.deleteAll()
    }

    @Pact(consumer = "assetService", provider = "assetRentalService")
    fun pactAssetDealInitialized(builder: MessagePactBuilder): MessagePact {
        return builder
                .given("rental deal for asset with id 1001 is INITIALIZED")
                .expectsToReceive("a rental deal initialized event")
                .withContent(PactDslJsonBody().stringValue("assetId", "1001"))
                .toPact()
    }

    @Test
    fun `should process the asset deal initialized event`(messages: List<Message>) {
        val asset = asset(id = "1001") {
            plotIdentifier = "B/401"
            address {
                street = "John's Street"
                city = "Pune"
                pinCode = "411098"
                state = "MH"
            }
        }

        assetRepository.save(asset).id
        rentalDealChannels.rentalDeals().send(GenericMessage(messages[0].contents.valueAsString()))

        val updatedAsset = assetRepository.findById("1001").get()
        assertThat(updatedAsset.status).isEqualTo(AssetStatus.RENTAL_DEAL_INITIALIZED)
    }
}