package com.company.library.view.bookinstance

import com.company.library.entity.BookInstance
import com.company.library.view.main.MainView
import com.vaadin.flow.router.Route
import io.jmix.flowui.view.*


@Route(value = "book-instances", layout = MainView::class)
@ViewController(id = "BookInstance.list")
@ViewDescriptor(path = "book-instance-list-view.xml")
@LookupComponent("bookInstancesDataGrid")
@DialogMode(width = "64em")
class BookInstanceListView : StandardListView<BookInstance>() {
}