package io.github.stiv3ns.twactionorganizer.core.utils

import io.github.stiv3ns.twactionorganizer.core.Player
import kotlinx.serialization.builtins.list
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import java.io.File

object ActionSerializer {
    private val json = Json(JsonConfiguration.Stable)

    fun save(players: List<Player>, outputFile: File) {
        val jsonData = json.stringify(Player.serializer().list, players)
        outputFile.writeText(jsonData)
    }

    fun save(players: List<Player>, outFilePath: String) {
        val outputFile = File(outFilePath)
        save(players, outputFile)
    }

    fun restore(inputFile: File): List<Player> {
        val jsonData = inputFile.readText()
        return json.parse(Player.serializer().list, jsonData)
    }

    fun restore(inputFilePath: String): List<Player> {
        val inputFile = File(inputFilePath)
        return restore(inputFile)
    }
}