package com.company.library.view.book

import com.company.library.entity.Book
import com.company.library.view.main.MainView
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.html.IFrame
import com.vaadin.flow.router.Route
import io.jmix.core.FileRef
import io.jmix.core.common.util.URLEncodeUtils
import io.jmix.flowui.Actions
import io.jmix.flowui.kit.component.button.JmixButton
import io.jmix.flowui.view.*
import io.jmix.reportsflowui.action.RunSingleEntityReportAction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.web.context.WebApplicationContext

private const val REST_PATH: String = "/rest/files?fileRef="

@Route(value = "books/:id", layout = MainView::class)
@ViewController(id = "Book.detail")
@ViewDescriptor(path = "book-detail-view.xml")
@EditedEntityContainer("bookDc")
class BookDetailView : StandardDetailView<Book>() {
    @Autowired
    private lateinit var actions: Actions
    @ViewComponent
    private lateinit var reportButton: JmixButton
    @Autowired
    private lateinit var applicationContext: ApplicationContext
    @ViewComponent
    private lateinit var displayIFrame: IFrame

    @Subscribe
    private fun onReady(event: ReadyEvent) {
        val book = editedEntity
        reloadIFrame(book.report)
    }

    @Subscribe
    private fun onInitEntity(event: InitEntityEvent<Book>) {
        val action: RunSingleEntityReportAction<Book> = actions.create(RunSingleEntityReportAction.ID)
        action.setReportOutputName(null)
        reportButton.action = action
    }

    fun reloadIFrame(document: FileRef?) {
        if (document != null) {
            val page = UI.getCurrent().page

            page.fetchCurrentURL { url ->
                run {
                    val contextPath = (applicationContext as WebApplicationContext).servletContext!!.contextPath
                    val documentSrc = url.protocol + "://" + url.authority + contextPath +
                            REST_PATH + URLEncodeUtils.encodeUtf8(document.toString())
                    displayIFrame.src = documentSrc
                }
            }
        }
    }
}