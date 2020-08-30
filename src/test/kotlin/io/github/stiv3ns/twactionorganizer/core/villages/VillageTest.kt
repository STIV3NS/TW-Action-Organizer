import io.github.stiv3ns.twactionorganizer.core.villages.StubVillageFactory
import io.kotlintest.data.forall
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec
import io.kotlintest.tables.row

class VillageTest : WordSpec({
    "Village.toString" should {
        "return proper representation" {
            val village = StubVillageFactory.getVillage()
            village.toString() shouldBe "${village.x}|${village.y}"
        }
    }

    "Village.distance" should {
        "return proper squared distance between given villages" {
            forall(
                row(
                    StubVillageFactory.getVillage(500, 500),
                    StubVillageFactory.getVillage(500, 501),
                    1
                ),
                row(
                    StubVillageFactory.getVillage(500, 500),
                    StubVillageFactory.getVillage(500, 510),
                    100
                ),
                row(
                    StubVillageFactory.getVillage(500, 500),
                    StubVillageFactory.getVillage(523, 614),
                    13525
                ),
                row(
                    StubVillageFactory.getVillage(318, 219),
                    StubVillageFactory.getVillage(411, 200),
                    9010
                ),
            ) { v1, v2, squaredDist ->
                v1 distanceTo v2 shouldBe squaredDist
            }
        }
    }
})

