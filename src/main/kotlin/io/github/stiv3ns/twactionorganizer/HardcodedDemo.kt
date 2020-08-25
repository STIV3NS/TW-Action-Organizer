package io.github.stiv3ns.twactionorganizer

import io.github.stiv3ns.twactionorganizer.core.*
import io.github.stiv3ns.twactionorganizer.core.assigners.AssignerType
import io.github.stiv3ns.twactionorganizer.core.parsers.AllyParserWithDynamicOwnerResolution
import io.github.stiv3ns.twactionorganizer.core.parsers.TargetParser
import io.github.stiv3ns.twactionorganizer.core.utils.PMFormatter
import io.github.stiv3ns.twactionorganizer.core.utils.Serializer
import io.github.stiv3ns.twactionorganizer.core.utils.idInitializer
import io.github.stiv3ns.twactionorganizer.core.villages.TargetVillage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.io.File
import java.time.LocalDateTime

fun main() = runBlocking {
    val world = World("pl150.plemiona.pl")
    val uow = UnitOfWork()
    uow.setWorld(world)

    ///////////////////////////////////////////////////////////////////////////////////////////

    val allyParser = AllyParserWithDynamicOwnerResolution(world)
        .apply {
            txtFilePath = "/home/stivens/rozpiski/tpk11/res_concrete.txt"
        }

    val concreteResources = allyParser.parseAndGetResources()
    uow.setConcreteResources(concreteResources)

    with(allyParser) {
        txtFilePath = "/home/stivens/rozpiski/tpk11/res_fake.txt"
        knownPlayers = uow.getAllPlayers().associateBy { it.nickname }.toMutableMap()
    }

    val fakeResources = allyParser.parseAndGetResources()
    uow.setFakeResources(fakeResources)

    with(allyParser) {
        txtFilePath = "/home/stivens/rozpiski/tpk11/res_demolition.txt"
        knownPlayers = uow.getAllPlayers().associateBy { it.nickname }.toMutableMap()
    }

    val demolitionResources = allyParser.parseAndGetResources()
    uow.setDemolitionResources(demolitionResources)

    ///////////////////////////////////////////////////////////////////////////////////////////

    val off = mutableListOf<TargetVillage>()
    TargetParser.parse("/home/stivens/rozpiski/tpk11/off3.txt", 3, off)

    val demolition = mutableListOf<TargetVillage>()
    TargetParser.parse("/home/stivens/rozpiski/tpk11/off3.txt", 15, demolition)

    val fakeA = mutableListOf<TargetVillage>()
    TargetParser.parse("/home/stivens/rozpiski/tpk11/fakeA40.txt", 40, fakeA)

    val fakeB = mutableListOf<TargetVillage>()
    TargetParser.parse("/home/stivens/rozpiski/tpk11/fakeB10.txt", 10, fakeB)

    val fakeC = mutableListOf<TargetVillage>()
    TargetParser.parse("/home/stivens/rozpiski/tpk11/fakeC10.txt", 10, fakeC)

    val fakeD = mutableListOf<TargetVillage>()
    TargetParser.parse("/home/stivens/rozpiski/tpk11/fakeD16.txt", 16, fakeD)

    val fakeE = mutableListOf<TargetVillage>()
    TargetParser.parse("/home/stivens/rozpiski/tpk11/fakeE13.txt", 13, fakeE)

    ///////////////////////////////////////////////////////////////////////////////////////////

    val idInits = setOf(
        concreteResources.villages,
        fakeResources.villages,
        demolitionResources.villages,
        off,
        fakeA,
        fakeB,
        fakeC,
        fakeD,
        fakeE,
    ).map { villages -> idInitializer(villages, world) }

    ///////////////////////////////////////////////////////////////////////////////////////////

    uow.addTargetGroup(TargetGroup(name = "OFF x3", type = AssignerType.RANDOMIZED_RAM, villages = off))
    uow.addTargetGroup(
        run {
            val MINUTES_PER_HOUR = 60L
            TargetGroup(name = "Demolition x15", type = AssignerType.RANDOMIZED_DEMOLITION, villages = demolition)
                .withDelayInMinutes(6 * MINUTES_PER_HOUR)
        }
    )
    uow.addTargetGroup(TargetGroup(name = "Fake A", type = AssignerType.RANDOMIZED_FAKE_RAM, villages = fakeA))
    uow.addTargetGroup(TargetGroup(name = "Fake B", type = AssignerType.RANDOMIZED_FAKE_RAM, villages = fakeB))
    uow.addTargetGroup(TargetGroup(name = "Fake C", type = AssignerType.RANDOMIZED_FAKE_RAM, villages = fakeC))
    uow.addTargetGroup(TargetGroup(name = "Fake D", type = AssignerType.RANDOMIZED_FAKE_RAM, villages = fakeD))
    uow.addTargetGroup(TargetGroup(name = "Fake E", type = AssignerType.RANDOMIZED_FAKE_RAM, villages = fakeE))

    ///////////////////////////////////////////////////////////////////////////////////////////

    for (report in Executor(uow, Dispatchers.Default).execute()) {
        println("""
            ${report.name} failed to assign ${report.numberOfUnassignedTargets} targets / ${report.numberOfMissingResources} attacks.
        """.trimIndent())
    }

    idInits.forEach { it.join() }

    Serializer.save(uow.getAllPlayers() as List<Player>, "/home/stivens/rozpiski/tpk11/rozpiska.json")

    File("/home/stivens/rozpiski/tpk11/rozpiska.txt").printWriter().use { pw ->
        val formatter = PMFormatter(world, dateOfArrival = LocalDateTime.of(2020, 8, 29, 7, 0))

        uow.getAllPlayers().forEach { player ->
            pw.println(
                """${player.nickname}
                |Cześć, oto Twoja rozpiska.
                |Ataki mają wchodzić na 29 sierpnia. Obok rozkazu jest podany czas T0 pierwszego możliwego terminu wysyłki.
                |
                |Kilka uwag:
                |1. Offy wysyłamy razem z katapultami (!patrz. podpunkt 2). Tym razem jeszcze bardziej staramy
                |   się w miarę możliwości dosyłać ataki regularnie co 2/3/4 godziny. No ale tak jak zawsze off wysłany
                |   na wieczór to lepszy off niż taki nie wysłany w ogóle.
                |2. Katapultami celujemy na zmianę w zagrodę/ratusz/hute żelaza.
                |3. Do fejków fajnie jest dorzucić ze dwie katapulty wycelowane w pałac (dwie wystarczą żeby go zburzyć).
                |4. Przy wysyłaniu ataków pamiętaj o zostawieniu wojska na w sumie 5 fejków.
                |5. Przy wysyłaniu ataków zawsze upewniaj sie, czy atak wchodzi na dobrą datę (a nie np. dzień wcześniej).
                |6. Jeśli na koncie jest zastępca to linki dla niego można wygenerować za pomocą tego:
                |    https://pl150.plemiona.pl/game.php?screen=forum&screenmode=view_thread&forum_id=617&thread_id=44897
                |
                |
                |${formatter.getFormattedMsgFor(player)}
                |
                |Ave (T)PK!
                |==============================================================================
                |==============================================================================
                """.trimMargin()
            )
        }
    }
}