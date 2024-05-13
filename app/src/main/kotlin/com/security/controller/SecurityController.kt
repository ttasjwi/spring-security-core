package com.security.controller

import com.security.domain.Account
import com.security.domain.AccountService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class SecurityController(
    private val accountService: AccountService
) {

    @GetMapping("/")
    fun index(): String {
        return "index"
    }

    @PostMapping("/writeList")
    fun writeList(@RequestBody accounts: List<Account>): List<Account> {
        return accountService.writeList(accounts)
    }

    @PostMapping("/writeMap")
    fun writeMap(@RequestBody accounts: List<Account>): Map<String, Account> {
        val dataMap = accounts.associateBy { it.owner }
        return accountService.writeMap(dataMap)
    }

    @GetMapping("/readList")
    fun readList(): List<Account> {
        return accountService.readList()
    }

    @GetMapping("/readMap")
    fun readMap(): Map<String, Account> {
        return accountService.readMap()
    }
}
