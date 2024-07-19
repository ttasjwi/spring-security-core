package com.security.config

import org.aopalliance.intercept.MethodInvocation
import org.springframework.aop.Advisor
import org.springframework.aop.Pointcut
import org.springframework.aop.aspectj.AspectJExpressionPointcut
import org.springframework.aop.support.ComposablePointcut
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Role
import org.springframework.security.authorization.AuthorityAuthorizationManager
import org.springframework.security.authorization.method.AuthorizationManagerBeforeMethodInterceptor

@Configuration
class MethodSecurityConfig {

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    fun myPointCutAdvisor(): Advisor {
        val pointCut = AspectJExpressionPointcut()
        pointCut.expression = "execution(* com.security.domain.DataService.getUser(..))"

        val manager = AuthorityAuthorizationManager.hasRole<MethodInvocation>("USER")
        return AuthorizationManagerBeforeMethodInterceptor(pointCut, manager)
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    fun multiPointCutAdvisor(): Advisor {
        val pointCut1 = AspectJExpressionPointcut()
        pointCut1.expression = "execution(* com.security.domain.DataService.getUser(..))"

        val pointCut2 = AspectJExpressionPointcut()
        pointCut2.expression = "execution(* com.security.domain.DataService.getOwner(..))"

        val pointCutComposite = ComposablePointcut(pointCut1 as Pointcut)
        pointCutComposite.union(pointCut2 as Pointcut)

        val manager = AuthorityAuthorizationManager.hasRole<MethodInvocation>("USER")
        return AuthorizationManagerBeforeMethodInterceptor(pointCutComposite, manager)
    }
}
