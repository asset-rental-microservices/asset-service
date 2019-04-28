package org.rentalhouse.assets.service

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.rentalhouse.assets.entity.Asset
import org.rentalhouse.assets.repository.AssertRepository
import org.springframework.test.util.ReflectionTestUtils

class AssetServiceUnitTest {

    private val assetRepository = mockk<AssertRepository>()

    private val assetService    = AssetService(assetRepository)

    @Test
    fun `should return the asset id of a newly added asset`() {
        every { assetRepository.save(any<Asset>()) } returns savedAsset()

        val assetId = assetService.add(Asset())

        assertThat(assetId).isEqualTo("1000")
    }

    private fun savedAsset(): Asset {
        return Asset().apply {
            ReflectionTestUtils.setField(this, "id", "1000")
        }
    }
}