import io.github.stiv3ns.twactionorganizer.core.parsers.CSVAllyParser
import io.github.stiv3ns.twactionorganizer.core.utils.exceptions.MissingConfigurationException
import io.kotlintest.data.forall
import io.kotlintest.matchers.collections.shouldBeUnique
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotThrowAny
import io.kotlintest.shouldThrow
import io.kotlintest.specs.WordSpec
import io.kotlintest.tables.row
import java.io.IOException
import java.nio.file.Paths

class CSVAllyParserTest : WordSpec({
    "CSVAllyParser" When {
        val parser = CSVAllyParser()

        "given proper path to proper CSV file" should {
            val nicknameHeader = "nick"
            val villagesHeader = "offs"
            val noblesHeader = "noble"

            val filePath = Paths.get(
                this.javaClass
                    .classLoader
                    .getResource("AllyParser_dummy_data.csv")!!
                    .toURI()
            ).toAbsolutePath().toString()

            parser.csvFilePath = filePath

            "throw MissingConfigurationException when trying to parse w/o setting headers" {
                shouldThrow<MissingConfigurationException> {
                    parser.parseAndGetResources()
                }

                parser.nicknameHeader = nicknameHeader
                shouldThrow<MissingConfigurationException> {
                    parser.parseAndGetResources()
                }
            }

            "not throw an exception when required headers are set" {
                parser.nicknameHeader = nicknameHeader
                parser.villagesHeader = villagesHeader
                parser.noblesHeader = noblesHeader

                shouldNotThrowAny {
                    parser.parseAndGetResources()
                }
            }

            "Resources::players should be proper list of players" {
                val resources = parser.parseAndGetResources()
                val parsedPlayers = resources.players

                parsedPlayers.shouldHaveSize(3)

                if (parsedPlayers.size == 3) {
                    forall(
                        row("tomjo", 4, 24),
                        row("darek0729", 2, 60),
                        row("nicoleesme", 1, 50)
                    ) { nick, vilNum, nobNum ->
                        with(parsedPlayers.filter { it.nickname == nick }.first()) {
                            numberOfVillages shouldBe vilNum
                            numberOfNobles shouldBe nobNum
                        }
                    }
                }
            }

            "duplicates should be handled" {
                val newFilePath = Paths.get(
                    this.javaClass
                        .classLoader
                        .getResource("AllyParser_data_with_duplicates.csv")!!
                        .toURI()
                ).toAbsolutePath().toString()

                val newParser = CSVAllyParser().apply {
                    this.villagesHeader = villagesHeader
                    this.noblesHeader = noblesHeader
                    this.nicknameHeader = nicknameHeader
                    this.csvFilePath = newFilePath
                }

                val resources = newParser.parseAndGetResources()

                val parsedPlayer = resources.players.first()
                val parsedVillages = resources.villages

                parsedPlayer.numberOfVillages shouldBe 3
                parsedVillages.size shouldBe 3
                parsedVillages.shouldBeUnique()
            }
        }

        "given bad file path or path to non-CSV file" should {
            parser.apply {
                this.villagesHeader = "asdas"
                this.noblesHeader = "asdsadasd"
                this.nicknameHeader = "asadasdas"
            }
            "throw an exception" {
                shouldThrow<IOException> {
                    parser.csvFilePath = "bad/path.csv"
                    parser.parseAndGetResources()
                }
            }
        }
    }
})