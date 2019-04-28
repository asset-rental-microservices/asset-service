package org.rentalhouse.assets.model

import org.rentalhouse.assets.entity.Asset

class AssetView private constructor(val identifier: String, val address: Address) {

    companion object {
        fun fromAssert(asset: Asset): AssetView {
            return AssetView(asset.identifier, Address.fromAddress(asset.address))
        }
    }
}