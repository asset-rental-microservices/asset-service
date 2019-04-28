package org.rentalhouse.assets.service

import org.rentalhouse.assets.entity.Asset
import org.rentalhouse.assets.repository.AssertRepository

class AssetService(private val assetRepository: AssertRepository) {

    fun add(any: Asset): String {
        return assetRepository.save(any).id
    }
}