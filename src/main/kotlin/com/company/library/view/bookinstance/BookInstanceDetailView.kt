package com.company.library.view.bookinstance

import com.company.library.entity.BookInstance
import com.company.library.view.main.MainView
import com.vaadin.flow.router.Route
import io.jmix.flowui.view.EditedEntityContainer
import io.jmix.flowui.view.StandardDetailView
import io.jmix.flowui.view.ViewController
import io.jmix.flowui.view.ViewDescriptor

@Route(value = "book-instances/:id", layout = MainView::class)
@ViewController(id = "BookInstance.detail")
@ViewDescriptor(path = "book-instance-detail-view.xml")
@EditedEntityContainer("bookInstanceDc")
class BookInstanceDetailView : StandardDetailView<BookInstance>() {
}