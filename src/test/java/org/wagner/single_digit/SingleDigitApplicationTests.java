package org.wagner.single_digit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.wagner.single_digit.application.service.SingleDigitService;
import org.wagner.single_digit.application.service.UserService;
import org.wagner.single_digit.infrastructure.repository.SingleDigitRepository;
import org.wagner.single_digit.infrastructure.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class SingleDigitApplicationTests {

    @Autowired(required = false)
    private SingleDigitService singleDigitService;

    @Autowired(required = false)
    private UserService userService;

    @Autowired(required = false)
    private SingleDigitRepository singleDigitRepository;

    @Autowired(required = false)
    private UserRepository userRepository;

    @Test
    void contextLoads() {
        // Validates Spring Context
    }

    @Test
    void allRequiredBeansAreLoaded() {
        assertNotNull(singleDigitService, "SingleDigitService should be loaded");
        assertNotNull(userService, "UserService should be loaded");
        assertNotNull(singleDigitRepository, "SingleDigitRepository should be loaded");
        assertNotNull(userRepository, "UserRepository should be loaded");
    }
}
