package com.security.domain

import org.springframework.security.access.prepost.PostAuthorize
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service

@Service
class DataService {

    @PreAuthorize("")
    fun getUser(): String {
        return "user"
    }

    @PostAuthorize("")
    fun getOwner(name: String): Account {
        return Account(name, false)
    }

    fun display(): String {
        return "display"
    }
}
