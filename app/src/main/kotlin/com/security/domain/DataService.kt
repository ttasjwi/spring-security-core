package com.security.domain

import org.springframework.security.access.prepost.PostAuthorize
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service

@Service
class DataService {

    @PreAuthorize("hasAuthority('ROLE_USER')")
    fun getUser(): String {
        return "user"
    }

    @PostAuthorize("returnObject.owner == authentication.name")
    fun getOwner(name: String): Account {
        return Account(name, false)
    }

    fun display(): String {
        return "display"
    }
}
