package com.company.library.view.author

import com.company.library.entity.Author
import com.company.library.view.main.MainView
import com.vaadin.flow.router.Route
import io.jmix.flowui.view.EditedEntityContainer
import io.jmix.flowui.view.StandardDetailView
import io.jmix.flowui.view.ViewController
import io.jmix.flowui.view.ViewDescriptor

@Route(value = "authors/:id", layout = MainView::class)
@ViewController(id = "Author.detail")
@ViewDescriptor(path = "author-detail-view.xml")
@EditedEntityContainer("authorDc")
class AuthorDetailView : StandardDetailView<Author>() {
}