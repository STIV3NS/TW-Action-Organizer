package io.github.stiv3ns.twactionorganizer.core.parsers

import io.github.stiv3ns.twactionorganizer.core.Player
import io.github.stiv3ns.twactionorganizer.core.Resources
import io.github.stiv3ns.twactionorganizer.core.World
import io.github.stiv3ns.twactionorganizer.core.villages.AllyVillage
import io.github.stiv3ns.twactionorganizer.core.villages.Village
import java.io.File
import java.io.IOException

class AllyParserWithDynamicOwnerResolution(val world: World) : AllyParser {
    var txtFilePath: String? = null

    var knownPlayers = mutableMapOf<String, Player>()
    var shouldRegisterVillages = true

    private val players = mutableListOf<Player>()
    private var villages = mutableListOf<AllyVillage>()

    @Throws(IOException::class)
    override fun parseAndGetResources(): Resources {
        if (!txtFilePath.isNullOrBlank()) {
            parseFile()

            return Resources(players.toMutableList(), villages.toMutableList())
                .also {
                    players.clear()
                    villages.clear()
                    knownPlayers.clear()
                }

        } else {
            throw IOException("Input file not specified.")
        }
    }

    private fun parseFile() {
        val file = File(txtFilePath!!)
        val fileContent = file.readText()

        val coordinatesRegex = Regex("\\d{3}\\|\\d{3}")
        val xyRegex = Regex("\\d{3}")

        coordinatesRegex.findAll(fileContent).forEach { matchResult ->
            val coordinates = matchResult.value

            with(xyRegex.findAll(coordinates)) {
                val x = first().value.toInt()
                val y = last().value.toInt()

                val ownerNick = world.fetchVillageOwner(Village(x, y))

                val owner = getPlayerObject(ownerNick)

                villages.add(AllyVillage(x, y, owner))

                if (shouldRegisterVillages) {
                    owner.registerVillage()
                }
            }
        }
    }

    private fun getPlayerObject(nickname: String): Player {
        if (!knownPlayers.containsKey(nickname)) {

            val newPlayer = Player(nickname)

            knownPlayers[nickname] = newPlayer
            players.add(newPlayer)
        }

        return knownPlayers[nickname]!!
    }
}