package com.company.library.entity

import io.jmix.core.FileRef
import io.jmix.core.annotation.DeletedBy
import io.jmix.core.annotation.DeletedDate
import io.jmix.core.entity.annotation.JmixGeneratedValue
import io.jmix.core.metamodel.annotation.InstanceName
import io.jmix.core.metamodel.annotation.JmixEntity
import io.jmix.eclipselink.lazyloading.NotInstantiatedList
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import java.time.OffsetDateTime
import java.util.*

@JmixEntity
@Table(name = "BOOK", indexes = [
    Index(name = "IDX_BOOK_LITERATURE_TYPE", columnList = "LITERATURE_TYPE_ID")
])
@Entity
open class Book {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    var id: UUID? = null

    @Column(name = "REPORT", length = 1024)
    var report: FileRef? = null

    @InstanceName
    @Column(name = "NAME", nullable = false, length = 50)
    @NotNull
    var name: String? = null

    @Column(name = "SUMMARY")
    @Lob
    var summary: String? = null

    @JoinTable(
        name = "BOOK_AUTHOR_LINK",
        joinColumns = [JoinColumn(name = "BOOK_ID")],
        inverseJoinColumns = [JoinColumn(name = "AUTHOR_ID")]
    )
    @ManyToMany
    var authors: MutableList<Author> = NotInstantiatedList()

    @JoinColumn(name = "LITERATURE_TYPE_ID", nullable = false)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    var literatureType: LiteratureType? = null

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