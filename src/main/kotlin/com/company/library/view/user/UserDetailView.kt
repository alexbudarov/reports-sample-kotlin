package com.company.library.view.user

import com.company.library.entity.User
import com.company.library.view.main.MainView
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.textfield.PasswordField
import com.vaadin.flow.router.Route
import io.jmix.core.EntityStates
import io.jmix.flowui.Notifications
import io.jmix.flowui.component.textfield.TypedTextField
import io.jmix.flowui.view.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder

import java.util.*

@Route(value = "users/:id", layout = MainView::class)
@ViewController(id = "User.detail")
@ViewDescriptor(path = "user-detail-view.xml")
@EditedEntityContainer("userDc")
open class UserDetailView : StandardDetailView<User>() {

    @ViewComponent
    private lateinit var usernameField: TypedTextField<String>

    @ViewComponent
    private lateinit var passwordField: PasswordField

    @ViewComponent
    private lateinit var confirmPasswordField: PasswordField

    @ViewComponent
    private lateinit var timeZoneField: ComboBox<String>

    @ViewComponent
    private lateinit var messageBundle: MessageBundle

    @Autowired
    private lateinit var notifications: Notifications

    @Autowired
    private lateinit var entityStates: EntityStates

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Subscribe
    fun onInit(event: InitEvent) {
        timeZoneField.setItems(listOf(*TimeZone.getAvailableIDs()))
    }

    @Subscribe
    fun onInitEntity(event: InitEntityEvent<User>) {
        usernameField.isReadOnly = false
        passwordField.isVisible = true
        confirmPasswordField.isVisible = true
    }

    @Subscribe
    fun onReady(event: ReadyEvent) {
        if (entityStates.isNew(editedEntity)) {
            usernameField.focus()
        }
    }

    @Subscribe
    fun onValidation(event: ValidationEvent) {
        if (entityStates.isNew(editedEntity)
                && !Objects.equals(passwordField.value, confirmPasswordField.value)) {
            event.errors.add(messageBundle.getMessage("passwordsDoNotMatch"))
        }
    }

    @Subscribe
    fun onBeforeSave(event: BeforeSaveEvent) {
        if (entityStates.isNew(editedEntity)) {
            editedEntity.password = passwordEncoder.encode(passwordField.value)

            notifications.create(messageBundle.getMessage("noAssignedRolesNotification"))
                .withType(Notifications.Type.WARNING)
                .withPosition(Notification.Position.TOP_END)
                .show()
        }
    }
}