package com.company.library.user

import com.company.library.ReportsSampleKotlinApplication
import com.company.library.entity.User
import com.company.library.view.user.UserDetailView
import com.company.library.view.user.UserListView
import io.jmix.core.DataManager
import io.jmix.flowui.ViewNavigators
import io.jmix.flowui.component.grid.DataGrid
import io.jmix.flowui.component.textfield.JmixPasswordField
import io.jmix.flowui.component.textfield.TypedTextField
import io.jmix.flowui.kit.component.button.JmixButton
import io.jmix.flowui.testassist.FlowuiTestAssistConfiguration
import io.jmix.flowui.testassist.UiTest
import io.jmix.flowui.testassist.UiTestUtils
import io.jmix.flowui.view.View
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * Sample UI integration test for the User entity.
 */
@UiTest
@SpringBootTest(classes = [ReportsSampleKotlinApplication::class, FlowuiTestAssistConfiguration::class])
class UserUiTest {

    @Autowired
    lateinit var dataManager: DataManager

    @Autowired
    lateinit var viewNavigators: ViewNavigators

    @Test
    fun test_createUser() {
        // Navigate to user list view
        viewNavigators.view(UiTestUtils.getCurrentView(), UserListView::class.java)
                .navigate()

        var userListView = UiTestUtils.getCurrentView<UserListView>()

        // click "Create" button
        val createBtn = UiTestUtils.getComponent<JmixButton>(userListView, "createButton")
        createBtn.click()

        // Get detail view
        val userDetailView = UiTestUtils.getCurrentView<UserDetailView>()

        // Set username and password in the fields
        val usernameField = UiTestUtils.getComponent<TypedTextField<String>>(userDetailView, "usernameField")
        val username = "test-user-" + System.currentTimeMillis()
        usernameField.value = username

        val passwordField = UiTestUtils.getComponent<JmixPasswordField>(userDetailView, "passwordField")
        passwordField.value = "test-passwd"

        val confirmPasswordField = UiTestUtils.getComponent<JmixPasswordField>(userDetailView, "confirmPasswordField")
        confirmPasswordField.value = "test-passwd"

        // Click "OK"
        val commitAndCloseBtn = UiTestUtils.getComponent<JmixButton>(userDetailView, "saveAndCloseButton")
        commitAndCloseBtn.click()

        // Get navigated user list view
        userListView = UiTestUtils.getCurrentView()

        // Check the created user is shown in the table
        val usersDataGrid = UiTestUtils.getComponent<DataGrid<User>>(userListView, "usersDataGrid")

        val usersDataGridItems = usersDataGrid.items
        val user = usersDataGridItems!!.items
                .find { u: User -> u.getUsername() == username }

        Assertions.assertNotNull(user)
    }

    @AfterEach
    fun tearDown() {
        dataManager.load(User::class.java)
                .query("e.username like ?1", "test-user-%")
                .list()
                .forEach { u: User? -> dataManager.remove(u) }
    }
}