package com.company.library.view.user

import com.company.library.entity.User
import com.company.library.view.main.MainView
import com.vaadin.flow.router.Route
import io.jmix.flowui.view.*

@Route(value = "users", layout = MainView::class)
@ViewController(id = "User.list")
@ViewDescriptor(path = "user-list-view.xml")
@LookupComponent("usersDataGrid")
@DialogMode(width = "64em")
open class UserListView : StandardListView<User>()
