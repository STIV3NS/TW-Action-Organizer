import io.github.stiv3ns.twactionorganizer.core.villages.TargetVillage
import io.kotlintest.*
import io.kotlintest.data.forall
import io.kotlintest.specs.WordSpec
import io.kotlintest.tables.row

class VillageTest : WordSpec({
    "Village.toString" should {
        "return proper representation" {
            TargetVillage(314, 200, 0).toString() shouldBe "314|200"
        }
    }


    "Village.distance" should {
        "return proper squared distance between given villages" {
            forall (
                row(TargetVillage(500, 500, 0), TargetVillage(500, 501, 0), 1),
                row(TargetVillage(500, 500, 0), TargetVillage(500, 510, 0), 100),
                row(TargetVillage(500, 500, 0), TargetVillage(523, 614, 0), 13525),
                row(TargetVillage(318, 219, 0), TargetVillage(411, 200, 0), 9010)
            ) { v1, v2, squaredDist ->
                v1 distanceTo v2 shouldBe squaredDist
            }
        }
    }
})

