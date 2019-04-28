package org.rentalhouse.assets.service

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.rentalhouse.assets.entity.Asset
import org.rentalhouse.assets.fixture.asset
import org.rentalhouse.assets.repository.AssetRepository
import org.springframework.test.util.ReflectionTestUtils

class AssetServiceUnitTest {

    private val assetRepository = mockk<AssetRepository>()

    private val assetService    = AssetService(assetRepository)

    @Test
    fun `should return the asset id of a newly added asset`() {
        val asset = asset {
            identifier = "B/401"
            address {
                street = "John's Street"
                city   = "Pune"
                pinCode = "411098"
            }
        }

        every { assetRepository.save(any<Asset>()) } returns savedAsset(asset)

        val assetId = assetService.add(asset)

        assertThat(assetId).isEqualTo("1000")
    }

    private fun savedAsset(asset: Asset): Asset {
        return asset.apply {
            ReflectionTestUtils.setField(this, "id", "1000")
        }
    }
}