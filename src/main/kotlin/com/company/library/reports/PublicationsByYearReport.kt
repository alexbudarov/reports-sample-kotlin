package com.company.library.reports

import io.jmix.reports.annotation.*
import io.jmix.reports.entity.*
import io.jmix.reports.yarg.formatters.CustomReport
import io.jmix.reports.yarg.structure.BandData
import io.jmix.reports.yarg.structure.Report
import org.dom4j.Document
import org.dom4j.DocumentHelper
import org.dom4j.Element
import org.dom4j.io.OutputFormat
import org.dom4j.io.XMLWriter
import java.io.ByteArrayOutputStream
import java.io.IOException

@ReportDef(
    name = "Publications by year",
    code = "BY_YEAR_STATS_ANN",
    group = DemoReportGroup::class,
    description = "Example with custom template",
    uuid = "01970c9e-6236-79c1-8297-bd04a2d9a517"
)
@InputParameterDef(
    alias = "startYear",
    name = "Start Year",
    type = ParameterType.NUMERIC,
    required = true,
    defaultValue = "2000"
)
@BandDef(name = "Root", root = true, orientation = Orientation.HORIZONTAL)
@BandDef(name = "header", parent = "Root", orientation = Orientation.HORIZONTAL)
@BandDef(
    name = "stats", parent = "Root", orientation = Orientation.HORIZONTAL, dataSets = [DataSetDef(
        type = DataSetType.SQL, query = """
                    select YEAR_ as PUB_YEAR, count(*) as PUB_COUNT
                    from BOOK_PUBLICATION
                    where YEAR_ >= ${'$'}{startYear}
                    group by YEAR_
                    order by YEAR_ DESC
"""
    )]
)
@TemplateDef(
    code = "XML",
    outputType = ReportOutputType.CUSTOM,
    isDefault = true,
    outputNamePattern = "Stats from \${Root.startYear}.xml",
    custom = CustomTemplateParameters(enabled = true, definedBy = CustomTemplateDefinedBy.DELEGATE)
)

class PublicationsByYearReport {
    @TemplateDelegate(code = "XML")
    fun customTemplate(): CustomReport {
        return CustomReport { _: Report, rootBand: BandData, _: Map<String, Any?> -> renderXml(rootBand) }
    }

    private fun renderXml(rootBand: BandData): ByteArray {
        val document = DocumentHelper.createDocument()
        document.addElement(rootBand.name)
        renderBandData(document.rootElement, rootBand.data)

        for (bandData in rootBand.childrenList) {
            renderSubBand(document.rootElement, bandData)
        }

        val byteContent = convertToByteArray(document)
        return byteContent
    }

    private fun convertToByteArray(document: Document): ByteArray {
        val baos = ByteArrayOutputStream()
        val format = OutputFormat.createPrettyPrint()
        val writer: XMLWriter
        try {
            writer = XMLWriter(baos, format)
            writer.write(document)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
        return baos.toByteArray()
    }

    private fun renderSubBand(rootElement: Element, bandData: BandData) {
        if (bandData.data.isEmpty() && bandData.childrenBands.isEmpty()) {
            return
        }
        val bandElement = rootElement.addElement(bandData.name)
        renderBandData(bandElement, bandData.data)

        for (subSubBand in bandData.childrenList) {
            renderSubBand(bandElement, subSubBand)
        }
    }

    private fun renderBandData(bandElement: Element, data: Map<String, Any>) {
        for ((key, value) in data) {
            val formattedValue = value.toString()
            bandElement.addAttribute(key, formattedValue)
        }
    }
}