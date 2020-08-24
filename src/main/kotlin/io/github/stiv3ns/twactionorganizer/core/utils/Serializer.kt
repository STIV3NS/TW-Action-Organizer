package io.github.stiv3ns.twactionorganizer.core.utils

import io.github.stiv3ns.twactionorganizer.core.Player
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

object Serializer {
    fun save(players: List<Player>, outputFile: File) {
        outputFile.writeText(
            Json.encodeToString(players)
        )
    }

    fun save(players: List<Player>, outFilePath: String) {
        val outputFile = File(outFilePath)
        save(players, outputFile)
    }

    fun restore(inputFile: File): List<Player> {
        val jsonData = inputFile.readText()
        return Json.decodeFromString(jsonData)
    }

    fun restore(inputFilePath: String): List<Player> {
        val inputFile = File(inputFilePath)
        return restore(inputFile)
    }
}