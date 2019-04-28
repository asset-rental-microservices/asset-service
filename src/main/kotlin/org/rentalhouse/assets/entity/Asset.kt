package org.rentalhouse.assets.entity

import org.springframework.data.annotation.Id

class Asset {

    @Id
    lateinit var id: String
    private set
}