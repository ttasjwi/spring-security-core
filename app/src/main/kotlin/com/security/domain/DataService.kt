package com.security.domain

import org.springframework.stereotype.Service

@Service
class DataService {

    fun getUser(): String {
        return "user"
    }

    fun getOwner(name: String): Account {
        return Account(name, false)
    }

    fun display(): String {
        return "display"
    }
}
