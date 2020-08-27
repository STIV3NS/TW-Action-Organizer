package io.github.stiv3ns.twactionorganizer.core.parsers

import io.github.stiv3ns.twactionorganizer.core.Player
import io.github.stiv3ns.twactionorganizer.core.Resources
import io.github.stiv3ns.twactionorganizer.core.World
import io.github.stiv3ns.twactionorganizer.core.villages.AllyVillage
import io.github.stiv3ns.twactionorganizer.core.villages.Village

class AllyParserWithDynamicOwnerResolution(val world: World) {
    var knownPlayers = mutableMapOf<String, Player>()

    private val players = mutableListOf<Player>()
    private var villages = mutableListOf<AllyVillage>()

    private val coordinatesRegex = Regex("\\d{3}\\|\\d{3}")

    fun parseAndGetResources(text: String): Resources {
        parseText(text)

        return Resources(players.toList(), villages.toList())
            .also {
                players.clear()
                villages.clear()
            }
    }

    private fun parseText(text: String) {
        coordinatesRegex.findAll(text).forEach { matchResult ->
            val coords = matchResult.value
            val (x, y) = coords.split("|").map { it.toInt() }

            val ownerNickname = world.fetchVillageOwner(Village(x, y))

            val owner = getPlayerObject(ownerNickname)

            villages.add(AllyVillage(x, y, owner))
        }
    }

    private fun getPlayerObject(nickname: String): Player =
        knownPlayers.getOrPut(
            key = nickname,
            defaultValue = {
                Player(nickname).also { players.add(it) }
            }
        )
}