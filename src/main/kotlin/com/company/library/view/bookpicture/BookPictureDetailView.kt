package com.company.library.view.bookpicture

import com.company.library.entity.BookPicture
import com.company.library.view.main.MainView
import com.vaadin.flow.router.Route
import com.vaadin.flow.server.InputStreamFactory
import com.vaadin.flow.server.StreamResource
import io.jmix.core.Resources
import io.jmix.flowui.component.image.JmixImage
import io.jmix.flowui.view.*
import org.springframework.beans.factory.annotation.Autowired

@Route(value = "book-pictures/:id", layout = MainView::class)
@ViewController(id = "BookPicture.detail")
@ViewDescriptor(path = "book-picture-detail-view.xml")
@EditedEntityContainer("bookPictureDc")
class BookPictureDetailView : StandardDetailView<BookPicture>() {
    @Autowired
    private lateinit var resources: Resources

    @ViewComponent
    private lateinit var picture: JmixImage<Any>

    @Subscribe
    private fun onReady(event: ReadyEvent) {
        if (editedEntity.picturePath != null) {
            picture.setSrc(
                StreamResource(
                    editedEntity.bookName,
                    InputStreamFactory { resources.getResourceAsStream(editedEntity.picturePath) }
                ))
        }
    }
}