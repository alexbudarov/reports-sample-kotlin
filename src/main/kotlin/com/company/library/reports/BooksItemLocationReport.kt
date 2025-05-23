package com.company.library.reports

import com.company.library.entity.BookInstance
import com.company.library.reports.annotation.*
import com.company.library.reports.api.*
import io.jmix.core.DataManager
import io.jmix.core.FetchPlans
import io.jmix.core.Messages
import io.jmix.core.querycondition.PropertyCondition
import io.jmix.reports.entity.DataSetType
import io.jmix.reports.entity.Orientation
import io.jmix.reports.entity.ParameterType
import io.jmix.reports.entity.ReportOutputType

@ReportDef(
    name = "Book Items location",
    code = "BOOK_ITEMS_LOCATION",
    group = DemoReportGroup::class
)
@InputParameterDef(
    alias = "entities",
    name = "Entities",
    type = ParameterType.ENTITY_LIST,
    required = true,
    entity = EntityParameterDef(entityClass = BookInstance::class)
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
        name = "BookInstances",
        type = DataSetType.MULTI,
        entity = EntityDataSetDef(listParameterAlias = "entities")
    )]
)
@TemplateDef(
    code = "default",
    outputType = ReportOutputType.XLSX,
    filePath = "com/company/library/reports/new/BookItemsLocation.xlsx",
    isDefault = true,
    outputNamePattern = "Book Items location.xlsx"
)
class BooksItemLocationReport(private val messages: Messages, private val dataManager: DataManager,
                              private val fetchPlans: FetchPlans) {

    @RelatesTo(inputParameter = "entities")
    fun validateEntities(): ParameterValidator<List<BookInstance>> {
        return ParameterValidator { value: List<BookInstance>, errorConsumer: ErrorConsumer ->
            if (value.size > 1000) {
                errorConsumer.addError(messages.getMessage("report.booksItemLocation.entities.tooBig"))
            }
        }
    }

    @RelatesTo(inputParameter = "entities")
    fun transformEntities(): ParameterTransformer<List<BookInstance>> {
        return ParameterTransformer { value: List<BookInstance>, params: Map<String?, Any?>? ->
            value.sortedBy { bi -> bi.inventoryNumber }
        }
    }

    @RelatesTo(inputParameter = "entities")
    fun defaultValueEntities(): DefaultValueProvider<List<BookInstance>> {
        return DefaultValueProvider {
            listOf(
                dataManager.load(BookInstance::class.java)
                    .condition(PropertyCondition.create("inventoryNumber", PropertyCondition.Operation.EQUAL, 155L))
                    .one(),
                dataManager.load(BookInstance::class.java)
                    .condition(PropertyCondition.create("inventoryNumber", PropertyCondition.Operation.EQUAL, 487L))
                    .one()
            )
        }
    }

    @RelatesTo(dataSet = "BookInstances")
    fun bookInstancesFetchPlan(): FetchPlanProvider {
        return FetchPlanProvider {
            fetchPlans.builder(BookInstance::class.java)
                .add("bookPublication") { publication ->
                    publication.add("book") { book ->
                        book.add("name")
                    }
                }
                .add("libraryDepartment") { department ->
                    department.add("name")
                }
                .build()
        }
    }
}