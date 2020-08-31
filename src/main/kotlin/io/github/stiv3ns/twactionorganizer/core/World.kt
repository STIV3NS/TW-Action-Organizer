package io.github.stiv3ns.twactionorganizer.core

import io.github.stiv3ns.twactionorganizer.core.utils.exceptions.BadDomainException
import io.github.stiv3ns.twactionorganizer.core.villages.Village
import org.w3c.dom.Document
import java.net.URL
import java.net.URLDecoder
import javax.xml.parsers.DocumentBuilderFactory

class World(domain: String) {
    val domain: String
    val worldSpeed: Double
    val unitSpeed: Double
    val speed: Double get() = worldSpeed * unitSpeed
    val maxNobleRange: Int
    val nightBonusEndHour: Int
    val villages: Map<String, Village>

    init {
        if (!domain.startsWith("https://"))
            this.domain = "https://$domain"
        else
            this.domain = domain

        try {
            val doc = getXMLDocumentWithConfig(this.domain)

            worldSpeed = doc.getElementsByTagName("speed").item(0).textContent.toDouble()
            unitSpeed = doc.getElementsByTagName("unit_speed").item(0).textContent.toDouble()

            val normalizedNobleRange = doc.getElementsByTagName("max_dist").item(0).textContent.toInt()
            maxNobleRange = normalizedNobleRange * normalizedNobleRange

            nightBonusEndHour = doc.getElementsByTagName("end_hour").item(0).textContent.toInt()

            villages = initializeVillages()
        }
        catch (e: Exception) {
            throw BadDomainException("$domain is not a valid TW server or a connection error occurred")
        }
    }

    private fun getXMLDocumentWithConfig(domain: String): Document {
        val url = URL("$domain/interface.php?func=get_config")
        val docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()

        return docBuilder.parse(url.openStream()).apply { documentElement.normalize() }
    }

    private fun initializeVillages(): Map<String, Village> {
        val playerIdToNicknameMap = getPlayerIdToNicknameMap()

        return URL("$domain/map/village.txt")
            .openStream()
            .reader()
            .use { r -> r.readLines() }
            .asSequence()
            .map { line -> line.split(",") }
            .mapNotNull { arr ->
                val (x, y) = listOf(arr[2], arr[3]).map { it.toInt() }
                val coords = "$x|$y"
                val id = arr[0].toInt()
                val ownerId = arr[4].toInt()
                val ownerNickname = playerIdToNicknameMap[ownerId]

                if (ownerNickname != null)
                    coords to Village(x, y, id, ownerNickname)
                else null
            }.toMap()

    }

    private fun getPlayerIdToNicknameMap(): Map<Int, String> =
        URL("$domain/map/player.txt")
            .openStream()
            .reader()
            .use { r -> r.readLines() }
            .asSequence()
            .map { line -> line.split(",") }
            .map { arr ->
                val id = arr[0].toInt()
                val nickname = arr[1].let { URLDecoder.decode(it) }

                id to nickname
            }.toMap()
}