package org.rentalhouse.assets.controller

import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.rentalhouse.assets.fixture.asset
import org.rentalhouse.assets.model.Status
import org.rentalhouse.assets.repository.AssetRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("integration-test")
class AssetControllerIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var assetRepository: AssetRepository

    @BeforeEach
    fun setUp() {
        assetRepository.deleteAll()
    }

    @Test
    fun `should add an asset`() {
        val address = """{"street": "Magarpatta", "city": "Pune", "state": "MH", "pinCode" : "400918"}"""
        val asset   = """{"plotIdentifier": "b/401", "address": $address}"""

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/v1/assets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asset)
        ).andExpect(MockMvcResultMatchers.status().isCreated)

        val addedAsset = assetRepository.findAll().first()

        assertThat(addedAsset.id).isNotNull()
        assertThat(addedAsset.status).isEqualTo(Status.AVAILABLE)
        assertThat(addedAsset.plotIdentifier).isEqualTo("b/401")
        assertThat(addedAsset.street()).isEqualTo("Magarpatta")
        assertThat(addedAsset.city()).isEqualTo("Pune")
        assertThat(addedAsset.state()).isEqualTo("MH")
        assertThat(addedAsset.pinCode()).isEqualTo("400918")
    }

    @Test
    fun `should find an asset by id`() {

        val asset = asset {
            plotIdentifier = "B/401"
            address {
                street = "John's Street"
                city = "Pune"
                pinCode = "411098"
                state = "MH"
            }
        }

        val id = assetRepository.save(asset).id

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/assets/$id"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.plotIdentifier",  Matchers.`is`("B/401")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.status",          Matchers.`is`(Status.AVAILABLE.name)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.address.street",  Matchers.`is`("John's Street")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.address.city",    Matchers.`is`("Pune")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.address.pinCode", Matchers.`is`("411098")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.address.state",   Matchers.`is`("MH")))
    }
}