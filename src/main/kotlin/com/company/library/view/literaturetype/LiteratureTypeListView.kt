package com.company.library.view.literaturetype

import com.company.library.entity.LiteratureType
import com.company.library.view.main.MainView
import com.vaadin.flow.router.Route
import io.jmix.flowui.Actions
import io.jmix.flowui.kit.component.button.JmixButton
import io.jmix.flowui.view.*
import io.jmix.reportsflowui.action.ShowExecutionReportHistoryAction
import org.springframework.beans.factory.annotation.Autowired


@Route(value = "literature-types", layout = MainView::class)
@ViewController(id = "LiteratureType.list")
@ViewDescriptor(path = "literature-type-list-view.xml")
@LookupComponent("literatureTypesDataGrid")
@DialogMode(width = "64em")
class LiteratureTypeListView : StandardListView<LiteratureType>() {
    @ViewComponent
    private lateinit var historyBtn: JmixButton

    @Autowired
    private lateinit var actions: Actions

    @Subscribe
    private fun onInit(event: InitEvent) {
        val action: ShowExecutionReportHistoryAction<LiteratureType> = actions.create(ShowExecutionReportHistoryAction.ID)
        historyBtn.action = action
    }
}