import io.github.stiv3ns.twactionorganizer.core.villages.Village
import io.kotlintest.*
import io.kotlintest.data.forall
import io.kotlintest.specs.WordSpec
import io.kotlintest.tables.row

class VillageTest : WordSpec({
    "Village.toString" should {
        "return proper representation" {
            Village(314, 200).toString() shouldBe "314|200"
        }
    }


    "Village.distance" should {
        "return proper squared distance between given villages" {
            forall (
                row(Village(500, 500), Village(500, 501), 1),
                row(Village(500, 500), Village(500, 510), 100),
                row(Village(500, 500), Village(523, 614), 13525),
                row(Village(318, 219), Village(411, 200), 9010)
            ) { v1, v2, squaredDist ->
                v1 distanceTo v2 shouldBe squaredDist
            }
        }
    }
})

