package org.rentalhouse.assets.extension

import java.net.URI

fun String.toUri(): URI = URI.create(this)