package org.rentalhouse.assets.event.channel

import org.springframework.cloud.stream.annotation.Input
import org.springframework.messaging.SubscribableChannel

interface RentalDealChannels {

    @Input("rentalDeals")
    fun rentalDeals(): SubscribableChannel
}