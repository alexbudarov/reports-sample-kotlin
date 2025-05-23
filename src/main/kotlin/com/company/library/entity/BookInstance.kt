package com.company.library.entity

import io.jmix.core.annotation.DeletedBy
import io.jmix.core.annotation.DeletedDate
import io.jmix.core.entity.annotation.JmixGeneratedValue
import io.jmix.core.metamodel.annotation.InstanceName
import io.jmix.core.metamodel.annotation.JmixEntity
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import java.time.OffsetDateTime
import java.util.*

@JmixEntity
@Table(name = "BOOK_INSTANCE", indexes = [
    Index(name = "IDX_BOOK_INSTANCE_BOOK_PUBLICATION", columnList = "BOOK_PUBLICATION_ID"),
    Index(name = "IDX_BOOK_INSTANCE_LIBRARY_DEPARTMENT", columnList = "LIBRARY_DEPARTMENT_ID")
])
@Entity
open class BookInstance {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    var id: UUID? = null

    @Column(name = "IS_REFERENCE")
    var isReference: Boolean? = null

    @Column(name = "INVENTORY_NUMBER")
    var inventoryNumber: Long? = null

    @Column(name = "BOOK_COUNT")
    var bookCount: Int? = null

    @InstanceName
    @JoinColumn(name = "BOOK_PUBLICATION_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    var bookPublication: BookPublication? = null

    @JoinColumn(name = "LIBRARY_DEPARTMENT_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    var libraryDepartment: LibraryDepartment? = null

    @Column(name = "VERSION", nullable = false)
    @Version
    var version: Int? = null

    @CreatedBy
    @Column(name = "CREATED_BY")
    var createdBy: String? = null

    @CreatedDate
    @Column(name = "CREATED_DATE")
    var createdDate: OffsetDateTime? = null

    @LastModifiedBy
    @Column(name = "LAST_MODIFIED_BY")
    var lastModifiedBy: String? = null

    @LastModifiedDate
    @Column(name = "LAST_MODIFIED_DATE")
    var lastModifiedDate: OffsetDateTime? = null

    @DeletedBy
    @Column(name = "DELETED_BY")
    var deletedBy: String? = null

    @DeletedDate
    @Column(name = "DELETED_DATE")
    var deletedDate: OffsetDateTime? = null

}