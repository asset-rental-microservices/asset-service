package org.rentalhouse.assets.model

data class AssetStatusUpdationRequest(val id: String, val status: Status)

enum class Status {
    RENTAL_DEAL_INITIALIZED
}