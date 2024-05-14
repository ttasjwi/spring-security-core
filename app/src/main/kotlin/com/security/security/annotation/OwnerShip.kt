package com.security.security.annotation

import org.springframework.security.access.prepost.PostAuthorize

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@PostAuthorize("returnObject.owner == authentication.name")
annotation class OwnerShip
