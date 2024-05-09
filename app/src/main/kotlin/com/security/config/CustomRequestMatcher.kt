package com.security.config

import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.web.util.matcher.RequestMatcher

class CustomRequestMatcher(
    private val urlPattern: String
) : RequestMatcher {

    override fun matches(request: HttpServletRequest): Boolean {
        val requestURI = request.requestURI
        return requestURI.startsWith(urlPattern)
    }

}
