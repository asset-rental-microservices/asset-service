package org.rentalhouse.assets.entity

import org.springframework.data.annotation.Id

class Asset(val plotIdentifier: String, val address: Address) {

    @Id
    lateinit var id: String
        private set

    fun street()    = address.street
    fun city()      = address.city
    fun state()     = address.state
    fun pinCode()   = address.pinCode
}

class Address(val street: String, val city: String, val state: String, val pinCode: String)