package org.wagner.single_digit.presentation.controller;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.wagner.single_digit.presentation.dto.SingleDigitRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class SingleDigitControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void calculate_givenSuccess_returnSingleDigit() {
        SingleDigitRequest request = new SingleDigitRequest("1", 1);
        ResponseEntity<Integer> response = restTemplate.postForEntity("/single-digits", request, Integer.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().intValue());
    }
}