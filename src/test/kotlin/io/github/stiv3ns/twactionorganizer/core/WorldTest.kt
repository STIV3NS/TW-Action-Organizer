import io.github.stiv3ns.twactionorganizer.core.World
import io.github.stiv3ns.twactionorganizer.core.utils.exceptions.BadDomainException
import io.github.stiv3ns.twactionorganizer.core.utils.exceptions.VillageNotFoundException
import io.github.stiv3ns.twactionorganizer.core.villages.Village
import io.kotlintest.data.forall
import io.kotlintest.shouldBe
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

        "given good domain /*test domain viable until 2021*/" should {
            val world = World("plp6.plemiona.pl")
            /* if tests fail you should try changing domain since this one may be already closed */

            "set proper domain" {
                world.domain shouldBe "https://plp6.plemiona.pl"
            }

            "load proper settings" {
                with(world) {
                    maxNobleRange shouldBe 1000000
                    nightBonusEndHour shouldBe 8
                    speed shouldBe 1.0
                }
            }

            "load proper Village.id" {
                forall(
                    row(Village(500, 499), 15),
                    row(Village(497, 506), 44),
                    row(Village(508, 499), 66)
                ) { village, id ->
                    world.fetchVillageId(village) shouldBe id
                }
            }

            /* this test may (and will) randomly crash when village changes owner... */
            "load proper Village.owner" {
                forall(
                    row(Village(500, 499), "Lucky1369"),
                    row(Village(497, 506), "adam11145"),
                    row(Village(508, 499), "adam11145"),
                    row(Village(707, 411), "Lord Królewski Aromat")
                ) { vil, owner ->
                    world.fetchVillageOwner(vil) shouldBe owner
                }
            }

            "throw an exception when trying to handle non-existing village" {
                shouldThrow<VillageNotFoundException> {
                    world.fetchVillageId(Village(-1, -1))
                }
            }
        }
    }
})
