package org.rentalhouse.assets.model

import org.rentalhouse.assets.entity.Asset

class AssetAdditionRequest {

}

fun AssetAdditionRequest.toAsset() = Asset()