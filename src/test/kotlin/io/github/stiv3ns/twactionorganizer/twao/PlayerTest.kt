import io.kotlintest.matchers.collections.shouldBeEmpty
import io.kotlintest.matchers.collections.shouldContainExactly
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec
import io.github.stiv3ns.twactionorganizer.twao.Player
import io.github.stiv3ns.twactionorganizer.twao.VillageAssignment
import io.github.stiv3ns.twactionorganizer.twao.villages.Village

class PlayerTest : WordSpec({
    "Player [nobles]" When {
        "has available noble" should {
            val player = Player("test", _numberOfNobles = 63)

            "return true when asked about it" {
                player.hasNoble() shouldBe true
            }

            "decrease numberOfNobles by 1 when Player.delegateNoble is called" {
                player.delegateNoble()
                player.numberOfNobles shouldBe 62

                player.delegateNoble()
                player.numberOfNobles shouldBe 61
            }
        }


        "doesnt have available noble" should {
            val player = Player("test", _numberOfNobles = 0)

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

        "::registerVillage increments ::numberOfVillages" {
            player.registerVillage()
            player.registerVillage()
            player.numberOfVillages shouldBe 2

            player.registerVillage()
            player.numberOfVillages shouldBe 3
        }

        "::unregisterVillage decrements ::numberOfVillages" {
            player.unregisterVillage()
            player.numberOfVillages shouldBe 2

            player.unregisterVillage()
            player.numberOfVillages shouldBe 1
        }
    }


    "Player [assigments]" should {
        val player = Player("test")
        val assignment_1 = VillageAssignment(Village(1,1), Village(2,2),2)
        val assignment_2 = VillageAssignment(Village(3,3), Village(4,4),2)
        val assignment_3 = VillageAssignment(Village(5,5), Village(6,6),2)
        val assignment_4 = VillageAssignment(Village(7,7), Village(8,8),2)

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