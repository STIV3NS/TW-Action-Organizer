import io.github.stiv3ns.twactionorganizer.core.World
import io.github.stiv3ns.twactionorganizer.core.parsers.TargetParser
import io.github.stiv3ns.twactionorganizer.core.villages.TargetVillage
import io.kotlintest.inspectors.forAny
import io.kotlintest.matchers.collections.shouldContainExactly
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec
import kotlinx.coroutines.ObsoleteCoroutinesApi

@ObsoleteCoroutinesApi
class TargetParserTest : WordSpec({
    "TargetParser::parse" should {
        val world = World("plp6.plemiona.pl")

        val outputList = mutableListOf<TargetVillage>()

        val attacksPerFirstGroup = 5
        val attacksPerSecondGroup = 17

        val villages_1 = listOf(
            "497|506",
            "499|492",
            "499|491",
            "506|509",
        )

        val villages_2 = listOf(
            "500|499",
            "328|218",
        )

        val plainText_1 = villages_1.map { "[coord]$it$[/coord]" }.joinToString(separator = "\n")
        val plainText_2 = villages_2.map { "[coord]$it$[/coord]" }.joinToString(separator = "\n")

        "properly parse the data and add villages to given list /* test may fail due to domain expiration */" {
            TargetParser.parse(plainText_1, attacksPerFirstGroup, appendTo = outputList, world = world)
            TargetParser.parse(plainText_2, attacksPerSecondGroup, appendTo = outputList, world = world)

            val result = outputList.map { targetVillage ->
                targetVillage.toString() to targetVillage.numberOfAttacks
            }.toMap()

            villages_1.forEach { coords -> result[coords] shouldBe attacksPerFirstGroup }
            villages_2.forEach { coords -> result[coords] shouldBe attacksPerSecondGroup }
        }
    }
})