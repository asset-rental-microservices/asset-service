package org.rentalhouse.assets.service

import org.rentalhouse.assets.entity.Asset
import org.rentalhouse.assets.model.AssetStatusUpdationRequest
import org.rentalhouse.assets.repository.AssetRepository
import org.springframework.stereotype.Service

@Service
class AssetService(private val assetRepository: AssetRepository) {

    fun add(asset: Asset): String {
        return assetRepository.save(asset).id
    }

    fun findById(id: String): Asset {
        return assetRepository.findById(id).orElseThrow { AssetNotFoundException(id) }
    }

    fun updateStatus(assetStatusUpdationRequest: AssetStatusUpdationRequest) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

class AssetNotFoundException(val id: String) : RuntimeException("No asset found for $id")