package org.rentalhouse.assets.service

import org.rentalhouse.assets.entity.Asset
import org.rentalhouse.assets.repository.AssetRepository
import org.springframework.stereotype.Service
import java.lang.RuntimeException

@Service
class AssetService(private val assetRepository: AssetRepository) {

    fun add(any: Asset): String {
        return assetRepository.save(any).id
    }

    fun findById(assetId: String): Asset {
        return assetRepository.findById(assetId).orElseThrow { AssetNotFoundException(assetId) }
    }
}

class AssetNotFoundException(assetId: String) : RuntimeException("No asset found for $assetId")