package io.github.stiv3ns.twactionorganizer.core.parsers

import io.github.stiv3ns.twactionorganizer.core.World
import io.github.stiv3ns.twactionorganizer.core.villages.AllyVillage
import io.github.stiv3ns.twactionorganizer.logging.Logger
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.runBlocking

object AllyParserWithDynamicOwnerResolution {
    @ObsoleteCoroutinesApi
    suspend fun parse(
        plainText: String,
        world: World,
        coordinatesRegex: Regex = Regex("\\d{3}\\|\\d{3}")
    ): Collection<AllyVillage> =
        coordinatesRegex
            .findAll(plainText)
            .mapNotNull { matchResult ->
                val coords = matchResult.value
                val village = world.villages[coords]

                village
                    ?: runBlocking {
                        Logger.warn("[AllyParserWithDynamicOwnerResolution] : $coords cannot be found on server ${world.domain}")
                        null
                    }
            }.toList()
}