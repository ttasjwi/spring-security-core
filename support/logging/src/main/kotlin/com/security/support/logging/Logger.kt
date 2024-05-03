package com.security.support.logging

interface Logger {

    fun trace(message: () -> Any?)

    fun trace(throwable: Throwable, message: () -> Any?)

    fun debug(message: () -> Any?)

    fun debug(throwable: Throwable, message: () -> Any?)

    fun info(message: () -> Any?)

    fun info(throwable: Throwable, message: () -> Any?)

    fun warn(message: () -> Any?)

    fun warn(throwable: Throwable, message: () -> Any?)

    fun error(message: () -> Any?)

    fun error(throwable: Throwable, message: () -> Any?)
}
