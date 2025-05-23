package com.company.library.view.literaturetype

import com.company.library.entity.LiteratureType
import com.company.library.view.main.MainView
import com.vaadin.flow.router.Route
import io.jmix.flowui.view.*

@Route(value = "literature-types/:id", layout = MainView::class)
@ViewController(id = "LiteratureType.detail")
@ViewDescriptor(path = "literature-type-detail-view.xml")
@EditedEntityContainer("literatureTypeDc")
class LiteratureTypeDetailView : StandardDetailView<LiteratureType>() {
}