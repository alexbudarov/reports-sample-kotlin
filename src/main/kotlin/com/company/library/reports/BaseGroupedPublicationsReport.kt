package com.company.library.reports

import io.jmix.core.TimeSource
import io.jmix.core.security.CurrentAuthentication
import io.jmix.reports.annotation.BandDef
import io.jmix.reports.annotation.DataSetDef
import io.jmix.reports.annotation.DataSetDelegate
import io.jmix.reports.entity.DataSetType
import io.jmix.reports.entity.Orientation
import io.jmix.reports.yarg.loaders.ReportDataLoader
import io.jmix.reports.yarg.structure.BandData
import io.jmix.reports.yarg.structure.ReportQuery

@BandDef(
    name = "header",
    parent = "Root",
    orientation = Orientation.HORIZONTAL,
    dataSets = [DataSetDef(name = "header", type = DataSetType.DELEGATE)]
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
                    select distinct b.literatureType.id as typeId,
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
abstract class BaseGroupedPublicationsReport(
    private val currentAuthentication: CurrentAuthentication,
    private val timeSource: TimeSource
): ReportWithRootBand {

    @DataSetDelegate(name = "header")
    fun headerImplementation(): ReportDataLoader {
        return ReportDataLoader { _: ReportQuery, _: BandData?, _: Map<String, Any?> ->
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

}