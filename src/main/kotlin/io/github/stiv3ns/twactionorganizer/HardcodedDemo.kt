package io.github.stiv3ns.twactionorganizer

import io.github.stiv3ns.twactionorganizer.core.*
import io.github.stiv3ns.twactionorganizer.core.assigners.AssignerType
import io.github.stiv3ns.twactionorganizer.core.parsers.AllyParserWithDynamicOwnerResolution
import io.github.stiv3ns.twactionorganizer.core.parsers.TargetParser
import io.github.stiv3ns.twactionorganizer.core.PMFormatter
import io.github.stiv3ns.twactionorganizer.core.utils.Serializer
import io.github.stiv3ns.twactionorganizer.core.utils.idInitializer
import io.github.stiv3ns.twactionorganizer.core.villages.TargetVillage
import io.github.stiv3ns.twactionorganizer.localization.PL_PMFormatterLocalization
import io.github.stiv3ns.twactionorganizer.logging.Info
import io.github.stiv3ns.twactionorganizer.logging.LogMessage
import io.github.stiv3ns.twactionorganizer.logging.Logger
import io.github.stiv3ns.twactionorganizer.logging.Report
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.actor
import java.io.File
import java.time.LocalDateTime

@ObsoleteCoroutinesApi
fun main(): Unit = runBlocking {
    val world = World("pl150.plemiona.pl")
    val uow = UnitOfWork()
    uow.setWorld(world)

    ///////////////////////////////////////////////////////////////////////////////////////////

    val allyParser = AllyParserWithDynamicOwnerResolution(world)

    val concreteResources = allyParser.parseAndGetResources(
        File("/home/stivens/rozpiski/tpk11/res_concrete.txt").readText())
    uow.setConcreteResources(concreteResources)

    val fakeResources = allyParser.parseAndGetResources(
        File("/home/stivens/rozpiski/tpk11/res_fake.txt").readText())
    uow.setFakeResources(fakeResources)

    val demolitionResources = allyParser.parseAndGetResources(
        File("/home/stivens/rozpiski/tpk11/res_demolition.txt").readText())
    uow.setDemolitionResources(demolitionResources)

    ///////////////////////////////////////////////////////////////////////////////////////////

    val off = mutableListOf<TargetVillage>()
    TargetParser.parse(
        File("/home/stivens/rozpiski/tpk11/off3.txt").readText(),
        attacksPerVillage = 3, appendTo = off)

    val demolition = mutableListOf<TargetVillage>()
    TargetParser.parse(
        File("/home/stivens/rozpiski/tpk11/off3.txt").readText(),
        attacksPerVillage = 15, appendTo = demolition)

    val fakeA = mutableListOf<TargetVillage>()
    TargetParser.parse(
        File("/home/stivens/rozpiski/tpk11/fakeA40.txt").readText(),
        attacksPerVillage = 40, appendTo = fakeA)

    val fakeB = mutableListOf<TargetVillage>()
    TargetParser.parse(
        File("/home/stivens/rozpiski/tpk11/fakeB10.txt").readText(),
        attacksPerVillage = 10, appendTo = fakeB)

    val fakeC = mutableListOf<TargetVillage>()
    TargetParser.parse(
        File("/home/stivens/rozpiski/tpk11/fakeC10.txt").readText(),
        attacksPerVillage = 10, appendTo = fakeC)

    val fakeD = mutableListOf<TargetVillage>()
    TargetParser.parse(
        File("/home/stivens/rozpiski/tpk11/fakeD16.txt").readText(),
        attacksPerVillage = 16, appendTo = fakeD)

    val fakeE = mutableListOf<TargetVillage>()
    TargetParser.parse(
        File("/home/stivens/rozpiski/tpk11/fakeE13.txt").readText(),
        attacksPerVillage = 13, appendTo = fakeE)

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

    val infoActor = actor<LogMessage> {
        for (msg in channel) {
            when (msg) {
                is Info ->
                    println(msg.text)
                is Report ->
                    println("${msg.assignerReport.name} failed to assign ${msg.assignerReport.numberOfMissingResources} attacks")
                else -> print("")
            }
        }
    }
    Logger.subscribe(Info::class, infoActor)
    Logger.subscribe(Report::class, infoActor)

    Executor(uow, Dispatchers.Default).execute().join()

    idInits.forEach { it.join() }
    infoActor.close()

    Serializer.save(uow.getAllPlayers() as List<Player>, "/home/stivens/rozpiski/tpk11/rozpiska.json.asd")

    File("/home/stivens/rozpiski/tpk11/rozpiska.txt").printWriter().use { pw ->
        val formatter = PMFormatter(
            world,
            dateOfArrival = LocalDateTime.of(2020, 8, 29, 7, 0),
            localization = PL_PMFormatterLocalization
        )

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