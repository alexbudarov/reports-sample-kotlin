package com.company.library.reports

import com.company.library.reports.annotation.*
import com.company.library.reports.api.ErrorConsumer
import com.company.library.reports.api.ParameterTransformer
import com.company.library.reports.api.ParametersCrossValidator
import io.jmix.reports.entity.DataSetType
import io.jmix.reports.entity.Orientation
import io.jmix.reports.entity.ParameterType
import io.jmix.reports.entity.ReportOutputType
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.ZoneOffset
import java.util.*

@ReportDef(
    name = "Recently added book items",
    code = "RECENTLY_ADDED_BOOK_ITEMS",
    group = DemoReportGroup::class
)
@InputParameterDef(
    alias = "createDt",
    name = "Create After",
    type = ParameterType.DATETIME,
    parameterClassName = Date::class,
    required = true
)
@BandDef(
    name = "Root",
    root = true,
    orientation = Orientation.HORIZONTAL
)
@BandDef(
    name = "headerBookInstances",
    parent = "Root",
    orientation = Orientation.HORIZONTAL
)
@BandDef(
    name = "BookInstances",
    parent = "Root",
    orientation = Orientation.HORIZONTAL,
    dataSets = [DataSetDef(
        type = DataSetType.JPQL,
        query = """
                    select bookPublication_book.name as "bookPublication.book.name",
                    libraryDepartment.name as "libraryDepartment.name"
                    from BookInstance e
                    left join e.bookPublication.book bookPublication_book
                    left join e.libraryDepartment libraryDepartment
                    where e.createdDate >= ${'$'}{createDt}
                    """
    )]
)
@TemplateDef(
    code = "DEFAULT",
    outputType = ReportOutputType.XLSX,
    filePath = "com/company/library/reports/new/RecentlyAddedBookItems.xlsx",
    isDefault = true,
    outputNamePattern = "Recently added book items.xlsx"
)

class RecentlyAddedBookItemsReport {

    @RelatesTo(inputParameter = "createDt")
    fun createDtTransform(): ParameterTransformer<Date> {
        return ParameterTransformer { value: Date, _: Map<String, Any?> ->
            value.toInstant().atOffset(ZoneOffset.UTC)
        }
    }

    // cross validation example.
    // Do we need any annotation at all here? maybe @RelatesTo(report = true) ?
    @Throws(ParseException::class)
    fun crossValidator(): ParametersCrossValidator {
        val border = SimpleDateFormat("dd.MM.yyyy").parse("01.01.1990")
        return ParametersCrossValidator { parameterValues: Map<String, Any?>, errorConsumer: ErrorConsumer ->
            val createDt = parameterValues["createDt"] as Date
            if (createDt.before(border)) {
                errorConsumer.addError("Date is too early")
            }
        }
    }
}