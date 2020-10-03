package io.github.stiv3ns.twactionorganizer.core.villages

import kotlinx.serialization.Serializable

@Serializable
open class Village(
    val x: Int,
    val y: Int,
    val id: Int,
    val ownerNickname: String
) {
    infix fun distanceTo(v2: Village): Int {
        fun Int.squared(): Int = this * this

        return (this.x - v2.x).squared() + (this.y - v2.y).squared()
    }

    override fun toString() = "$x|$y"
}