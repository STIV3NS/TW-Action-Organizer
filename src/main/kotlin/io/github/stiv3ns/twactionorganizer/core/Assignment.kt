package io.github.stiv3ns.twactionorganizer.core

import io.github.stiv3ns.twactionorganizer.core.assigners.AssignerType
import io.github.stiv3ns.twactionorganizer.core.villages.Village
import kotlinx.serialization.Serializable

@Serializable
data class Assignment(
    val departure: Village,
    val destination: Village,
    val squaredDistance: Int,
    val delayInMinutes: Long = 0,
    val type: AssignerType
) {
    override fun toString() = "$departure -> $destination"
}