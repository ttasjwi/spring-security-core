package com.security.security

import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations
import org.springframework.stereotype.Component

@Component("myAuthorizer")
internal class MyAuthorizer {
    fun isUser(operations: MethodSecurityExpressionOperations): Boolean {
        val decision = operations.hasAuthority("ROLE_USER") // 인증된,사용자가,ROLE_USER,권한을,가지고,있는지를,검사
        return decision
    }
}
