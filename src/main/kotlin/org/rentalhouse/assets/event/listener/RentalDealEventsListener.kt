package org.rentalhouse.assets.event.listener

import org.rentalhouse.assets.event.channel.RentalDealChannels
import org.rentalhouse.assets.model.Status
import org.rentalhouse.assets.service.AssetService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cloud.stream.annotation.EnableBinding
import org.springframework.cloud.stream.annotation.StreamListener
import org.springframework.stereotype.Component

@Component
@EnableBinding(RentalDealChannels::class)
class RentalDealEventsListener(private val assetService: AssetService) {

    private val logger: Logger = LoggerFactory.getLogger(RentalDealEventsListener::class.java)

    @StreamListener("rentalDeals")
    fun dealInitialized(rentalDealInitialized: RentalDealInitialized) {
        logger.info("Received rentalDealInitialized for ${rentalDealInitialized.assetId}")
        assetService.updateStatus(rentalDealInitialized.assetId, Status.RENTAL_DEAL_INITIALIZED)
    }
}

class RentalDealInitialized(val assetId: String)