package com.company.library.app

import io.jmix.reports.ReportExecutionHistoryRecorder
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.springframework.stereotype.Component

@Component("ReportHistoryCleaner")
class ReportHistoryCleanJob(
    private val reportExecutionHistoryRecorder: ReportExecutionHistoryRecorder
) : Job {

    override fun execute(context: JobExecutionContext?) {
        reportExecutionHistoryRecorder.cleanupHistory()
    }
}