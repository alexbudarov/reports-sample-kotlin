package com.company.library.view.author

import com.company.library.entity.Author
import com.company.library.view.main.MainView
import com.vaadin.flow.router.Route
import io.jmix.flowui.Actions
import io.jmix.flowui.kit.component.button.JmixButton
import io.jmix.flowui.view.*
import io.jmix.reportsflowui.action.RunListEntityReportAction
import org.springframework.beans.factory.annotation.Autowired


@Route(value = "authors", layout = MainView::class)
@ViewController(id = "Author.list")
@ViewDescriptor(path = "author-list-view.xml")
@LookupComponent("authorsDataGrid")
@DialogMode(width = "64em")
class AuthorListView : StandardListView<Author>() {
    @ViewComponent
    private lateinit var runListBtn: JmixButton

    @Autowired
    private lateinit var actions: Actions

    @Subscribe
    private fun onInit(event: InitEvent) {
        val action: RunListEntityReportAction<Author> = actions.create(RunListEntityReportAction.ID)
        runListBtn.action = action
    }
}