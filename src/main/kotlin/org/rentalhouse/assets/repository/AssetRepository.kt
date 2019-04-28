package org.rentalhouse.assets.repository

import org.rentalhouse.assets.entity.Asset
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface AssetRepository : MongoRepository<Asset, String>