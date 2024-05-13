package com.security.domain

import org.springframework.security.access.prepost.PostFilter
import org.springframework.security.access.prepost.PreFilter
import org.springframework.stereotype.Service

@Service
class AccountService {

    private val repository = mapOf(
        "user" to Account("user", false),
        "db" to Account("db", false),
        "admin" to Account("admin", true)
    )

    @PreFilter("filterObject.owner == authentication.name")
    fun writeList(accounts: List<Account>): List<Account> {
        return accounts
    }

    @PreFilter("filterObject.value.owner == authentication.name")
    fun writeMap( accounts: Map<String, Account>): Map<String, Account> {
        return accounts
    }

    @PostFilter("filterObject.owner == authentication.name")
    fun readList(): List<Account> {
        return repository.values.toList()
    }

    @PostFilter("filterObject.value.owner == authentication.name")
    fun readMap(): Map<String, Account> {
        return repository.toMap()
    }
}
