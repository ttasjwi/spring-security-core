package com.security.adapters.input.event

import com.security.security.event.CustomAuthenticationFailureEvent
import com.security.security.event.CustomAuthenticationSuccessEvent
import com.security.support.logging.getLogger
import org.springframework.context.event.EventListener
import org.springframework.security.authentication.event.*
import org.springframework.stereotype.Component

@Component
class AuthenticationEventInputAdapter {

    companion object {
        val log = getLogger(AuthenticationEventInputAdapter::class.java)
    }

    @EventListener
    fun onAuthenticationSuccess(successEvent: AuthenticationSuccessEvent) {
        log.info { "success = ${successEvent.authentication.name}" }
    }

    /**
     * 대화형 인증 성공
     */
    @EventListener
    fun onSuccess(successEvent: InteractiveAuthenticationSuccessEvent) {
        log.info { "success = ${successEvent.authentication.name}" }
    }

    @EventListener
    fun onSuccess(successEvent: CustomAuthenticationSuccessEvent) {
        log.info { "success = ${successEvent.authentication.name}" }
    }

    @EventListener
    fun onFailure(failures: AbstractAuthenticationFailureEvent) {

        log.info {"failures = ${failures.exception.message}" }
    }

    @EventListener
    fun onFailure(failures: AuthenticationFailureBadCredentialsEvent) {
        log.info {"failures = ${failures.exception.message}" }
    }

    @EventListener
    fun onFailure(failures: CustomAuthenticationFailureEvent) {
        log.info {"failures = ${failures.exception.message}" }
    }

}
