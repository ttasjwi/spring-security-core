package com.security.support.logging

import com.security.support.logging.impl.DelegatingLogger

fun getLogger(clazz: Class<*>): Logger = DelegatingLogger(clazz)
