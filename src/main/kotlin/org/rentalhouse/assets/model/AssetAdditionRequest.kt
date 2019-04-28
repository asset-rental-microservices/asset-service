package org.rentalhouse.assets.model

import org.rentalhouse.assets.entity.Asset

class AssetAdditionRequest(val identifier: String, val address :Address)

class Address(val street: String, val city: String, val state: String, val pinCode: String) {

    companion object {
        fun fromAddress(address: org.rentalhouse.assets.entity.Address): Address {
            return Address(address.street, address.city, address.state, address.pinCode)
        }
    }
}

fun Address.toAddress()            = org.rentalhouse.assets.entity.Address(street, city, state, pinCode)
fun AssetAdditionRequest.toAsset() = Asset(identifier, address.toAddress())