package org.wagner.single_digit.presentation.controller;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.wagner.single_digit.presentation.dto.SingleDigitRequest;
import org.wagner.single_digit.presentation.dto.SingleDigitResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserSingleDigitControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @Sql(scripts = {"/test-data/cleanup.sql", "/test-data/users.sql"})
    void createSingleDigit_givenSuccess_returnInteger() {
        var request = new SingleDigitRequest("1", 1);

        ResponseEntity<Integer> response = restTemplate.postForEntity(
            "/users/1/single-digits",
            request,
            Integer.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().intValue());
    }

    @Test
    @Sql(scripts = {"/test-data/cleanup.sql", "/test-data/users.sql"})
    void getSingleDigits_givenSuccess_returnEmptySingleDigitList() {

        ResponseEntity<List<SingleDigitResponse>> response = restTemplate.exchange(
                "/users/2/single-digits",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().size());
    }

    @Test
    @Sql(scripts = {"/test-data/cleanup.sql", "/test-data/users.sql"})
    void getSingleDigits_givenSingleDigitAdded_returnSingleDigitList() {
        var request = new SingleDigitRequest("1", 1);

        restTemplate.postForEntity(
                "/users/2/single-digits",
                request,
                Integer.class
        );

        ResponseEntity<List<SingleDigitResponse>> response = restTemplate.exchange(
                "/users/2/single-digits",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        
        SingleDigitResponse singleDigit = response.getBody().get(0);
        assertEquals("1", singleDigit.baseNumber());
        assertEquals(1, singleDigit.repetitionFactor());
        assertEquals(1, singleDigit.result());
    }

    @Test
    @Sql(scripts = {"/test-data/cleanup.sql", "/test-data/users.sql"})
    void createSingleDigit_givenInvalidUserId_returnUserNotFound() {
        var request = new SingleDigitRequest("1", 1);

        ResponseEntity<Integer> response = restTemplate.postForEntity(
                "/users/100/single-digits",
                request,
                Integer.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}