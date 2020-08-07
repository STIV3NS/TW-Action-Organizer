package io.github.stiv3ns.twactionorganizer

import io.github.stiv3ns.twactionorganizer.core.TWAOExecutor
import io.github.stiv3ns.twactionorganizer.core.TWAOUnitOfWork
import io.github.stiv3ns.twactionorganizer.core.TargetGroup
import io.github.stiv3ns.twactionorganizer.core.World
import io.github.stiv3ns.twactionorganizer.core.assigners.AssignerType
import io.github.stiv3ns.twactionorganizer.core.parsers.CSVAllyParser
import io.github.stiv3ns.twactionorganizer.core.parsers.TargetParser
import io.github.stiv3ns.twactionorganizer.core.villages.TargetVillage
import io.github.stiv3ns.twactionorganizer.core.utils.PMFormatter
import java.time.LocalDateTime

fun main() {
    val world = World("pl150.plemiona.pl")
    val uow = TWAOUnitOfWork()

    val allyParser = CSVAllyParser().apply {
        csvFilePath = "/home/stivens/rozpiski/tpk7/allies.csv"
        nicknameHeader = "nick"
        villagesHeader = "village"
        villageRegexLiteral = "\\d{3}\\|\\d{3}"
    }

    val resources = allyParser.parseAndGetResources()
    resources.villages.forEach { it.initID(world) }

    val fakeA = mutableListOf<TargetVillage>()
    val fakeB = mutableListOf<TargetVillage>()

    TargetParser.parse("/home/stivens/rozpiski/tpk7/fakeA5.txt", 5, fakeA)
    TargetParser.parse("/home/stivens/rozpiski/tpk7/fakeA6.txt", 6, fakeA)
    TargetParser.parse("/home/stivens/rozpiski/tpk7/fakeA7.txt", 7, fakeA)

    TargetParser.parse("/home/stivens/rozpiski/tpk7/fakeB3.txt", 3, fakeB)

    fakeA.forEach { it.initID(world) }
    fakeB.forEach { it.initID(world) }

    uow.setWorld(world)
    uow.setConcreteResources(resources)

    uow.addTargetGroup(TargetGroup("Fake A", AssignerType.FAKE_RAM, fakeA))
    uow.addTargetGroup(TargetGroup("Fake B", AssignerType.FAKE_RAM, fakeB))

    val reports = TWAOExecutor.execute(uow)
    TWAOExecutor.shutdownNow()

    val formatter = PMFormatter(
        world,
        dateOfArrival = LocalDateTime.of(2020, 7, 27, 7, 0, 0, 0)
    )

    for (player in resources.players) {
        if (player.fakeAssignments.isNotEmpty()) {
            println(player.nickname)

            println(formatter.getFormattedMsgFor(player))

            println("============================================")
            println("============================================\n\n\n")
        }
    }
}