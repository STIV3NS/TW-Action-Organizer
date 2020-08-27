package io.github.stiv3ns.twactionorganizer.core.parsers

import io.github.stiv3ns.twactionorganizer.core.villages.TargetVillage

object TargetParser {
    private val coordinatesRegex = Regex("\\d{3}\\|\\d{3}")

    fun parse(
        text: String,
        attacksPerVillage: Int,
        appendTo: MutableCollection<TargetVillage>
    )
    {
        coordinatesRegex.findAll(text).forEach { matchResult ->
            val coords = matchResult.value
            val (x, y) = coords.split("|").map { it.toInt() }

            appendTo += TargetVillage(x, y, attacksPerVillage)
        }
    }
}