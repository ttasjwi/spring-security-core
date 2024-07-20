package com.security.security

import org.aopalliance.intercept.MethodInterceptor
import org.aopalliance.intercept.MethodInvocation
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authorization.AuthorizationManager
import org.springframework.security.core.context.SecurityContextHolder
import java.util.function.Supplier

class CustomMethodInterceptor(
    private val authorizationManager: AuthorizationManager<MethodInvocation>,
) : MethodInterceptor {

    override fun invoke(methodInvocation: MethodInvocation): Any? {
        val authentication = Supplier {
            SecurityContextHolder.getContextHolderStrategy().context.authentication
        }

        val isGranted = authorizationManager.check(authentication, methodInvocation)?.isGranted ?: false

        if (isGranted) {
            return methodInvocation.proceed()
        }
        throw AccessDeniedException("권한 없음")
    }

}
