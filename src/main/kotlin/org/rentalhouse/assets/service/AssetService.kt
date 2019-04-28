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

    fun findById(id: String): Asset {
        return assetRepository.findById(id).orElseThrow { AssetNotFoundException(id) }
    }
}

class AssetNotFoundException(id: String) : RuntimeException("No asset found for $id")