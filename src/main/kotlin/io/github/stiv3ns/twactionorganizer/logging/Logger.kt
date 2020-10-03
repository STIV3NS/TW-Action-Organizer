package io.github.stiv3ns.twactionorganizer.logging

import io.github.stiv3ns.twactionorganizer.core.assigners.AssignerReport
import io.github.stiv3ns.twactionorganizer.logging.logs.ErrorLog
import io.github.stiv3ns.twactionorganizer.logging.logs.InfoLog
import io.github.stiv3ns.twactionorganizer.logging.logs.ReportLog
import io.github.stiv3ns.twactionorganizer.logging.logs.WarnLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

object Logger : CoroutineScope {
    override val coroutineContext = Dispatchers.Default

    suspend fun info(text: String) {
        InfoLog.trigger(text)
    }

    suspend fun warn(text: String) {
        WarnLog.trigger(text)
    }

    suspend fun error(text: String) {
        ErrorLog.trigger(text)
    }

    suspend fun report(assignerReport: AssignerReport) {
        ReportLog.trigger(assignerReport)
    }
}