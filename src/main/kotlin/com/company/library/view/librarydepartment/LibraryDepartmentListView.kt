package com.company.library.view.librarydepartment

import com.company.library.entity.LibraryDepartment
import com.company.library.view.main.MainView
import com.vaadin.flow.router.Route
import io.jmix.flowui.view.*


@Route(value = "library-departments", layout = MainView::class)
@ViewController(id = "LibraryDepartment.list")
@ViewDescriptor(path = "library-department-list-view.xml")
@LookupComponent("libraryDepartmentsDataGrid")
@DialogMode(width = "64em")
class LibraryDepartmentListView : StandardListView<LibraryDepartment>() {
}