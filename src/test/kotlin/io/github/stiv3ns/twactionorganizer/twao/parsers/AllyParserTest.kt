import io.kotlintest.data.forall
import io.kotlintest.matchers.collections.shouldBeUnique
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotThrowAny
import io.kotlintest.shouldThrow
import io.kotlintest.specs.WordSpec
import io.kotlintest.tables.row
import io.github.stiv3ns.twactionorganizer.twao.utils.exceptions.UnspecifiedHeaderException
import io.github.stiv3ns.twactionorganizer.twao.parsers.AllyParser
import java.io.IOException
import java.nio.file.Paths

class AllyParserTest : WordSpec({
    "AllyParser" When {
        "given bad file path or path to non-CSV file" should {
            "throw an exception" {
                shouldThrow<IOException> {
                    AllyParser("bad/path.csv")
                }
            }
        }


        "given proper path to proper CSV file" should {
            val nicknameHeader = "nick"
            val offsHeader = "offs"
            val noblesHeader = "noble"

            var filePath = Paths.get(
                    this.javaClass
                            .classLoader
                            .getResource("AllyParser_dummy_data.csv")!!
                            .toURI()
            ).toAbsolutePath().toString()
            var parser = AllyParser(filePath)

            "throw UnspecifiedHeaderException when trying to parse w/o setting headers" {
                shouldThrow<UnspecifiedHeaderException> {
                    parser.parse()
                }

                parser.setNicknameHeader(nicknameHeader)
                shouldThrow<UnspecifiedHeaderException> {
                    parser.parse()
                }

                parser.setOffsHeader(offsHeader)
                shouldThrow<UnspecifiedHeaderException> {
                    parser.parse()
                }
            }

            "not throw an exception when all headers are set" {
                parser.setNoblesHeader(noblesHeader)
                shouldNotThrowAny {
                    parser.parse()
                }
            }

            "::getPlayers should return proper list of players" {
                val parsedPlayers = parser.players

                parsedPlayers.shouldHaveSize(3)

                if (parsedPlayers.size == 3) {
                    forall (
                            row("tomjo", 4, 24),
                            row("darek0729", 2, 60),
                            row("nicoleesme", 1, 50)
                    ) { nick, vilNum, nobNum ->
                        with(parsedPlayers.filter { it.nickname == nick }
                                .first()) {
                            this.shouldNotBeNull()

                            numberOfVillages shouldBe vilNum
                            numberOfNobles shouldBe nobNum
                        }
                    }
                }
            }

            "duplicates should be handled" {
                filePath = Paths.get(
                        this.javaClass
                                .classLoader
                                .getResource("AllyParser_data_with_duplicates.csv")!!
                                .toURI()
                ).toAbsolutePath().toString()
                var parser = AllyParser(filePath).apply {
                    setNicknameHeader(nicknameHeader)
                    setOffsHeader(offsHeader)
                    setNoblesHeader(noblesHeader)
                    parse()
                }

                val parsedPlayer = parser.players.first()
                val parsedVillages = parser.villages

                parsedPlayer.numberOfVillages shouldBe 3
                parsedVillages.size shouldBe 3
                parsedVillages.shouldBeUnique()
            }
        }
    }
})