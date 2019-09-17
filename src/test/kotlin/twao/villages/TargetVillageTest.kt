import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec
import twao.villages.TargetVillage

class TargetVillageTest : WordSpec({
    "TargetVillage" When {
        "it needs more attacks" should {
            val target = TargetVillage(500, 500, 1)

            "return false when asked if assigning is completed" {
                target.isAssignCompleted() shouldBe false
            }
            "decrease numberOfAttacks when it was attacked" {
                target.attack()

                target.numberOfAttacks shouldBe 0
            }
        }
        "it doesnt need more attacks" should {
            val target = TargetVillage(500, 500, 0)

            "return true when asked if assigning is completed" {
                target.isAssignCompleted() shouldBe true
            }
        }
    }
})