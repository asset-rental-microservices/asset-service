package org.rentalhouse.assets.service

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.rentalhouse.assets.entity.Asset
import org.rentalhouse.assets.fixture.asset
import org.rentalhouse.assets.repository.AssetRepository
import org.springframework.test.util.ReflectionTestUtils
import java.util.*

class AssetServiceUnitTest {

    private val assetRepository = mockk<AssetRepository>()

    private val assetService = AssetService(assetRepository)

    @Test
    fun `should return the asset id of a newly added asset`() {
        val asset = asset {
            identifier = "B/401"
            address {
                street = "John's Street"
                city = "Pune"
                pinCode = "411098"
            }
        }

        every { assetRepository.save(any<Asset>()) } returns savedAsset(asset)

        val id = assetService.add(asset)

        assertThat(id).isEqualTo("1000")
    }

    @Test
    fun `should find an asset with identifier`() {
        val asset = asset { identifier = "B/401" }

        every { assetRepository.findById("1000") } returns Optional.of(asset)

        val foundAsset = assetService.findById("1000")

        assertThat(foundAsset.identifier).isEqualTo("B/401")
    }

    @Test
    fun `should find an asset with city`() {
        val asset = asset {
            identifier = "B/401"
            address {
                city = "Pune"
            }
        }

        every { assetRepository.findById("1000") } returns Optional.of(asset)

        val foundAsset = assetService.findById("1000")

        assertThat(foundAsset.city()).isEqualTo("Pune")
    }

    @Test
    fun `should find an asset with street`() {
        val asset = asset {
            identifier = "B/401"
            address {
                street = "Magarpatta"
            }
        }

        every { assetRepository.findById("1000") } returns Optional.of(asset)

        val foundAsset = assetService.findById("1000")

        assertThat(foundAsset.street()).isEqualTo("Magarpatta")
    }

    @Test
    fun `should find an asset with state`() {
        val asset = asset {
            identifier = "B/401"
            address {
                state = "MH"
            }
        }

        every { assetRepository.findById("1000") } returns Optional.of(asset)

        val foundAsset = assetService.findById("1000")

        assertThat(foundAsset.state()).isEqualTo("MH")
    }

    @Test
    fun `should find an asset with pincode`() {
        val asset = asset {
            identifier = "B/401"
            address {
                pinCode = "400910"
            }
        }

        every { assetRepository.findById("1000") } returns Optional.of(asset)

        val foundAsset = assetService.findById("1000")

        assertThat(foundAsset.pinCode()).isEqualTo("400910")
    }

    @Test
    fun `should throw AssetNotFoundException given no asset exists for an id`() {
        every { assetRepository.findById("1000") } returns Optional.empty()

        assertThrows<AssetNotFoundException> { assetService.findById("1000") }
    }

    private fun savedAsset(asset: Asset): Asset {
        return asset.apply {
            ReflectionTestUtils.setField(this, "id", "1000")
        }
    }
}