package org.rentalhouse.assets.controller

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.rentalhouse.assets.service.AssetService
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class AssetControllerUnitTest {

    private val assetService    = mockk<AssetService>()

    private val assetController = AssetController(assetService)

    private val mockMvc = MockMvcBuilders.standaloneSetup(assetController).build()

    @Test
    fun `should add an asset with CREATED status`() {

        val asset = """{"identifier": "n/401"}"""

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/v1/assets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asset)
        ).andExpect(MockMvcResultMatchers.status().isCreated)
    }

    @Test
    fun `should add an asset with location uri referring to the newly created asset`() {

        val asset = """{"identifier": "n/401"}"""

        every { assetService.add(any()) } returns "1000"

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/v1/assets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asset)
        ).andExpect(MockMvcResultMatchers.header().string(HttpHeaders.LOCATION, "/v1/assets/1000"))
    }
}