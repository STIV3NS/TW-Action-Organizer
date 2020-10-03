package io.github.stiv3ns.twactionorganizer.core.parsers

import io.github.stiv3ns.twactionorganizer.core.World
import io.github.stiv3ns.twactionorganizer.core.villages.TargetVillage
import io.github.stiv3ns.twactionorganizer.logging.Logger
import kotlinx.coroutines.ObsoleteCoroutinesApi

object TargetParser {
    @ObsoleteCoroutinesApi
    suspend fun parse(
        plainText: String,
        attacksPerVillage: Int,
        world: World,
        coordinatesRegex: Regex = Regex("\\d{3}\\|\\d{3}")
    ): Collection<TargetVillage>
    {
        val villages = mutableListOf<TargetVillage>()

        coordinatesRegex.findAll(plainText).forEach { matchResult ->
            val coords = matchResult.value
            val village = world.villages[coords]

            if (village != null)
                villages += TargetVillage(village, attacksPerVillage)
            else
                Logger.warn("[TargetParser] : $coords cannot be found on server ${world.domain}")
        }

        return villages
    }
}