package org.rentalhouse.assets.fixture

import org.rentalhouse.assets.entity.Address
import org.rentalhouse.assets.entity.Asset
import org.springframework.test.util.ReflectionTestUtils

class AssetBuilder {

    lateinit var plotIdentifier: String
    private  var address: Address = Address(state = "", street = "", city = "", pinCode = "")

    fun build(): Asset = Asset(plotIdentifier, address)

    fun address(block: AddressBuilder.() -> Unit): Address =
        AddressBuilder()
            .apply(block)
            .build()
            .also { this.address = it }
}

class AddressBuilder {
    var street: String     = ""
    var city: String       = ""
    var state: String      = ""
    var pinCode: String    = ""

    fun build(): Address = Address(street, city, state, pinCode)
}

fun asset(block: AssetBuilder.() -> Unit): Asset = AssetBuilder().apply(block).build()

fun asset(id: String, block: AssetBuilder.() -> Unit): Asset = AssetBuilder()
    .apply(block)
    .build()
    .run {
        ReflectionTestUtils.setField(this, "id", id)
        this
    }