package org.rentalhouse.assets.controller

import org.rentalhouse.assets.extension.toUri
import org.rentalhouse.assets.model.AssetAdditionRequest
import org.rentalhouse.assets.model.AssetView
import org.rentalhouse.assets.model.toAsset
import org.rentalhouse.assets.service.AssetService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class AssetController(private val assetService: AssetService) {

    @PostMapping(value = ["/v1/assets"])
    fun add(@RequestBody assetAdditionRequest: AssetAdditionRequest): ResponseEntity<String> {
        val assetId = assetService.add(assetAdditionRequest.toAsset())
        return ResponseEntity.created("/v1/assets/$assetId".toUri()).build()
    }

    @GetMapping(value = ["/v1/assets/{assetId}"])
    fun findById(@PathVariable assetId: String): AssetView {
        val asset = assetService.findById(assetId)
        return AssetView.fromAssert(asset)
    }
}