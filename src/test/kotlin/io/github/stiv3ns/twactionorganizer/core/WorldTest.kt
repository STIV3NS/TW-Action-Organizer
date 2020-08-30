import io.github.stiv3ns.twactionorganizer.core.World
import io.github.stiv3ns.twactionorganizer.core.utils.exceptions.BadDomainException
import io.github.stiv3ns.twactionorganizer.core.villages.Village
import io.kotlintest.data.forall
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.WordSpec
import io.kotlintest.tables.row

class WorldTest : WordSpec({
    "World" When {
        "given bad domain" should {
            "throw an exception" {
                shouldThrow<BadDomainException> {
                    World("bad.domain")
                }
            }
        }

        "given good domain /* test may fail due to domain expiration */" should {
            /* if tests fail try changing domain since this one may be already closed */
            val world = World("plp6.plemiona.pl")
            val world2 = World("https://plp6.plemiona.pl")

            "set proper domain" {
                world.domain shouldBe "https://plp6.plemiona.pl"
                world2.domain shouldBe "https://plp6.plemiona.pl"
            }

            "fetch proper settings" {
                with(world) {
                    maxNobleRange shouldBe 1000000
                    nightBonusEndHour shouldBe 8
                    speed shouldBe 1.0
                }
            }

            "fetch proper village id and owner /* this test may (and will) randomly crash when village changes owner... */" {
                forall(
                    row("500|499",
                        Village(x = 500, y = 499, id = 15, ownerNickname = "Lucky1369")),
                    row("497|506",
                        Village(x = 497, y = 506, id = 44, ownerNickname = "adam11145")),
                    row("508|499",
                        Village(x = 508, y = 499, id = 66, ownerNickname = "adam11145")),
                ) { coords, expectedVillage ->
                    val fetched = world.villages[coords]

                    fetched shouldNotBe null

                    if (fetched != null) {
                        fetched.x shouldBe expectedVillage.x
                        fetched.y shouldBe expectedVillage.y
                        fetched.id shouldBe expectedVillage.id
                        fetched.ownerNickname shouldBe expectedVillage.ownerNickname
                    }
                }
            }
        }
    }
})
