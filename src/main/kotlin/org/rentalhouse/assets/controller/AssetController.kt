package org.rentalhouse.assets.controller

import org.rentalhouse.assets.extension.toUri
import org.rentalhouse.assets.model.AssetAdditionRequest
import org.rentalhouse.assets.model.AssetView
import org.rentalhouse.assets.model.toAsset
import org.rentalhouse.assets.service.AssetNotFoundException
import org.rentalhouse.assets.service.AssetService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class AssetController(private val assetService: AssetService) {

    @PostMapping(value = ["/v1/assets"])
    fun add(@RequestBody assetAdditionRequest: AssetAdditionRequest): ResponseEntity<String> {
        val id = assetService.add(assetAdditionRequest.toAsset())
        return ResponseEntity.created("/v1/assets/$id".toUri()).build()
    }

    @GetMapping(value = ["/v1/assets/{id}"])
    fun findById(@PathVariable id: String): AssetView {
        val asset = assetService.findById(id)
        return AssetView.fromAssert(asset)
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun assetNotFoundHandler(ex: AssetNotFoundException){
    }
}