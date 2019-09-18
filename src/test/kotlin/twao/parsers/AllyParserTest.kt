import io.kotlintest.data.forall
import io.kotlintest.matchers.collections.shouldBeUnique
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotThrowAny
import io.kotlintest.shouldThrow
import io.kotlintest.specs.WordSpec
import io.kotlintest.tables.row
import twao.exceptions.UnspecifiedKeyException
import twao.parsers.AllyParser
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
            val nicknameKey = "nick"
            val vilagesKey = "offs"
            val noblesKey = "noble"

            var filePath = Paths.get(
                    this.javaClass
                            .classLoader
                            .getResource("AllyParser_dummy_data.csv")!!
                            .toURI()
            ).toAbsolutePath().toString()
            var parser = AllyParser(filePath)

            "throw UnspecifiedKeyException when trying to parse w/o setting keys" {
                shouldThrow<UnspecifiedKeyException> {
                    parser.parse()
                }

                parser.setNicknameKey(nicknameKey)
                shouldThrow<UnspecifiedKeyException> {
                    parser.parse()
                }

                parser.setVillagesKey(vilagesKey)
                shouldThrow<UnspecifiedKeyException> {
                    parser.parse()
                }
            }

            "not throw an exception when all keys are set" {
                parser.setNobleKey(noblesKey)
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
                var parser = AllyParser(filePath).also {
                    with(it) {
                        setNicknameKey(nicknameKey)
                        setVillagesKey(vilagesKey)
                        setNobleKey(noblesKey)
                        parse()
                    }
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