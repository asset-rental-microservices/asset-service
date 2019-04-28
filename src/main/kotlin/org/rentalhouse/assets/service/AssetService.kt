package org.rentalhouse.assets.service

import org.rentalhouse.assets.entity.Asset
import org.rentalhouse.assets.repository.AssertRepository
import org.springframework.stereotype.Service

@Service
class AssetService(private val assetRepository: AssertRepository) {

    fun add(any: Asset): String {
        return assetRepository.save(any).id
    }
}