package com.stephen.spring_boot_api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "/test.properties")
class SpringBootApiApplicationTests {

    @Test
    void contextLoads() {}
}
