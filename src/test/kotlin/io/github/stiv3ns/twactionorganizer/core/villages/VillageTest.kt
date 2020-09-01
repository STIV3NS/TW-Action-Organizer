package io.github.stiv3ns.twactionorganizer.core.villages

import io.kotest.core.spec.style.WordSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe

class VillageTest : WordSpec({
    "Village.toString" should {
        "return proper representation" {
            val village = StubVillageFactory.getVillage()
            village.toString() shouldBe "${village.x}|${village.y}"
        }
    }

    "Village.distance" should {
        "return proper squared distance between given villages" {
            forAll(
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

