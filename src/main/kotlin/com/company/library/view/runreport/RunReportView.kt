package com.company.library.view.runreport


import com.company.library.entity.BookPublication
import com.company.library.entity.LiteratureType
import com.company.library.view.main.MainView
import com.vaadin.flow.component.ClickEvent
import com.vaadin.flow.router.Route
import io.jmix.core.DataManager
import io.jmix.flowui.download.DownloadFormat
import io.jmix.flowui.download.Downloader
import io.jmix.flowui.kit.component.button.JmixButton
import io.jmix.flowui.view.StandardView
import io.jmix.flowui.view.Subscribe
import io.jmix.flowui.view.ViewController
import io.jmix.flowui.view.ViewDescriptor
import io.jmix.localfs.LocalFileStorageProperties
import io.jmix.reports.ReportImportExport
import io.jmix.reports.entity.Report
import io.jmix.reports.entity.ReportOutputType
import io.jmix.reports.entity.ReportTemplate
import io.jmix.reports.exception.ReportingException
import io.jmix.reports.runner.ReportRunContext
import io.jmix.reports.runner.ReportRunner
import io.jmix.reports.util.ReportZipUtils
import io.jmix.reports.yarg.reporting.ReportOutputDocument
import io.jmix.reportsflowui.runner.ParametersDialogShowMode
import io.jmix.reportsflowui.runner.UiReportRunContext
import io.jmix.reportsflowui.runner.UiReportRunner
import org.apache.commons.io.FileUtils
import org.springframework.beans.factory.annotation.Autowired
import java.io.File
import java.io.IOException

@Route(value = "run-report-view", layout = MainView::class)
@ViewController(id = "RunReportView")
@ViewDescriptor(path = "run-report.xml")
class RunReportView : StandardView() {
    @Autowired
    private lateinit var dataManager: DataManager
    @Autowired
    private lateinit var reportRunner: ReportRunner
    @Autowired
    private lateinit var reportZipUtils: ReportZipUtils
    @Autowired
    private lateinit var downloader: Downloader
    @Autowired
    private lateinit var uiReportRunner: UiReportRunner
    @Autowired
    private lateinit var localFileStorageProperties: LocalFileStorageProperties
    @Autowired
    private lateinit var reportImportExport: ReportImportExport

    private fun getReportByCode(code: String): Report {
        return dataManager.load(Report::class.java)
            .query("select r from report_Report r where r.code = :code1")
            .parameter("code1", code)
            .one()
    }

    @Subscribe(id = "rrcBtn1", subject = "clickListener")
    private fun onRrcBtn1Click(event: ClickEvent<JmixButton>) {
        val report = getReportByCode("BOOKS")
        val type = dataManager.load(LiteratureType::class.java)
            .query("select l from LiteratureType l where l.name = :name1")
            .parameter("name1", "Art")
            .one()
        val context = ReportRunContext(report)
            .addParam("type", type)
            .setOutputNamePattern("Books")
        val document = reportRunner.run(context)
        val documents: MutableList<ReportOutputDocument> = ArrayList()
        documents.add(document)
        val zipArchiveContent = reportZipUtils.createZipArchive(documents)
        downloader.download(zipArchiveContent, "Reports.zip", DownloadFormat.ZIP)
    }

    @Subscribe(id = "rrcBtn2", subject = "clickListener")
    private fun onRrcBtn2Click(event: ClickEvent<JmixButton>) {
        val report = getReportByCode("BOOKS")
        val type = dataManager.load(LiteratureType::class.java)
            .query("select c from LiteratureType c where c.name = :name")
            .parameter("name", "Art")
            .one()

        val paramsMap: MutableMap<String, Any> = HashMap()
        paramsMap["type"] = type
        val template = dataManager.load(ReportTemplate::class.java)
            .query("select c from report_ReportTemplate c where c.code = :code and c.report = :report")
            .parameter("code", "DEFAULT")
            .parameter("report", report)
            .one()

        val context = ReportRunContext(report)
            .setReportTemplate(template)
            .setOutputType(ReportOutputType.PDF)
            .setParams(paramsMap)

        val document = reportRunner
            .run(ReportRunContext(report).setParams(paramsMap))

    }

    @Subscribe(id = "rrBtn1", subject = "clickListener")
    private fun onRrBtn1Click(event: ClickEvent<JmixButton>) {
        val type = dataManager.load(LiteratureType::class.java)
            .query("select c from LiteratureType c where c.name = :name")
            .parameter("name", "Art")
            .one()
        val document = reportRunner.byReportCode("BOOKS")
            .addParam("type", type)
            .withTemplateCode("books-template")
            .run()
    }

    @Subscribe(id = "rrBtn2", subject = "clickListener")
    private fun onRrBtn2Click(event: ClickEvent<JmixButton>) {
        val report = getReportByCode("BOOKS")
        val type = dataManager.load(LiteratureType::class.java)
            .query("select c from LiteratureType c where c.name = :name")
            .parameter("name", "Art")
            .one()
        val document = reportRunner.byReportEntity(report)
            .addParam("type", type)
            .withOutputType(ReportOutputType.PDF)
            .withOutputNamePattern("Books")
            .run()
        val documentName = document.documentName

        val content = document.content
    }

    @Subscribe(id = "urrBtn1", subject = "clickListener")
    private fun onUrrBtn1Click(event: ClickEvent<JmixButton>) {
        val report = getReportByCode("BOOK_COUNT")
        uiReportRunner.runAndShow(UiReportRunContext(report))
    }

    @Subscribe(id = "urrBtn2", subject = "clickListener")
    private fun onUrrBtn2Click(event: ClickEvent<JmixButton>) {
        val report = getReportByCode("BOOK_COUNT")
        uiReportRunner.byReportEntity(report)
            .withParametersDialogShowMode(ParametersDialogShowMode.IF_REQUIRED)
            .inBackground(this@RunReportView)
            .runAndShow()
    }

    @Subscribe(id = "urrBtn3", subject = "clickListener")
    private fun onUrrBtn3Click(event: ClickEvent<JmixButton>) {
        val report = getReportByCode("PUBL")
        val publicationList = dataManager.load(BookPublication::class.java).all().list()
        
        uiReportRunner.byReportEntity(report)
            .withOutputType(ReportOutputType.PDF)
            .withTemplateCode("publication-template")
            .withParametersDialogShowMode(ParametersDialogShowMode.NO)
            .runMultipleReports("entity", publicationList)
    }

    @Subscribe(id = "importBtn", subject = "clickListener")
    private fun onImportBtnClick(event: ClickEvent<JmixButton>) {
        val tempFile = File(
            localFileStorageProperties
                .storageDir + "Book count.zip"
        )
        val bytes: ByteArray
        try {
            bytes = FileUtils.readFileToByteArray(tempFile)
        } catch (e: IOException) {
            throw ReportingException(String.format("Error while importing"), e)
        }
        reportImportExport.importReportsWithResult(bytes, null)
    }
}