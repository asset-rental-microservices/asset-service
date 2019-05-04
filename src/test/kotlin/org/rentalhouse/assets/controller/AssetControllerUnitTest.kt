package org.rentalhouse.assets.controller

import io.mockk.every
import io.mockk.mockk
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.rentalhouse.assets.fixture.asset
import org.rentalhouse.assets.service.AssetNotFoundException
import org.rentalhouse.assets.service.AssetService
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.util.ReflectionTestUtils
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class AssetControllerUnitTest {

    private val assetService = mockk<AssetService>(relaxed = true)

    private val assetController = AssetController(assetService)

    private val mockMvc = MockMvcBuilders.standaloneSetup(assetController).build()

    private val address = """{"street": "Magarpatta", "city": "Pune", "state": "MH", "pinCode" : "400918"}"""
    private val asset   = """{"plotIdentifier": "b/401", "address": $address}"""

    @Test
    fun `should add an asset with CREATED status`() {

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/v1/assets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asset)
        ).andExpect(MockMvcResultMatchers.status().isCreated)
    }

    @Test
    fun `should add an asset with location uri referring to the newly created asset`() {

        every { assetService.add(any()) } returns "1000"

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/v1/assets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asset)
        ).andExpect(MockMvcResultMatchers.header().string(HttpHeaders.LOCATION, "/v1/assets/1000"))
    }

    @Test
    fun `should find an asset with id`() {

        val asset = asset {
            plotIdentifier = "B/401"
            address {
                street = "John's Street"
                city = "Pune"
                pinCode = "411098"
                state = "MH"
            }
        }

        ReflectionTestUtils.setField(asset, "id", "1923233")

        every { assetService.findById("1000") } returns asset

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/assets/1000"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.plotIdentifier", Matchers.`is`("B/401")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.address.street", Matchers.`is`("John's Street")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.address.city", Matchers.`is`("Pune")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.address.pinCode", Matchers.`is`("411098")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.address.state", Matchers.`is`("MH")))
    }

    @Test
    fun `should find all assets`() {

        val asset = asset {
            plotIdentifier = "B/401"
            address {
                street = "John's Street"
                city = "Pune"
                pinCode = "411098"
                state = "MH"
            }
        }

        ReflectionTestUtils.setField(asset, "id", "1923233")

        every { assetService.findAll() } returns listOf(asset)

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/assets"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize<Int>(1)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].plotIdentifier", Matchers.`is`("B/401")))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].address.street", Matchers.`is`("John's Street")))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].address.city", Matchers.`is`("Pune")))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].address.pinCode", Matchers.`is`("411098")))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].address.state", Matchers.`is`("MH")))
    }

    @Test
    fun `should return Ok given No assets are found in finding all assets`() {

        every { assetService.findAll() } returns listOf()

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/assets"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize<Int>(0)))
    }

    @Test
    fun `should return NOT_FOUND given an asset is not found for id`() {

        every { assetService.findById("1000") } throws AssetNotFoundException("1000")

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/assets/1000"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }
}