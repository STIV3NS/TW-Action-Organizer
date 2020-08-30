package io.github.stiv3ns.twactionorganizer.core

import kotlinx.serialization.Serializable

@Serializable
data class Player(
    val nickname: String,
    val numberOfNobles: Int = 0
)