package com.security.core.service

import com.security.core.util.getLogger
import org.springframework.security.core.session.SessionRegistry
import org.springframework.stereotype.Service

@Service
class SessionInfoService(
    private val sessionRegistry: SessionRegistry
) {

    private val logger = getLogger(javaClass)

    fun sessionInfo() {
        for (principal in sessionRegistry.allPrincipals) {
            val activeSessions = sessionRegistry.getAllSessions(principal, false)
            for (activeSessionInformation in activeSessions) {
                logger.info { "사용자: $principal | 세션 id: ${activeSessionInformation.sessionId} | 최종 요청 시간: ${activeSessionInformation.lastRequest}" }
            }
        }
    }
}
