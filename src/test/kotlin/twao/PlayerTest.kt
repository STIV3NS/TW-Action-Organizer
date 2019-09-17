import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec
import twao.Player

class PlayerTest : WordSpec({
    "Player" When {
        "has available noble" should {
            val player = Player("test", 1)

            "return true when asked about it" {
                player.hasNoble() shouldBe true
            }
            "decrease numberOfNobles by 1 when Player.delegateNoble is called" {
                player.delegateNoble()
                player.numberOfNobles shouldBe 0
            }
        }
        "doesnt have available noble" should {
            val player = Player("test", 0)

            "return false when asked about it" {
                player.hasNoble() shouldBe false
            }
        }
    }
})