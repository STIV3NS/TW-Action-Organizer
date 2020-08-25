package io.github.stiv3ns.twactionorganizer.core

import io.github.stiv3ns.twactionorganizer.core.utils.exceptions.BadDomainException
import io.github.stiv3ns.twactionorganizer.core.utils.exceptions.VillageNotFoundException
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

    private lateinit var playerIdToNicknameMap: Map<Int, String>
    private lateinit var villageCoordsToEntityMap: Map<String, VillageEntity>

    private class VillageEntity(val id: Int, val ownerId: Int)

    init {
        if (!domain.startsWith("https://")) {
            this.domain = "https://" + domain;
        } else {
            this.domain = domain
        }
        try {
            val doc = getXMLDocumentWithConfig(this.domain)

            worldSpeed = doc.getElementsByTagName("speed").item(0).textContent.toDouble()
            unitSpeed = doc.getElementsByTagName("unit_speed").item(0).textContent.toDouble()

            val normalizedNobleRange = doc.getElementsByTagName("max_dist").item(0).textContent.toInt()
            maxNobleRange = normalizedNobleRange * normalizedNobleRange

            nightBonusEndHour = doc.getElementsByTagName("end_hour").item(0).textContent.toInt()

            initializeMaps()
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

    private fun initializeMaps() {
        playerIdToNicknameMap = URL("$domain/map/player.txt")
            .openStream()
            .reader()
            .use { r ->
                r.readLines()
                    .map { line ->
                        line.split(",")
                            .let { arr ->
                                val id = arr[0].toInt()
                                val nickname = arr[1].let { URLDecoder.decode(it) }

                                Pair(id, nickname)
                            }
                    }
                    .toMap()
            }

        villageCoordsToEntityMap = URL("$domain/map/village.txt")
            .openStream()
            .reader()
            .use { r ->
                r.readLines()
                    .map { line ->
                        line.split(",")
                            .let { arr ->
                                val id = arr[0].toInt()
                                val ownerId = arr[4].toInt()
                                val coords = "${arr[2]}|${arr[3]}"

                                Pair(coords, VillageEntity(id, ownerId))
                            }
                    }
                    .toMap()
            }
    }

    @Throws(VillageNotFoundException::class)
    fun fetchVillageId(village: Village): Int =
        villageCoordsToEntityMap[village.toString()]
            ?.id ?: throw VillageNotFoundException("$village cannot be found on $domain")

    @Throws(VillageNotFoundException::class)
    fun fetchVillageOwner(village: Village): String =
        villageCoordsToEntityMap[village.toString()]
            ?.ownerId?.let { ownerId ->
                playerIdToNicknameMap[ownerId]
            } ?: throw VillageNotFoundException("$village cannot be found on $domain")
}