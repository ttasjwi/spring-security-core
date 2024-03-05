package com.security.core.util

import io.github.oshai.kotlinlogging.KotlinLogging

fun getLogger(clazz: Class<Any>) = KotlinLogging.logger(clazz.name)
