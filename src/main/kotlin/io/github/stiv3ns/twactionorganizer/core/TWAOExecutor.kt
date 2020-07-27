package io.github.stiv3ns.twactionorganizer.core

import io.github.stiv3ns.twactionorganizer.core.assigners.AssignerBuilder
import io.github.stiv3ns.twactionorganizer.core.assigners.AssignerReport
import io.github.stiv3ns.twactionorganizer.core.assigners.AssignerType
import io.github.stiv3ns.twactionorganizer.core.villages.AllyVillage
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

object TWAOExecutor {
    private val executorService: ExecutorService

    private val runningTasks = mutableMapOf< TargetGroup, Future<AssignerReport> >()

    private var sharedResourceVillages = mutableListOf<AllyVillage>()

    init {
        val numberOfCores = Runtime.getRuntime().availableProcessors()
        executorService = Executors.newFixedThreadPool(numberOfCores)
    }

    fun execute(uow: TWAOUnitOfWork): List<AssignerReport> {
        sharedResourceVillages = uow.getConcreteResourceVillages().toMutableList()

        startFakeRamAssigners(uow)
        startConcreteAssigners(uow)

        return collectReports()
    }

    private fun startFakeRamAssigners(uow: TWAOUnitOfWork) {
        uow.getTargetGroups(AssignerType.FAKE_RAM).forEach { group ->
            val task = executorService.submit(
                AssignerBuilder()
                    .mainReferencePoint(group.averagedCoordsAsVillage)
                    .targets(group.villageList.toMutableList())
                    .resources(uow.getConcreteResourceVillages().apply{
                        addAll(uow.getAdditionalResourceVillages())
                    })
                    .type(group.type)
                .build()
            )

            runningTasks[group] = task
        }
    }

    private fun startConcreteAssigners(uow: TWAOUnitOfWork) {
        val groups = uow.getTargetGroups(
                AssignerType.NOBLE
        ).toMutableList().apply {
            addAll(uow.getTargetGroups(AssignerType.REVERSED_RAM))
            addAll(uow.getTargetGroups(AssignerType.RAM))
        }

        groups.forEach { group ->
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

    private fun collectReports(): List<AssignerReport> {
        val completed = mutableListOf<AssignerReport>()

        runningTasks.forEach { group, task ->
            completed.add(task.get().apply {
                name = group.name
            })
        }

        runningTasks.clear()

        return completed
    }

    fun shutdownNow() {
        cancelAllAssigners()
        executorService.shutdownNow()
        executorService.awaitTermination( 5, TimeUnit.SECONDS )
    }

    fun cancelAllAssigners() {
        runningTasks.forEach { group, task ->
            task.cancel(true)
        }

        runningTasks.clear()
    }
}