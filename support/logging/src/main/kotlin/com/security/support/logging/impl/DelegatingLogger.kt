package com.security.support.logging.impl

import com.security.support.logging.Logger
import io.github.oshai.kotlinlogging.KotlinLogging

internal class DelegatingLogger (clazz: Class<*>) : Logger {

    private val delegatingLogger = KotlinLogging.logger(clazz.name)

    override fun trace(message: () -> Any?) {
        delegatingLogger.trace(message)
    }

    override fun trace(throwable: Throwable, message: () -> Any?) {
        delegatingLogger.trace(throwable, message)
    }

    override fun debug(message: () -> Any?) {
        delegatingLogger.debug(message)
    }

    override fun debug(throwable: Throwable, message: () -> Any?) {
        delegatingLogger.debug(throwable, message)
    }

    override fun info(message: () -> Any?) {
        delegatingLogger.info(message)
    }

    override fun info(throwable: Throwable, message: () -> Any?) {
        delegatingLogger.info(throwable, message)
    }

    override fun warn(message: () -> Any?) {
        delegatingLogger.warn(message)
    }

    override fun warn(throwable: Throwable, message: () -> Any?) {
        delegatingLogger.warn(throwable, message)
    }

    override fun error(message: () -> Any?) {
        delegatingLogger.error(message)
    }

    override fun error(throwable: Throwable, message: () -> Any?) {
        delegatingLogger.error(throwable, message)
    }
}
