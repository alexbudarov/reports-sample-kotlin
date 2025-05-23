package com.company.library.view.librarydepartment

import com.company.library.entity.LibraryDepartment
import com.company.library.view.main.MainView
import com.vaadin.flow.router.Route
import io.jmix.flowui.view.EditedEntityContainer
import io.jmix.flowui.view.StandardDetailView
import io.jmix.flowui.view.ViewController
import io.jmix.flowui.view.ViewDescriptor

@Route(value = "library-departments/:id", layout = MainView::class)
@ViewController(id = "LibraryDepartment.detail")
@ViewDescriptor(path = "library-department-detail-view.xml")
@EditedEntityContainer("libraryDepartmentDc")
class LibraryDepartmentDetailView : StandardDetailView<LibraryDepartment>() {
}