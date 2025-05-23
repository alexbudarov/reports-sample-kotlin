package com.company.library.view.bookpublication

import com.company.library.entity.BookPublication
import com.company.library.view.main.MainView
import com.vaadin.flow.router.Route
import io.jmix.flowui.view.EditedEntityContainer
import io.jmix.flowui.view.StandardDetailView
import io.jmix.flowui.view.ViewController
import io.jmix.flowui.view.ViewDescriptor

@Route(value = "book-publications/:id", layout = MainView::class)
@ViewController(id = "BookPublication.detail")
@ViewDescriptor(path = "book-publication-detail-view.xml")
@EditedEntityContainer("bookPublicationDc")
class BookPublicationDetailView : StandardDetailView<BookPublication>() {
}