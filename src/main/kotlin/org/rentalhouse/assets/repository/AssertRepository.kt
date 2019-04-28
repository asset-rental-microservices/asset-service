package org.rentalhouse.assets.repository

import org.rentalhouse.assets.entity.Asset
import org.springframework.data.mongodb.repository.MongoRepository

interface AssertRepository : MongoRepository<Asset, String>