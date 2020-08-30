package io.github.stiv3ns.twactionorganizer.core.parsers

import io.github.stiv3ns.twactionorganizer.core.World
import io.kotlintest.matchers.collections.shouldContainExactly
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec
import kotlinx.coroutines.ObsoleteCoroutinesApi

@ObsoleteCoroutinesApi
class AllyParserWithDynamicOwnerResolutionTest : WordSpec({
    "AllyParserWithDynamicOwnerResolution::parse" should {
        val world = World("plp6.plemiona.pl")

        val coordsToOwnerMap: Map<String, String> = mapOf(
            "500|499" to "Lucky1369",
            "497|506" to "adam11145",
        )

        val plainText = coordsToOwnerMap.keys.joinToString(separator = "\n")

        "properly parse the data /* test may fail due to domain expiration or owner change on actual server */" {
            val result = AllyParserWithDynamicOwnerResolution.parse(plainText, world)

            result.map { village -> village.toString() }
                .shouldContainExactly(coordsToOwnerMap.keys)

            result.forEach { village ->
                village.ownerNickname shouldBe coordsToOwnerMap[ village.toString() ]
            }
        }
    }
})