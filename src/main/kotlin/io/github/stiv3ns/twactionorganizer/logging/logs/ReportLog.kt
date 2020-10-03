package io.github.stiv3ns.twactionorganizer.logging.logs

import io.github.stiv3ns.twactionorganizer.core.assigners.AssignerReport

class ReportLog private constructor(val report: AssignerReport) : Log() {
    companion object {
        private val manager = Manager<ReportLog>()

        suspend fun subscribe(callback: (ReportLog)->Unit) {
            manager
                .subChannel
                .send(callback)
        }

        suspend fun trigger(report: AssignerReport) {
            manager
                .logChannel
                .send( ReportLog(report) )
        }
    }
}