import io.github.stiv3ns.twactionorganizer.core.parsers.TargetParser
import io.github.stiv3ns.twactionorganizer.core.villages.TargetVillage
import io.kotlintest.matchers.collections.shouldContainExactly
import io.kotlintest.shouldThrow
import io.kotlintest.specs.WordSpec
import java.io.File
import java.io.FileNotFoundException
import java.nio.file.Paths

class TargetParserTest : WordSpec({
    "TargetParser.parse" should {
        val outputList = mutableListOf<TargetVillage>()

        val attacksPerFirstGroup = 5
        val attacksPerSecondGroup = 17

        val filePath = Paths.get(
            this.javaClass
                .classLoader
                .getResource("TargetParser_dummy_data.txt")!!
                .toURI()
        ).toAbsolutePath().toString()

        val filePath_2 = Paths.get(
            this.javaClass
                .classLoader
                .getResource("TargetParser_dummy_data_2.txt")!!
                .toURI()
        ).toAbsolutePath().toString()

        "properly parse the file and add data to given list" {
            TargetParser.parse(File(filePath).readText(), attacksPerFirstGroup, outputList)
            TargetParser.parse(File(filePath_2).readText(), attacksPerSecondGroup, outputList)

            outputList.shouldContainExactly(
                TargetVillage(x = 623, y = 574, numberOfAttacks = attacksPerFirstGroup),
                TargetVillage(x = 626, y = 576, numberOfAttacks = attacksPerFirstGroup),
                TargetVillage(x = 626, y = 578, numberOfAttacks = attacksPerFirstGroup),
                TargetVillage(x = 625, y = 576, numberOfAttacks = attacksPerFirstGroup),
                TargetVillage(x = 597, y = 541, numberOfAttacks = attacksPerSecondGroup),
                TargetVillage(x = 612, y = 550, numberOfAttacks = attacksPerSecondGroup)
            )
        }
    }
})