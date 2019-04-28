package org.rentalhouse.assets.controller

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.rentalhouse.assets.repository.AssetRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
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
        val asset   = """{"identifier": "b/401", "address": $address}"""

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/v1/assets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asset)
        ).andExpect(MockMvcResultMatchers.status().isCreated)

        val addedAsset = assetRepository.findAll().first()

        assertThat(addedAsset.id).isNotNull()
        assertThat(addedAsset.identifier).isEqualTo("b/401")
        assertThat(addedAsset.street()).isEqualTo("Magarpatta")
        assertThat(addedAsset.city()).isEqualTo("Pune")
        assertThat(addedAsset.state()).isEqualTo("MH")
        assertThat(addedAsset.pinCode()).isEqualTo("400918")
    }
}