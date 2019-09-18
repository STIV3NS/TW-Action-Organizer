import io.kotlintest.data.forall
import io.kotlintest.matchers.collections.shouldBeUnique
import io.kotlintest.matchers.collections.shouldContainExactly
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotThrowAny
import io.kotlintest.shouldThrow
import io.kotlintest.specs.WordSpec
import io.kotlintest.tables.row
import twao.Player
import twao.exceptions.UnspecifiedKeyException
import twao.loaders.AllyLoader
import twao.villages.AllyVillage
import java.io.IOException
import java.nio.file.Paths

class AllyLoaderTest : WordSpec({
    "AllyLoader" When {
        "given bad file path or path non-CSV file" should {
            "throw an exception" {
                shouldThrow<IOException> {
                    AllyLoader("bad/path.csv")
                }
            }
        }

        "given proper CSV file" should {
            val nicknameKey = "nick"
            val vilagesKey = "offs"
            val noblesKey = "noble"

            var filePath = Paths.get(
                    this.javaClass
                            .classLoader
                            .getResource("AllyLoader_dummy_data.csv")!!
                            .toURI()
            ).toAbsolutePath().toString()
            var loader = AllyLoader(filePath)

            "throw UnspecifiedKeyException when trying to fetchData w/o setting keys" {
                shouldThrow<UnspecifiedKeyException> {
                    loader.fetchData()
                }

                loader.setNicknameKey(nicknameKey)
                shouldThrow<UnspecifiedKeyException> {
                    loader.fetchData()
                }

                loader.setVillagesKey(vilagesKey)
                shouldThrow<UnspecifiedKeyException> {
                    loader.fetchData()
                }
            }

            "not throw an exception when all keys are set" {
                loader.setNobleKey(noblesKey)
                shouldNotThrowAny {
                    loader.fetchData()
                }
            }

            "::getPlayers should return proper list of players" {
                val loadedPlayers = loader.players

                loadedPlayers.shouldHaveSize(3)

                if (loadedPlayers.size == 3) {
                    forall (
                            row("tomjo", 4, 24),
                            row("darek0729", 2, 60),
                            row("nicoleesme", 1, 50)
                    ) { nick, vilNum, nobNum ->
                        with(loadedPlayers.filter { it.nickname == nick }
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
                                .getResource("AllyLoader_data_with_duplicates.csv")!!
                                .toURI()
                ).toAbsolutePath().toString()
                var loader = AllyLoader(filePath).also {
                    with(it) {
                        setNicknameKey(nicknameKey)
                        setVillagesKey(vilagesKey)
                        setNobleKey(noblesKey)
                        fetchData()
                    }
                }

                val loadedPlayer = loader.players.first()
                val loadedVillages = loader.villages

                loadedPlayer.numberOfVillages shouldBe 3
                loadedVillages.size shouldBe 3
                loadedVillages.shouldBeUnique()
            }
        }
    }
})