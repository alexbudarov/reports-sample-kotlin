package com.company.library.reports.old

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

/**
 * Sample abstract XML renderer for runtime Reports.
 */
@Suppress("unused")
class XmlReportRenderer: CustomReport {

    override fun createReport(report: Report, rootBand: BandData, params: MutableMap<String, Any>): ByteArray {
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