import io.github.stiv3ns.twactionorganizer.core.villages.StubVillageFactory
import io.github.stiv3ns.twactionorganizer.core.villages.TargetVillage
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

class TargetVillageTest : WordSpec({
    "TargetVillage" When {
        "it needs more attacks" should {
            val target = TargetVillage(StubVillageFactory.getVillage(), numberOfAttacks = 5)

            "return false when asked if assigning is completed" {
                target.isAssignCompleted() shouldBe false
            }

            "decrease numberOfAttacks when it was attacked" {
                target.attack()
                target.numberOfAttacks shouldBe 4

                target.attack()
                target.numberOfAttacks shouldBe 3
            }
        }

        "it doesnt need more attacks" should {
            val target = TargetVillage(StubVillageFactory.getVillage(), numberOfAttacks = 0)

            "return true when asked if assigning is completed" {
                target.isAssignCompleted() shouldBe true
            }
        }
    }
})