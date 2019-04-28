package org.rentalhouse.assets.service

import org.rentalhouse.assets.entity.Asset
import org.rentalhouse.assets.repository.AssetRepository
import org.springframework.stereotype.Service

@Service
class AssetService(private val assetRepository: AssetRepository) {

    fun add(any: Asset): String {
        return assetRepository.save(any).id
    }

    fun findById(assetId: String): Asset {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}