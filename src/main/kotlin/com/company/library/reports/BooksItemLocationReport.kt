package com.company.library.reports

import com.company.library.entity.BookInstance
import io.jmix.core.DataManager
import io.jmix.core.FetchPlans
import io.jmix.core.Messages
import io.jmix.core.querycondition.PropertyCondition
import io.jmix.reports.annotation.*
import io.jmix.reports.delegate.FetchPlanProvider
import io.jmix.reports.delegate.ParameterTransformer
import io.jmix.reports.delegate.ParameterValidator
import io.jmix.reports.entity.DataSetType
import io.jmix.reports.entity.Orientation
import io.jmix.reports.entity.ParameterType
import io.jmix.reports.entity.ReportOutputType
import io.jmix.reports.exception.ReportParametersValidationException
import io.jmix.reports.yarg.structure.DefaultValueProvider

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
        entity = EntityDataSetDef(parameterAlias = "entities")
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

    @InputParameterDelegate(alias = "entities")
    fun validateEntities(): ParameterValidator<Collection<BookInstance>> {
        return ParameterValidator { value: Collection<BookInstance> ->
            if (value.size > 1000) {
                throw ReportParametersValidationException(messages.getMessage("report.booksItemLocation.entities.tooBig"))
            }
        }
    }

    @InputParameterDelegate(alias = "entities")
    fun transformEntities(): ParameterTransformer<Collection<BookInstance>> {
        return ParameterTransformer { value: Collection<BookInstance>, _: Map<String?, Any?>? ->
            value.sortedBy { bi -> bi.inventoryNumber }
        }
    }

    @InputParameterDelegate(alias = "entities")
    fun defaultValueEntities(): DefaultValueProvider<Collection<BookInstance>> {
        return DefaultValueProvider {
            listOf(
                dataManager.load(BookInstance::class.java)
                    .condition(PropertyCondition.create("inventoryNumber", PropertyCondition.Operation.EQUAL, 12584572132L))
                    .one(),
                dataManager.load(BookInstance::class.java)
                    .condition(PropertyCondition.create("inventoryNumber", PropertyCondition.Operation.EQUAL, 32541435134L))
                    .one()
            )
        }
    }

    @DataSetDelegate(name = "BookInstances")
    fun bookInstancesFetchPlan(): FetchPlanProvider {
        return FetchPlanProvider {
            fetchPlans.builder(BookInstance::class.java)
                .add("inventoryNumber")
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