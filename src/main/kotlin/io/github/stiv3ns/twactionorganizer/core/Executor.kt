package io.github.stiv3ns.twactionorganizer.core

import io.github.stiv3ns.twactionorganizer.core.assigners.AssignerBuilder
import io.github.stiv3ns.twactionorganizer.core.assigners.AssignerReport
import io.github.stiv3ns.twactionorganizer.core.assigners.AssignerType
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

object Executor {
    private val executorService: ExecutorService
    private val runningTasks = mutableMapOf<TargetGroup, Future<AssignerReport>>()

    init {
        val nCores = Runtime.getRuntime().availableProcessors()
        executorService = Executors.newFixedThreadPool(nCores)
    }

    fun execute(uow: UnitOfWork): List<AssignerReport> {
        startFakeRamAssigners(uow)
        startConcreteAssigners(uow)
        startFakeNobleAssigners(uow)
        startDemolitionAssigners(uow)

        return collectReports()
    }

    private fun startFakeRamAssigners(uow: UnitOfWork) {
            uow.getTargetGroups(
                AssignerType.RANDOMIZED_FAKE_RAM,
                AssignerType.FAKE_RAM,
            ).forEach { group ->
            val task = executorService.submit(
                AssignerBuilder()
                    .mainReferencePoint(group.averagedCoordsAsVillage)
                    .targets(group.villageList.toMutableList())
                    .resources(uow.getFakeResourceVillages().toMutableList())
                    .type(group.type)
                    .build()
            )

            runningTasks[group] = task
        }
    }

    private fun startDemolitionAssigners(uow: UnitOfWork) {
        val sharedResourceVillages = uow.getDemolitionResourceVillages()

        uow.getTargetGroups(
            AssignerType.RANDOMIZED_DEMOLITION,
            AssignerType.DEMOLITION,
        ).forEach { group ->
            val task = executorService.submit(
                AssignerBuilder()
                    .mainReferencePoint(group.averagedCoordsAsVillage)
                    .targets(group.villageList.toMutableList())
                    .resources(sharedResourceVillages)
                    .type(group.type)
                    .build()
            )

            runningTasks[group] = task

            task.get() /* <--- wait for completion ! */
        }
    }

    private fun startConcreteAssigners(uow: UnitOfWork) {
        val sharedResourceVillages = uow.getConcreteResourceVillages()

        uow.getTargetGroups(
            AssignerType.NOBLE,
            AssignerType.REVERSED_RAM,
            AssignerType.RANDOMIZED_RAM,
            AssignerType.RAM,
        ).forEach { group ->
            val task = executorService.submit(
                AssignerBuilder()
                    .mainReferencePoint(group.averagedCoordsAsVillage)
                    .targets(group.villageList.toMutableList())
                    .resources(sharedResourceVillages)
                    .type(group.type)
                    .maxNobleRange(uow.getWorld().maxNobleRange)
                    .build()
            )

            runningTasks[group] = task

            task.get() /* <--- wait for completion ! */
        }
    }

    private fun startFakeNobleAssigners(uow: UnitOfWork) {
        uow.getTargetGroups(
            AssignerType.FAKE_NOBLE
        ).forEach { group ->
            val task = executorService.submit(
                AssignerBuilder()
                    .mainReferencePoint(group.averagedCoordsAsVillage)
                    .targets(group.villageList.toMutableList())
                    .resources(uow.getFakeResourceVillages().toMutableList())
                    .type(group.type)
                    .maxNobleRange(uow.getWorld().maxNobleRange)
                    .build()
            )

            runningTasks[group] = task

            task.get() /* <--- wait for completion ! */
        }
    }

    private fun collectReports(): List<AssignerReport> {
        val completedTasks = mutableListOf<AssignerReport>()

        runningTasks.forEach { group, task ->
            completedTasks.add(task.get().apply {
                name = group.name
            })
        }

        runningTasks.clear()

        return completedTasks
    }

    fun shutdownNow() {
        cancelAllAssigners()
        executorService.shutdownNow()
        executorService.awaitTermination(5, TimeUnit.SECONDS)
    }

    fun cancelAllAssigners() {
        runningTasks.forEach { group, task ->
            task.cancel(true)
        }

        runningTasks.clear()
    }
}