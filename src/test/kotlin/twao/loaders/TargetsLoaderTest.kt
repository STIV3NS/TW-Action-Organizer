import io.kotlintest.matchers.collections.shouldContainExactly
import io.kotlintest.shouldThrow
import io.kotlintest.specs.WordSpec
import twao.loaders.TargetsLoader
import twao.villages.TargetVillage
import java.io.FileNotFoundException
import java.nio.file.Paths

class TargetsLoaderTest : WordSpec({
    "TargetsLoader.load" When {
        "given bad file path" should {
            "throw an exception" {
                shouldThrow<FileNotFoundException> {
                    TargetsLoader.load("bad/path.txt", 5, mutableListOf())
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
                            .getResource("TargetsLoader_dummy_data.txt")!!
                            .toURI()
            ).toAbsolutePath().toString()

            val filePath_2 = Paths.get(
                    this.javaClass
                            .classLoader
                            .getResource("TargetsLoader_dummy_data_2.txt")!!
                            .toURI()
            ).toAbsolutePath().toString()

            "properly parse the file and load data to given list" {
                TargetsLoader.load(filePath, attacksPerFirstGroup, outputList)
                TargetsLoader.load(filePath_2, attacksPerSecondGroup, outputList)

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