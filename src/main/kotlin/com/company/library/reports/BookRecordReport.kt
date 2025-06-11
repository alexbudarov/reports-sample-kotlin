package com.company.library.reports

import com.company.library.entity.Book
import com.company.library.reports.api.Factory
import io.jmix.core.DataManager
import io.jmix.core.FetchPlanBuilder
import io.jmix.core.FetchPlans
import io.jmix.reports.annotation.*
import io.jmix.reports.delegate.FetchPlanProvider
import io.jmix.reports.entity.*
import io.jmix.reports.yarg.loaders.ReportDataLoader
import io.jmix.reports.yarg.structure.BandData
import io.jmix.reports.yarg.structure.ReportQuery
import org.springframework.core.io.ResourceLoader

@ReportDef(
    name = "msg://BookRecordReport.name",
    code = "book-report",
    group = DemoReportGroup::class
)
@InputParameterDef(
    alias = "entity",
    name = "msg://BookRecordReport.param.entity",
    type = ParameterType.ENTITY,
    required = true,
    entity = EntityParameterDef(entityClass = Book::class)
)
@BandDef(
    name = "Root",
    root = true,
    orientation = Orientation.HORIZONTAL,
    dataSets = [DataSetDef(name = "title", type = DataSetType.DELEGATE)]
)
@BandDef(
    name = "Book1",
    parent = "Root",
    orientation = Orientation.HORIZONTAL,
    dataSets = [DataSetDef(
        name = "Book1",
        type = DataSetType.SINGLE,
        entity = EntityDataSetDef(parameterAlias = "entity")
    )]
)
@BandDef(
    name = "Authors2",
    parent = "Root",
    orientation = Orientation.HORIZONTAL,
    dataSets = [DataSetDef(
        name = "Authors2",
        type = DataSetType.MULTI,
        entity = EntityDataSetDef(parameterAlias = "entity", nestedCollectionAttribute = "authors")
    )]
)
@TemplateDef(
    code = "DEFAULT",
    outputType = ReportOutputType.PDF,
    isDefault = true,
    // todo remove when Factory will be supported
    filePath = "com/company/library/reports/new/Template-for-BookRecord.docx",
    outputNamePattern = "\${Root.title}.pdf"
)
class BookRecordReport(
    private val fetchPlans: FetchPlans,
    private val dataManager: DataManager,
    private val resourceLoader: ResourceLoader
) {
    @DataSetDelegate(name = "title")
    fun titleDataLoader(): ReportDataLoader {
        return ReportDataLoader { _: ReportQuery, _: BandData?, parameters: Map<String, Any?> ->
            val book = parameters["entity"] as Book
            listOf(
                mapOf("title" to "Book Record - ${book.name}")
            )
        }
    }

    @DataSetDelegate(name = "Book1")
    fun book1FetchPlan(): FetchPlanProvider {
        return FetchPlanProvider {
            fetchPlans.builder(Book::class.java)
                .add("name")
                .add("summary")
                .add("literatureType") { literatureType: FetchPlanBuilder ->
                    literatureType.add("name")
                }
                .build()
        }
    }

    @DataSetDelegate(name = "Authors2")
    fun authors2FetchPlan(): FetchPlanProvider {
        // !!! we specify fetch plan for Book, not for nested authors
        return FetchPlanProvider {
            fetchPlans.builder(Book::class.java)
                .add("authors") { author: FetchPlanBuilder ->
                    author.add("firstName")
                        .add("lastName")
                }
                .build()
        }
    }

    // example of custom factory method for report template
    // todo uncomment when factory is supported
    //  @TemplateDelegate(code = "DEFAULT")
    fun defaultTemplate(): Factory<ReportTemplate> {
        return Factory {
            val t = dataManager.create(ReportTemplate::class.java)
            val customTemplateFromDb = loadTemplateFileFromDatabase()
            if (customTemplateFromDb.isEmpty()) {
                val file = resourceLoader.getResource("com/company/library/reports/new/Template-for-BookRecord.docx")
                t.content = file.contentAsByteArray
            } else {
                t.content = customTemplateFromDb
            }

            t.outputNamePattern = "\${Root.title}.pdf"
            t
        }
    }

    private fun loadTemplateFileFromDatabase(): ByteArray {
        // todo load from database
        return ByteArray(0)
    }
}