package com.company.library.entity

import io.jmix.core.entity.annotation.JmixGeneratedValue
import io.jmix.core.metamodel.annotation.JmixEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@JmixEntity
@Table(name = "INIT_FLAGS")
@Entity
open class InitFlags {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    var id: Int? = null

    @Column(name = "REPORTS_INITIALIZED")
    var reportsInitialized: Boolean? = null
}