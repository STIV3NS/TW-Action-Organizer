package io.github.stiv3ns.twactionorganizer.core

import io.github.stiv3ns.twactionorganizer.core.assigners.AssignerType
import io.github.stiv3ns.twactionorganizer.core.villages.StubVillageFactory
import io.github.stiv3ns.twactionorganizer.core.villages.TargetVillage
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldNotBeSameInstanceAs

class TargetGroupTest : WordSpec({
    "TargetGroup" When {
        val targetGroup = TargetGroup(
            name = "",
            type = AssignerType.RAM,
            villages = listOf(
                TargetVillage(StubVillageFactory.getVillage(500, 600), numberOfAttacks = 3),
                TargetVillage(StubVillageFactory.getVillage(600, 600), numberOfAttacks = 3),
                TargetVillage(StubVillageFactory.getVillage(600, 800), numberOfAttacks = 3),
                TargetVillage(StubVillageFactory.getVillage(500, 800), numberOfAttacks = 3),
            )
        )

        "invoked ::averagedCoordsAsVillage" should {
            "return proper Village instance" {
                val result = targetGroup.averagedCoordsAsVillage
                assertSoftly {
                    result.x shouldBe 550
                    result.y shouldBe 700
                }
            }
        }

        "invoked ::villageCount" should {
            "return proper number of villages" {
                targetGroup.villageCount shouldBe 4
            }
        }

        "invoked ::totalAttackCount" should {
            "return proper number of required attacks" {
                targetGroup.totalAttackCount shouldBe 12
            }
        }

        "invoked ::withDelayInMinutes" should {
            val delay = 60L
            val delayed = targetGroup.withDelayInMinutes(delay)

            "return deep copy of itself with proper delay" {
                delayed shouldNotBeSameInstanceAs targetGroup
                delayed.villages shouldNotBeSameInstanceAs targetGroup.villages

                delayed.villages.forEach { village ->
                    village.delayInMinutes shouldBe delay
                }

                targetGroup.villages.forEach { village ->
                    village.delayInMinutes shouldBe 0L
                }
            }
        }
    }
})