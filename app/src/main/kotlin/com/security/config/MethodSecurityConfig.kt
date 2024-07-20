package com.security.config

import com.security.security.CustomMethodInterceptor
import org.springframework.aop.Advisor
import org.springframework.aop.aspectj.AspectJExpressionPointcut
import org.springframework.aop.support.DefaultPointcutAdvisor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authorization.AuthenticatedAuthorizationManager

@Configuration
class MethodSecurityConfig {

    @Bean
    fun serviceAdvisor(): Advisor {
        val pointCut = AspectJExpressionPointcut()
        pointCut.expression = "execution(* com.security.domain.DataService.*(..))"

        val advice = CustomMethodInterceptor(AuthenticatedAuthorizationManager())

        return DefaultPointcutAdvisor(pointCut, advice)
    }

}
