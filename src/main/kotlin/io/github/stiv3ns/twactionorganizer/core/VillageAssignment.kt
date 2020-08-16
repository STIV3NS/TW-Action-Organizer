package io.github.stiv3ns.twactionorganizer.core

import io.github.stiv3ns.twactionorganizer.core.villages.Village
import kotlinx.serialization.Serializable

@Serializable
data class VillageAssignment(
    val departure: Village,
    val destination: Village,
    val squaredDistance: Int,
    val delayInMinutes: Long = 0
) {
    override fun toString() = "$departure -> $destination"
}