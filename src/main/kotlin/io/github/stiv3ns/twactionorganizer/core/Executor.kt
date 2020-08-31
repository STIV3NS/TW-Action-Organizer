package io.github.stiv3ns.twactionorganizer.core

import io.github.stiv3ns.twactionorganizer.core.assigners.AssignerBuilder
import io.github.stiv3ns.twactionorganizer.core.assigners.AssignerReport
import io.github.stiv3ns.twactionorganizer.core.assigners.AssignerType
import io.github.stiv3ns.twactionorganizer.logging.Logger
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

typealias DeferredAssignments = Deferred<List<Assignment>>
typealias DeferredReport = Deferred<AssignerReport>

@ObsoleteCoroutinesApi
class Executor(
    val uow: UnitOfWork,
    override val coroutineContext: CoroutineContext = Dispatchers.Default
) : CoroutineScope
{
    fun executeAsync(): DeferredAssignments =
        async { launchExecutors() }

    private suspend fun launchExecutors(): List<Assignment> {
        Logger.info("Execution started")

        val result = mutableListOf<Assignment>()
        val jobs = mutableListOf<DeferredReport>()

        startFakeRamAssigners(jobs)
        startDemolitionAssigners(jobs)
        startConcreteAssigners(jobs)

        jobs.forEach {
            val report = it.await()
            result.addAll(report.result)
        }

        jobs.clear()

        startFakeNobleAssigners(jobs)

        jobs.forEach {
            val report = it.await()
            result.addAll(report.result)
        }

        Logger.info("Execution finished")

        return result
    }

    private fun startFakeRamAssigners(jobs: MutableList<DeferredReport>) {
        uow.getTargetGroups(
            AssignerType.RANDOMIZED_FAKE_RAM,
            AssignerType.FAKE_RAM,
        )
        .forEach { group ->
            jobs += GlobalScope.async(CoroutineName(group.name)) {
                logAssignerCalled(group.name)

                AssignerBuilder()
                    .name(group.name)
                    .mainReferencePoint(group.averagedCoordsAsVillage)
                    .targets(group.villages)
                    .resources(uow.getFakeResources())
                    .type(group.type)
                    .build()
                    .call()
                    .also { report -> logReport(report) }
            }
        }
    }

    private suspend fun startDemolitionAssigners(jobs: MutableList<DeferredReport>) {
        var sharedResourceVillages = uow.getDemolitionResources()

        uow.getTargetGroups(
            AssignerType.RANDOMIZED_DEMOLITION,
            AssignerType.DEMOLITION,
        ).forEach { group ->
            val job = GlobalScope.async(CoroutineName(group.name)) {
                logAssignerCalled(group.name)

                AssignerBuilder()
                    .name(group.name)
                    .mainReferencePoint(group.averagedCoordsAsVillage)
                    .targets(group.villages)
                    .resources(sharedResourceVillages)
                    .type(group.type)
                    .build()
                    .call()
                    .also { report -> logReport(report) }
            }

            jobs.add(job)
            sharedResourceVillages = job.await().unusedResources
        }
    }

    private suspend fun startConcreteAssigners(jobs: MutableList<DeferredReport>) {
        var sharedResourceVillages = uow.getConcreteResources()

        uow.getTargetGroups(
            AssignerType.NOBLE,
            AssignerType.REVERSED_RAM,
            AssignerType.RANDOMIZED_RAM,
            AssignerType.RAM,
        ).forEach { group ->
            val job = GlobalScope.async(CoroutineName(group.name)) {
                logAssignerCalled(group.name)

                AssignerBuilder()
                    .name(group.name)
                    .mainReferencePoint(group.averagedCoordsAsVillage)
                    .targets(group.villages)
                    .resources(sharedResourceVillages)
                    .type(group.type)
                    .maxNobleRange(uow.getWorld().maxNobleRange)
                    .build()
                    .call()
                    .also { report -> logReport(report) }
            }

            jobs.add(job)
            sharedResourceVillages = job.await().unusedResources
        }
    }

    private fun startFakeNobleAssigners(jobs: MutableList<DeferredReport>) {
        uow.getTargetGroups(
            AssignerType.FAKE_NOBLE
        ).forEach { group ->
            val job = async {
                logAssignerCalled(group.name)

                AssignerBuilder()
                    .name(group.name)
                    .mainReferencePoint(group.averagedCoordsAsVillage)
                    .targets(group.villages)
                    .resources(uow.getFakeResources())
                    .type(group.type)
                    .maxNobleRange(uow.getWorld().maxNobleRange)
                    .build()
                    .call()
                    .also { report -> logReport(report) }
            }

            jobs.add(job)
        }
    }

    private suspend fun logAssignerCalled(name: String) {
        Logger.info("Assigner for $name called")
    }

    private suspend fun logReport(report: AssignerReport) {
        Logger.report(report)
    }
}