package org.rentalhouse.assets.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.rentalhouse.assets.entity.Asset
import org.rentalhouse.assets.fixture.asset
import org.rentalhouse.assets.model.AssetStatus
import org.rentalhouse.assets.repository.AssetRepository
import org.springframework.data.domain.Sort
import org.springframework.test.util.ReflectionTestUtils
import java.util.*

class AssetServiceUnitTest {

    private val assetRepository = mockk<AssetRepository>()

    private val assetService = AssetService(assetRepository)

    @Test
    fun `should return the asset id of a newly added asset`() {
        val asset = asset {
            plotIdentifier = "B/401"
            address {
                street = "John's Street"
                city = "Pune"
                pinCode = "411098"
            }
        }

        every { assetRepository.save(any<Asset>()) } returns savedAsset(asset, id = "1000")

        val id = assetService.add(asset)

        assertThat(id).isEqualTo("1000")
    }

    @Test
    fun `should find an asset with identifier`() {
        val asset = asset { plotIdentifier = "B/401" }

        every { assetRepository.findById("1000") } returns Optional.of(asset)

        val foundAsset = assetService.findById("1000")

        assertThat(foundAsset.plotIdentifier).isEqualTo("B/401")
    }

    @Test
    fun `should find an asset with city`() {
        val asset = asset {
            plotIdentifier = "B/401"
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
            plotIdentifier = "B/401"
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
            plotIdentifier = "B/401"
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
            plotIdentifier = "B/401"
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

    @Test
    fun `should update the status of an asset to RENTAL_DEAL_INITIALIZED`() {
        val asset = asset {
            plotIdentifier = "B/401"
        }

        val slot = slot<Asset>()

        every { assetRepository.findById("1000") }    returns Optional.of(asset)
        every { assetRepository.save(capture(slot)) } returns savedAsset(asset, "90909")

        assetService.updateStatus("1000", AssetStatus.RENTAL_DEAL_INITIALIZED)

        assertThat(slot.captured.status).isEqualTo(AssetStatus.RENTAL_DEAL_INITIALIZED)
    }

    @Test
    fun `should throw AssetNotFoundException given no asset exists for an id while updating the status`() {
        every { assetRepository.findById("1000") } returns Optional.empty()

        assertThrows<AssetNotFoundException> { assetService.updateStatus("1000", AssetStatus.RENTAL_DEAL_INITIALIZED) }
    }

    @Test
    fun `should find all assets with size 2`() {
        val asset1 = asset {
            plotIdentifier = "B/401"
        }
        val asset2 = asset {
            plotIdentifier = "C/401"
        }

        every { assetRepository.findAll(any<Sort>()) } returns listOf(asset1, asset2)

        val assets = assetService.findAll()

        assertThat(assets.size).isEqualTo(2)
    }


    private fun savedAsset(asset: Asset, id: String): Asset {
        return asset.apply {
            ReflectionTestUtils.setField(this, "id", id)
        }
    }
}