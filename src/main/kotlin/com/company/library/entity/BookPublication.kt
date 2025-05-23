package com.company.library.entity

import io.jmix.core.annotation.DeletedBy
import io.jmix.core.annotation.DeletedDate
import io.jmix.core.entity.annotation.JmixGeneratedValue
import io.jmix.core.metamodel.annotation.InstanceName
import io.jmix.core.metamodel.annotation.JmixEntity
import io.jmix.core.metamodel.annotation.NumberFormat
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import java.time.OffsetDateTime
import java.util.*

@JmixEntity
@Table(name = "BOOK_PUBLICATION", indexes = [
    Index(name = "IDX_BOOK_PUBLICATION_BOOK", columnList = "BOOK_ID"),
    Index(name = "IDX_BOOK_PUBLICATION_PUBLISHER", columnList = "PUBLISHER_ID"),
    Index(name = "IDX_BOOK_PUBLICATION_CITY", columnList = "CITY_ID")
])
@Entity
open class BookPublication {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    var id: UUID? = null

    @NumberFormat(pattern = "####")
    @Column(name = "YEAR_")
    var year: Int? = null

    @InstanceName
    @JoinColumn(name = "BOOK_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    var book: Book? = null

    @JoinColumn(name = "PUBLISHER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    var publisher: Publisher? = null

    @JoinColumn(name = "CITY_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    var city: City? = null

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