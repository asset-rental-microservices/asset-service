package org.rentalhouse.assets.controller

import org.rentalhouse.assets.extension.toUri
import org.rentalhouse.assets.model.AssetAdditionRequest
import org.rentalhouse.assets.model.AssetView
import org.rentalhouse.assets.model.toAsset
import org.rentalhouse.assets.service.AssetNotFoundException
import org.rentalhouse.assets.service.AssetService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class AssetController(private val assetService: AssetService) {

    private val logger: Logger = LoggerFactory.getLogger(AssetController::class.java)

    @PostMapping(value = ["/v1/assets"])
    fun add(@RequestBody assetAdditionRequest: AssetAdditionRequest): ResponseEntity<String> {
        val id = assetService.add(assetAdditionRequest.toAsset())
        logger.info("Asset created with id $id")
        return ResponseEntity.created("/v1/assets/$id".toUri()).build()
    }

    @GetMapping(value = ["/v1/assets/{id}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun findById(@PathVariable id: String): AssetView {
        logger.info("Finding an asset with id $id")
        val asset = assetService.findById(id)
        return AssetView.fromAssert(asset)
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun assetNotFoundHandler(ex: AssetNotFoundException) {
        logger.error("Asset with id ${ex.id} not found")
    }
}