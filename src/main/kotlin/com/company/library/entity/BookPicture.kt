package com.company.library.entity

import io.jmix.core.entity.annotation.JmixGeneratedValue
import io.jmix.core.metamodel.annotation.JmixEntity
import io.jmix.core.metamodel.annotation.Store
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import java.util.*

@JmixEntity
@Store(name = "pictures")
@Table(name = "BOOK_PICTURE")
@Entity
open class BookPicture {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    var id: UUID? = null

    @Column(name = "BOOK_NAME", nullable = false)
    @NotNull
    var bookName: String? = null

    @Column(name = "PICTURE_PATH")
    var picturePath: String? = null
}