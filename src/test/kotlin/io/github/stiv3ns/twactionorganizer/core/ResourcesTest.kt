package io.github.stiv3ns.twactionorganizer.core

import io.github.stiv3ns.twactionorganizer.core.villages.StubVillageFactory
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe

class ResourcesTest : WordSpec({
    "Resources::empty" should {
        "return Resources instance with empty collections" {
            val res = Resources.empty()

            assertSoftly {
                res.players.shouldBeEmpty()
                res.villages.shouldBeEmpty()
            }
        }
    }

    "Resources::fromVillageCollection" should {
        val village_1 = StubVillageFactory.getVillage(ownerNickname = "TestPlayer1")
        val village_2 = StubVillageFactory.getVillage(ownerNickname = "TestPlayer1")
        val village_3 = StubVillageFactory.getVillage(ownerNickname = "TestPlayer1")
        val village_4 = StubVillageFactory.getVillage(ownerNickname = "Foobar")
        val village_5 = StubVillageFactory.getVillage(ownerNickname = "Ryan")

        val villages = listOf(
            village_1,
            village_2,
            village_3,
            village_4,
            village_5,
        )

        "return proper Resource instance" {
            val result = Resources.fromVillageCollection(villages)

            assertSoftly {
                result.playerCount shouldBe 3
                result.villageCount shouldBe 5
                result.nobleCount shouldBe 0

                result.players.map { player -> player.nickname }
                    .shouldContainExactly("TestPlayer1", "Foobar", "Ryan")

                result.villages.shouldContainExactly(villages)
            }
        }
    }
})