package org.rentalhouse.assets.service

import org.rentalhouse.assets.entity.Asset
import org.rentalhouse.assets.model.AssetStatus
import org.rentalhouse.assets.repository.AssetRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class AssetService(private val assetRepository: AssetRepository) {

    private val logger: Logger = LoggerFactory.getLogger(AssetService::class.java)

    fun add(asset: Asset): String {
        return assetRepository.save(asset).id
    }

    fun findById(id: String): Asset {
        return assetRepository.findById(id).orElseThrow { AssetNotFoundException(id) }
    }

    fun updateStatus(id: String, status: AssetStatus) {
        logger.info("Updating status of id $id to $status")
        findById(id).updateStatus(status).also { assetRepository.save(it) }
    }

    fun findAll(): List<Asset> {
        logger.info("Finding all assets")
        return assetRepository.findAll(Sort.by(Sort.Order(Sort.Direction.ASC, "id")))
    }
}

class AssetNotFoundException(val id: String) : RuntimeException("No asset found for $id")