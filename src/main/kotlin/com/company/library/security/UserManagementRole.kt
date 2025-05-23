package com.company.library.security

import com.company.library.entity.User
import io.jmix.security.model.EntityAttributePolicyAction
import io.jmix.security.model.EntityPolicyAction
import io.jmix.security.role.annotation.EntityAttributePolicy
import io.jmix.security.role.annotation.EntityAttributePolicyContainer
import io.jmix.security.role.annotation.EntityPolicy
import io.jmix.security.role.annotation.ResourceRole

@ResourceRole(name = "User Management", code = UserManagementRole.CODE, scope = ["API"])
interface UserManagementRole {

    companion object {
        const val CODE = "user-management"
    }

    @EntityAttributePolicyContainer(
        EntityAttributePolicy(
            entityClass = User::class,
            attributes = ["*"],
            action = EntityAttributePolicyAction.MODIFY
        )
    )
    @EntityPolicy(entityClass = User::class, actions = [EntityPolicyAction.ALL])
    fun user()
}