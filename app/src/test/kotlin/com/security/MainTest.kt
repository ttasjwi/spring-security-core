package com.security

import com.security.support.logging.getLogger
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class MainTest {

    private val logger = getLogger(javaClass)

    @Test
    fun contextLoad() {
        logger.info { "컨텍스트 로딩 성공" }
    }
}
