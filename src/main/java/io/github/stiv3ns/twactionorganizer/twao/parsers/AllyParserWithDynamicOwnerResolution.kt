package io.github.stiv3ns.twactionorganizer.twao.parsers

import io.github.stiv3ns.twactionorganizer.twao.Player
import io.github.stiv3ns.twactionorganizer.twao.Resources
import io.github.stiv3ns.twactionorganizer.twao.World
import io.github.stiv3ns.twactionorganizer.twao.villages.AllyVillage
import io.github.stiv3ns.twactionorganizer.twao.villages.Village
import java.io.File
import java.io.IOException

class AllyParserWithDynamicOwnerResolution(val world: World) : AllyParser {
    var txtFilePath: String? = null

    private val players = mutableListOf<Player>()
    private var villages = mutableListOf<AllyVillage>()

    private val knownPlayers = mutableMapOf<String, Player>()

    @Throws(IOException::class)
    override fun parseAndGetResources(): Resources {
        if ( ! txtFilePath.isNullOrBlank() ) {
            parseFile()

            return Resources(players.toList(), villages.toList())
            .also {
                players.clear()
                villages.clear()
                knownPlayers.clear()
            }

        } else {
            throw IOException("Input file not specified.");
        }
    }

    private fun parseFile() {
        val file = File(txtFilePath!!)
        val fileContent = file.readText()

        val coordinatesRegex = Regex("\\d{3}\\|\\d{3}")
        val xyRegex = Regex("\\d{3}")

        coordinatesRegex.findAll(fileContent).forEach { matchResult ->
            val coordinates = matchResult.value

            with (xyRegex.findAll(coordinates)) {
                val x = first().value.toInt()
                val y = last().value.toInt()

                val ownerNick = world.fetchVillageOwner(
                        object : Village {
                            override val x: Int = x
                            override val y: Int = y
                            override var id: Int? = null
                        }
                )

                val owner = getPlayerObject(ownerNick)

                villages.add( AllyVillage(x, y, owner) )
                owner.registerVillage()
            }
        }
    }

    private fun getPlayerObject(nickname: String): Player {
        if ( !knownPlayers.containsKey(nickname) ) {

            val newPlayer = Player(nickname)

            knownPlayers[nickname] = newPlayer
            players.add(newPlayer)
        }

        return knownPlayers[nickname]!!
    }
}