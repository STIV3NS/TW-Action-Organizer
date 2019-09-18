import io.kotlintest.matchers.collections.shouldContainExactly
import io.kotlintest.shouldThrow
import io.kotlintest.specs.WordSpec
import twao.parsers.TargetParser
import twao.villages.TargetVillage
import java.io.FileNotFoundException
import java.nio.file.Paths

class TargetParserTest : WordSpec({
    "TargetParser.parse" When {
        "given bad file path" should {
            "throw an exception" {
                shouldThrow<FileNotFoundException> {
                    TargetParser.parse("bad/path.txt", 5, mutableListOf())
                }
            }
        }

        "given good file path" should {
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
                TargetParser.parse(filePath, attacksPerFirstGroup, outputList)
                TargetParser.parse(filePath_2, attacksPerSecondGroup, outputList)

                outputList.shouldContainExactly(
                        TargetVillage(x = 623, y = 574, _numberOfAttacks = attacksPerFirstGroup),
                        TargetVillage(x = 626, y = 576, _numberOfAttacks = attacksPerFirstGroup),
                        TargetVillage(x = 626, y = 578, _numberOfAttacks = attacksPerFirstGroup),
                        TargetVillage(x = 625, y = 576, _numberOfAttacks = attacksPerFirstGroup),
                        TargetVillage(x = 597, y = 541, _numberOfAttacks = attacksPerSecondGroup),
                        TargetVillage(x = 612, y = 550, _numberOfAttacks = attacksPerSecondGroup)
                )
            }
        }
    }
})