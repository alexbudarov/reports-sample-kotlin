package com.company.library.view.book

import com.company.library.entity.Book
import com.company.library.view.main.MainView
import com.vaadin.flow.component.ClickEvent
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.router.Route
import io.jmix.core.DataManager
import io.jmix.core.FileStorage
import io.jmix.flowui.Actions
import io.jmix.flowui.Notifications
import io.jmix.flowui.component.grid.DataGrid
import io.jmix.flowui.kit.component.button.JmixButton
import io.jmix.flowui.view.*
import io.jmix.reports.runner.ReportRunner
import io.jmix.reportsflowui.action.RunReportAction
import org.springframework.beans.factory.annotation.Autowired
import java.io.ByteArrayInputStream


@Route(value = "books", layout = MainView::class)
@ViewController(id = "Book.list")
@ViewDescriptor(path = "book-list-view.xml")
@LookupComponent("booksDataGrid")
@DialogMode(width = "64em")
class BookListView : StandardListView<Book>() {
    @ViewComponent
    private lateinit var runBtn: JmixButton
    @Autowired
    private lateinit var actions: Actions
    @ViewComponent
    private lateinit var booksDataGrid: DataGrid<Book>
    @Autowired
    private lateinit var fileStorage: FileStorage
    @Autowired
    private lateinit var dataManager: DataManager
    @Autowired
    private lateinit var reportRunner: ReportRunner
    @Autowired
    private lateinit var notifications: Notifications

    @Subscribe
    private fun onInit(event: InitEvent) {
        val action: RunReportAction<Book> = actions.create(RunReportAction.ID)
        runBtn.action = action
    }

    @Subscribe(id = "generateBtn", subject = "clickListener")
    private fun onGenerateBtnClick(event: ClickEvent<JmixButton>) {
        val book = booksDataGrid.singleSelectedItem ?: return

        val document = reportRunner.byReportCode("book-report")
            .addParam("entity", book)
            .run()

        val reportContent = document.content

        val fileRef = fileStorage.saveStream(document.documentName, ByteArrayInputStream(reportContent))
        book.report = fileRef

        dataManager.save(book)

        notifications.create("Report generated successfully!")
            .withPosition(Notification.Position.TOP_END)
            .withType(Notifications.Type.SUCCESS)
            .show()
    }
}