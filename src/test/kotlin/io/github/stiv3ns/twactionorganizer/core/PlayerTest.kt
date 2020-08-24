import io.github.stiv3ns.twactionorganizer.core.Player
import io.github.stiv3ns.twactionorganizer.core.Assignment
import io.github.stiv3ns.twactionorganizer.core.villages.AllyVillage
import io.github.stiv3ns.twactionorganizer.core.villages.TargetVillage
import io.kotlintest.matchers.collections.shouldBeEmpty
import io.kotlintest.matchers.collections.shouldContainExactly
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

class PlayerTest : WordSpec({
    "Player [nobles]" When {
        "has available noble" should {
            val player = Player("test", numberOfNobles = 63)

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
            val player = Player("test", numberOfNobles = 0)

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
        val assignment_1 = Assignment(
            departure = AllyVillage(1, 1, player),
            destination = TargetVillage(2, 2, 0),
            squaredDistance = 2
        )
        val assignment_2 = Assignment(
            departure = AllyVillage(3, 3, player),
            destination = TargetVillage(4, 4, 0),
            squaredDistance = 2
        )
        val assignment_3 = Assignment(
            departure = AllyVillage(5, 5, player),
            destination = TargetVillage(6, 6, 0),
            squaredDistance = 2
        )
        val assignment_4 = Assignment(
            departure = AllyVillage(7, 7, player),
            destination = TargetVillage(8, 8, 0),
            squaredDistance = 2
        )

        "be initialized as empty lists" {
            with(player) {
                offAssignments.shouldBeEmpty()
                fakeAssignments.shouldBeEmpty()
                nobleAssignments.shouldBeEmpty()
                fakeNobleAssignments.shouldBeEmpty()
            }
        }

        "put off-assigment to offAssignments" {
            player.putOffAssignment(assignment_1)

            player.offAssignments.shouldContainExactly(assignment_1)
        }

        "put fake-assigment to fakeAssignments" {
            player.putFakeAssignment(assignment_2)

            player.fakeAssignments.shouldContainExactly(assignment_2)
        }

        "put noble-assigment to nobleAssignments" {
            player.putNobleAssignment(assignment_3)

            player.nobleAssignments.shouldContainExactly(assignment_3)
        }

        "put fake-noble-assigment to fakeNobleAssignments" {
            player.putFakeNobleAssignment(assignment_4)

            player.fakeNobleAssignments.shouldContainExactly(assignment_4)
        }

        "properly put multiple assignments" {
            val newPlayer = Player("test").apply {
                putOffAssignment(assignment_1)
                putOffAssignment(assignment_2)
                putOffAssignment(assignment_3)
                putOffAssignment(assignment_4)
            }

            newPlayer.offAssignments.shouldContainExactly(assignment_1, assignment_2, assignment_3, assignment_4)
        }
    }
})