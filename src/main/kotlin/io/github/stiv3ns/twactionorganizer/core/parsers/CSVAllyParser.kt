package io.github.stiv3ns.twactionorganizer.core.parsers

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import io.github.stiv3ns.twactionorganizer.core.Player
import io.github.stiv3ns.twactionorganizer.core.Resources
import io.github.stiv3ns.twactionorganizer.core.utils.exceptions.MissingConfigurationException
import io.github.stiv3ns.twactionorganizer.core.villages.AllyVillage
import java.io.File
import java.io.IOException


class CSVAllyParser : AllyParser {
    var csvFilePath: String? = null

    var nicknameHeader: String? = null
    var noblesHeader: String? = null
    var villagesHeader: String? = null
    var villageRegexLiteral: String = "\\(\\d{3}\\|\\d{3}\\) [a-zA-Z]\\d{1,3}"

    var knownPlayers = mutableMapOf<String, Player>()

    private val requiredHeadersAreSet get() =
        !(nicknameHeader.isNullOrBlank()) && !(villagesHeader.isNullOrBlank()) && !(csvFilePath.isNullOrBlank())

    private val players = mutableListOf<Player>()
    private var villages = mutableListOf<AllyVillage>()

    @Throws(MissingConfigurationException::class, IOException::class)
    override fun parseAndGetResources(): Resources {
        if (requiredHeadersAreSet) {
            parseCSV()
            removeDuplicates()

            return Resources(players.toMutableList(), villages.toMutableList())
                .also {
                    players.clear()
                    villages.clear()
                    knownPlayers.clear()
                }
        } else {
            var exceptionMsg = "CSVAllyParser missing: "

            if (nicknameHeader.isNullOrBlank()) exceptionMsg += "nicknameHeader, "
            if (villagesHeader.isNullOrBlank()) exceptionMsg += "villagesHeader, "
            if (csvFilePath.isNullOrBlank()) exceptionMsg += "csvFilePath"


            throw MissingConfigurationException(exceptionMsg)
        }
    }

    private fun parseCSV() {
        val file = File(csvFilePath!!)
        val rows = csvReader().readAllWithHeader(file)

        rows.forEach { r ->
            val player = parsePlayer(r)
            parseVillages(r, forPlayer = player)
        }
    }

    private fun parsePlayer(row: Map<String, String>): Player {
        val nickname = row.getOrElse(
            nicknameHeader!!,
            { throw IOException() }
        )

        if (!knownPlayers.containsKey(nickname)) {
            val numberOfNobles = when (noblesHeader) {
                null -> 0
                else -> row.getOrElse(
                    noblesHeader!!,
                    { throw IOException() }
                ).toInt()
            }

            val newPlayer = Player(nickname, numberOfNobles)

            knownPlayers[nickname] = newPlayer
            players.add(newPlayer)
        }

        return knownPlayers[nickname]!!
    }

    private fun parseVillages(row: Map<String, String>, forPlayer: Player) {
        val coordinatesRegex = villageRegexLiteral.toRegex()
        val xyRegex = Regex("\\d{3}")

        val rawVillages = row.getOrElse(
            villagesHeader!!,
            { throw IOException() }
        )

        coordinatesRegex.findAll(rawVillages).forEach { matchResult ->
            val coordinates = matchResult.value

            with(xyRegex.findAll(coordinates)) {
                val x = first().value.toInt()
                val y = last().value.toInt()

                villages.add(AllyVillage(x, y, owner = forPlayer))
            }
        }
    }

    private fun removeDuplicates() {
        villages = villages.distinctBy { it.toString() }.toMutableList()
    }
}