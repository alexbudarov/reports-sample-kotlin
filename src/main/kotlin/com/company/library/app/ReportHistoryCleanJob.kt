package com.company.library.app

import io.jmix.core.security.Authenticated
import io.jmix.reports.ReportExecutionHistoryRecorder
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.springframework.stereotype.Component

@Component("ReportHistoryCleaner")
open class ReportHistoryCleanJob(
    private val reportExecutionHistoryRecorder: ReportExecutionHistoryRecorder
) : Job {

    @Authenticated
    override fun execute(context: JobExecutionContext?) {
        reportExecutionHistoryRecorder.cleanupHistory()
    }
}