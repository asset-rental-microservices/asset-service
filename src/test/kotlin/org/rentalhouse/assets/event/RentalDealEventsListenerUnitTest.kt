package org.rentalhouse.assets.event

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.rentalhouse.assets.model.AssetStatusUpdationRequest
import org.rentalhouse.assets.model.Status
import org.rentalhouse.assets.service.AssetService

class RentalDealEventsListenerUnitTest {

    private val assetService             = mockk<AssetService>(relaxed = true)
    private val rentalDealEventsListener = RentalDealEventsListener(assetService)

    @Test
    fun `should invoke service to update the status of asset to RENTAL_DEAL_INITIALIZED on receiving an event of type RentalDealInitialized`() {

        rentalDealEventsListener.dealInitialized(RentalDealInitialized(assetId = "1000"))
        verify { assetService.updateStatus(AssetStatusUpdationRequest("1000", Status.RENTAL_DEAL_INITIALIZED)) }
    }
}