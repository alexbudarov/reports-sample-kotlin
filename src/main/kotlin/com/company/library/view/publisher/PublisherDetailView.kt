package com.company.library.view.publisher

import com.company.library.entity.Publisher
import com.company.library.view.main.MainView
import com.vaadin.flow.router.Route
import io.jmix.flowui.view.EditedEntityContainer
import io.jmix.flowui.view.StandardDetailView
import io.jmix.flowui.view.ViewController
import io.jmix.flowui.view.ViewDescriptor

@Route(value = "publishers/:id", layout = MainView::class)
@ViewController(id = "Publisher.detail")
@ViewDescriptor(path = "publisher-detail-view.xml")
@EditedEntityContainer("publisherDc")
class PublisherDetailView : StandardDetailView<Publisher>() {
}