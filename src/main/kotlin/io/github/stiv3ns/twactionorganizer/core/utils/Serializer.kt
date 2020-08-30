package io.github.stiv3ns.twactionorganizer.core.utils

import io.github.stiv3ns.twactionorganizer.core.Assignment
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object Serializer {
    fun toJson(assignments: List<Assignment>): String =
        Json.encodeToString(assignments)

    fun fromJson(json: String): List<Assignment> =
        Json.decodeFromString(json)
}