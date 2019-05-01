package org.rentalhouse.assets.event

import org.rentalhouse.assets.model.Status
import org.rentalhouse.assets.service.AssetService
import org.springframework.stereotype.Component

@Component
class RentalDealEventsListener(private val assetService: AssetService) {

    fun dealInitialized(rentalDealInitialized: RentalDealInitialized) {
        assetService.updateStatus(rentalDealInitialized.assetId, Status.RENTAL_DEAL_INITIALIZED)
    }
}

class RentalDealInitialized(val assetId: String)