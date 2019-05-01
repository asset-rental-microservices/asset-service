package org.rentalhouse.assets.model

import org.rentalhouse.assets.entity.Asset

class AssetView private constructor(val id: String,
                                    val status: AssetStatus,
                                    val plotIdentifier: String,
                                    val address: Address) {

    companion object {
        fun fromAssert(asset: Asset): AssetView {
            return AssetView(asset.id, asset.status, asset.plotIdentifier, Address.fromAddress(asset.address))
        }
    }
}