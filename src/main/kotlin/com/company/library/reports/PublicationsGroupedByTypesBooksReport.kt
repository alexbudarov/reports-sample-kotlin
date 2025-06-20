package com.company.library.reports

import io.jmix.core.TimeSource
import io.jmix.core.security.CurrentAuthentication
import io.jmix.reports.annotation.ReportDef
import io.jmix.reports.annotation.TemplateDef
import io.jmix.reports.annotation.ValueFormatDef
import io.jmix.reports.annotation.ValueFormatDelegate
import io.jmix.reports.entity.ReportOutputType
import io.jmix.reports.yarg.structure.CustomValueFormatter
import java.util.*

@ReportDef(
    name = "Publications grouped by types and books",
    code = "PUBLICATIONS_GROUPED",
    group = DemoReportGroup::class
)
@TemplateDef(
    code = "DEFAULT",
    outputType = ReportOutputType.XLSX,
    filePath = "com/company/library/reports/new/Template for publications by type.xlsx",
    isDefault = true,
    outputNamePattern = "Publications grouped by types and books"
)
@ValueFormatDef(
    band = "header",
    field = "generated_when",
    format = "dd.MM.yyyy"
)
@ValueFormatDef(
    band = "header",
    field = "generated_by"
)
class PublicationsGroupedByTypesBooksReport(
    currentAuthentication: CurrentAuthentication,
    timeSource: TimeSource
): BaseGroupedPublicationsReport(currentAuthentication, timeSource) {

    @ValueFormatDelegate(band = "header", field = "generated_by")
    fun headerGeneratedByValueFormat(): CustomValueFormatter<String> {
        return CustomValueFormatter { value: String? -> value?.uppercase(Locale.getDefault()) }
    }
}