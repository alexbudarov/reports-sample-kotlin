package com.company.library.entity

import io.jmix.core.HasTimeZone
import io.jmix.core.annotation.DeletedBy
import io.jmix.core.annotation.DeletedDate
import io.jmix.core.annotation.Secret
import io.jmix.core.entity.annotation.JmixGeneratedValue
import io.jmix.core.entity.annotation.SystemLevel
import io.jmix.core.metamodel.annotation.DependsOnProperties
import io.jmix.core.metamodel.annotation.InstanceName
import io.jmix.core.metamodel.annotation.JmixEntity
import io.jmix.security.authentication.JmixUserDetails
import jakarta.persistence.*
import jakarta.validation.constraints.Email
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.security.core.GrantedAuthority
import java.time.OffsetDateTime
import java.util.*

@JmixEntity
@Entity
@Table(name = "USER_", indexes = [
    Index(name = "IDX_USER__ON_USERNAME", columnList = "USERNAME", unique = true)
])
open class User : JmixUserDetails, HasTimeZone {

    @Id
    @Column(name = "ID", nullable = false)
    @JmixGeneratedValue
    var id: UUID? = null

    @Version
    @Column(name = "VERSION", nullable = false)
    var version: Int? = null

    @Column(name = "USERNAME", nullable = false)
    @get:JvmName("getUsername_")
    var username: String? = null

    @Secret
    @SystemLevel
    @Column(name = "PASSWORD")
    @get:JvmName("getPassword_")
    var password: String? = null

    @Column(name = "FIRST_NAME")
    var firstName: String? = null

    @Column(name = "LAST_NAME")
    var lastName: String? = null

    @Email
    @Column(name = "EMAIL")
    var email: String? = null

    @Column(name = "ACTIVE")
    var active: Boolean? = true

    @Column(name = "TIME_ZONE_ID")
    @get:JvmName("getTimeZoneId_")
    var timeZoneId: String? = null

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

    @Transient
    private var userAuthorities: Collection<GrantedAuthority?>? = null

    override fun getPassword(): String? = password

    override fun getUsername(): String? = username

    override fun getAuthorities(): Collection<GrantedAuthority?> =
            userAuthorities ?: emptyList()

    override fun setAuthorities(authorities: Collection<GrantedAuthority?>) {
        this.userAuthorities = authorities
    }

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = active == true

    @get:DependsOnProperties("firstName", "lastName", "username")
    @get:InstanceName
    val displayName: String
        get() = "${firstName ?: ""} ${lastName ?: ""} [${username ?: ""}]".trim()

    override fun getTimeZoneId(): String? {
        return timeZoneId
    }

    override fun isAutoTimeZone(): Boolean = true
}