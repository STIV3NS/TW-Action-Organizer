import io.kotlintest.matchers.collections.shouldBeEmpty
import io.kotlintest.matchers.collections.shouldContainExactly
import io.kotlintest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec
import twao.Player
import twao.VillageAssignment
import twao.villages.Village

class PlayerTest : WordSpec({
    "Player [nobles]" When {
        "has available noble" should {
            val player = Player("test", _numberOfNobles = 1)

            "return true when asked about it" {
                player.hasNoble() shouldBe true
            }
            "decrease numberOfNobles by 1 when Player.delegateNoble is called" {
                player.delegateNoble()

                player.numberOfNobles shouldBe 0
            }
        }
        "doesnt have available noble" should {
            val player = Player("test")

            "return false when asked about it" {
                player.hasNoble() shouldBe false
            }
        }
    }

    "Player [villages]" should {
        val player = Player("test")

        "be initialized with 0 villages" {
            player.numberOfVillages shouldBe 0
        }
        "::increaseNumberOfVillages increment this value" {
            player.increaseNumberOfVillages()
            player.increaseNumberOfVillages()

            player.numberOfVillages shouldBe 2
        }
        "::decreaseNumberOfVillages decrement this value" {
            player.decreaseNumberOfVillaes()

            player.numberOfVillages shouldBe 1
        }
    }

    "Player [assigments]" should {
        val player = Player("test")
        val assignment_1 = VillageAssignment(Village(500, 500), Village(300, 300), 100)
        val assignment_2 = VillageAssignment(Village(400, 400), Village(400, 400), 200)
        val assignment_3 = VillageAssignment(Village(700, 700), Village(100, 100), 2500)
        val assignment_4 = VillageAssignment(Village(400, 700), Village(100, 300), 2700)

        "be initialized as empty lists" {
            with(player) {
                offAssignmentsCopy.shouldBeEmpty()
                fakeAssignmentsCopy.shouldBeEmpty()
                nobleAssignmentsCopy.shouldBeEmpty()
                fakeNobleAssignmentsCopy.shouldBeEmpty()
            }
        }
        "put off-assigment to offAssigments" {
            player.putOffAssignment(assignment_1)

            player.offAssignmentsCopy.shouldContainExactly(assignment_1)
        }
        "put fake-assigment to fakeAssigments" {
            player.putFakeAssignment(assignment_2)

            player.fakeAssignmentsCopy.shouldContainExactly(assignment_2)
        }
        "put noble-assigment to nobleAssigments" {
            player.putNobleAssignment(assignment_3)

            player.nobleAssignmentsCopy.shouldContainExactly(assignment_3)
        }
        "put fake-noble-assigment to fakeNobleAssigments" {
            player.putFakeNobleAssignment(assignment_4)

            player.fakeNobleAssignmentsCopy.shouldContainExactly(assignment_4)
        }
        "properly put multiple assigments" {
            val new_player = Player("test")
            with(new_player) {
                putOffAssignment(assignment_1)
                putOffAssignment(assignment_2)
                putOffAssignment(assignment_3)
                putOffAssignment(assignment_4)
            }

            new_player.offAssignmentsCopy.shouldContainExactly(assignment_1, assignment_2, assignment_3, assignment_4)
        }
    }
})