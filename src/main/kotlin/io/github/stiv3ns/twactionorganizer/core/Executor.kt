package io.github.stiv3ns.twactionorganizer.core

import io.github.stiv3ns.twactionorganizer.core.assigners.AssignerBuilder
import io.github.stiv3ns.twactionorganizer.core.assigners.AssignerReport
import io.github.stiv3ns.twactionorganizer.core.assigners.AssignerType
import io.github.stiv3ns.twactionorganizer.logging.Logger
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

@ObsoleteCoroutinesApi
class Executor(
    val uow: UnitOfWork,
    override val coroutineContext: CoroutineContext
) : CoroutineScope
{
    fun execute(): Job = launch { launchExecutors() }

    private suspend fun launchExecutors() {
        Logger.info("Execution started")

        val jobs = mutableListOf<Job>()

        startFakeRamAssigners(jobs)
        startDemolitionAssigners(jobs)
        startConcreteAssigners(jobs)

        jobs.forEach { it.join() }
        jobs.clear()

        startFakeNobleAssigners(jobs)

        jobs.forEach { it.join() }

        Logger.info("Execution finished")
    }

    private fun startFakeRamAssigners(jobs: MutableList<Job>) {
        uow.getTargetGroups(
            AssignerType.RANDOMIZED_FAKE_RAM,
            AssignerType.FAKE_RAM,
        )
        .forEach { group ->
            jobs += GlobalScope.launch(CoroutineName(group.name)) {
                Logger.info("Assigner for ${group.name} called")
                AssignerBuilder()
                    .mainReferencePoint(group.averagedCoordsAsVillage)
                    .targets(group.villages)
                    .resources(uow.getFakeResourceVillages())
                    .type(group.type)
                    .build()
                    .call()
                    .let { sendReport(it, name = group.name) }
            }
        }
    }

    private suspend fun startDemolitionAssigners(jobs: MutableList<Job>) {
        var sharedResourceVillages = uow.getDemolitionResourceVillages()

        uow.getTargetGroups(
            AssignerType.RANDOMIZED_DEMOLITION,
            AssignerType.DEMOLITION,
        ).forEach { group ->
            val job = GlobalScope.async(CoroutineName(group.name)) {
                Logger.info("Assigner for ${group.name} called")
                AssignerBuilder()
                    .mainReferencePoint(group.averagedCoordsAsVillage)
                    .targets(group.villages)
                    .resources(sharedResourceVillages)
                    .type(group.type)
                    .build()
                    .call()
                    .let { report ->
                        sendReport(report, name = group.name)
                        report.unusedResourceVillages
                    }
            }

            jobs += job
            sharedResourceVillages = job.await()
        }
    }

    private suspend fun startConcreteAssigners(jobs: MutableList<Job>) {
        var sharedResourceVillages = uow.getConcreteResourceVillages()

        uow.getTargetGroups(
            AssignerType.NOBLE,
            AssignerType.REVERSED_RAM,
            AssignerType.RANDOMIZED_RAM,
            AssignerType.RAM,
        ).forEach { group ->
            val job = GlobalScope.async(CoroutineName(group.name)) {
                Logger.info("Assigner for ${group.name} called")
                AssignerBuilder()
                    .mainReferencePoint(group.averagedCoordsAsVillage)
                    .targets(group.villages)
                    .resources(sharedResourceVillages)
                    .type(group.type)
                    .maxNobleRange(uow.getWorld().maxNobleRange)
                    .build()
                    .call()
                    .let { report ->
                        sendReport(report, name = group.name)
                        report.unusedResourceVillages
                    }
            }

            jobs += job
            sharedResourceVillages = job.await()
        }
    }

    private fun startFakeNobleAssigners(jobs: MutableList<Job>) {
        uow.getTargetGroups(
            AssignerType.FAKE_NOBLE
        ).forEach { group ->
            val job = launch {
                Logger.info("Assigner for ${group.name} called")
                AssignerBuilder()
                    .mainReferencePoint(group.averagedCoordsAsVillage)
                    .targets(group.villages)
                    .resources(uow.getFakeResourceVillages())
                    .type(group.type)
                    .maxNobleRange(uow.getWorld().maxNobleRange)
                    .build()
                    .call()
                    .let { sendReport(it, name = group.name) }
            }

            jobs += job
        }
    }

    private suspend fun sendReport(report: AssignerReport, name: String) {
        Logger.report(report.apply { this.name = name })
    }
}