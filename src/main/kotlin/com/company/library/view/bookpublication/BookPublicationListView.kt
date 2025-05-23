package com.company.library.view.bookpublication

import com.company.library.entity.BookPublication
import com.company.library.view.main.MainView
import com.vaadin.flow.router.Route
import io.jmix.flowui.view.*


@Route(value = "book-publications", layout = MainView::class)
@ViewController(id = "BookPublication.list")
@ViewDescriptor(path = "book-publication-list-view.xml")
@LookupComponent("bookPublicationsDataGrid")
@DialogMode(width = "64em")
class BookPublicationListView : StandardListView<BookPublication>() {
}