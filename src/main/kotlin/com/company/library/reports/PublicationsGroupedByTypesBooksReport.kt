package com.company.library.reports

import com.company.library.reports.annotation.*
import com.company.library.reports.api.DataSetDataLoader
import com.company.library.reports.api.ValueFormatter
import io.jmix.core.TimeSource
import io.jmix.core.security.CurrentAuthentication
import io.jmix.reports.entity.DataSetType
import io.jmix.reports.entity.Orientation
import io.jmix.reports.entity.ReportOutputType
import io.jmix.reports.yarg.structure.BandData
import java.util.*

@ReportDef(
    name = "Publications grouped by types and books",
    code = "PUBLICATIONS_GROUPED",
    group = DemoReportGroup::class
)
@BandDef(
    name = "Root",
    root = true,
    orientation = Orientation.HORIZONTAL
)
@BandDef(
    name = "header",
    parent = "Root",
    orientation = Orientation.HORIZONTAL,
    dataSets = [DataSetDef(name = "header", type = DataSetType.GROOVY)]
)
@BandDef(
    name = "tableheader",
    parent = "Root",
    orientation = Orientation.HORIZONTAL
)
@BandDef(
    name = "type", parent = "Root", orientation = Orientation.HORIZONTAL, dataSets = [DataSetDef(
        type = DataSetType.JPQL,
        query = """
                    select b.literatureType.id as typeId,
                    b.literatureType.name as type
                    from Book b                    
                    """
    )]
)
@BandDef(
    name = "book", parent = "type", orientation = Orientation.HORIZONTAL, dataSets = [DataSetDef(
        type = DataSetType.JPQL,
        query = """
                    select b.id as bookId,
                    b.name as bookName
                    from Book b
                    where b.literatureType.id = ${'$'}{type.typeId}                    
                    """
    )]
)
@BandDef(
    name = "publisher", parent = "book", orientation = Orientation.HORIZONTAL, dataSets = [DataSetDef(
        type = DataSetType.JPQL,
        query = """
                    select bp.publisher.name as publisher,
                    bp.year as year,
                    bp.city as town
                    from BookPublication bp
                    where bp.book.id = ${'$'}{book.bookId} 
                    """
    )]
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
    private val currentAuthentication: CurrentAuthentication,
    private val timeSource: TimeSource
) {

    @RelatesTo(dataSet = "header")
    fun headerImplementation(): DataSetDataLoader {
        return DataSetDataLoader { _: Map<String, Any?>, _: BandData? ->
            val user = currentAuthentication.user.username
            val currentDate = timeSource.currentTimestamp()
            listOf(
                mapOf(
                    "generated_by" to user,
                    "generated_when" to currentDate
                )
            )
        }
    }

    @RelatesTo(valueFormat = "header.generated_by")
    fun headerGeneratedByValueFormat(): ValueFormatter<String> {
        return ValueFormatter { value: String? -> value?.uppercase(Locale.getDefault()) }
    }
}