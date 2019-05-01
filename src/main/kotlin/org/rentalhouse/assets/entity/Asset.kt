package org.rentalhouse.assets.entity

import org.rentalhouse.assets.model.Status
import org.springframework.data.annotation.Id

class Asset(val plotIdentifier: String, val address: Address) {

    @Id
    lateinit var id: String
        private set

    var status: Status = Status.AVAILABLE
        private set

    fun street()    = address.street
    fun city()      = address.city
    fun state()     = address.state
    fun pinCode()   = address.pinCode

    fun updateStatus(status: Status): Asset {
        this.status = status
        return this
    }
}

class Address(val street: String, val city: String, val state: String, val pinCode: String)