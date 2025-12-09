package org.wagner.single_digit.presentation.controller;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.wagner.single_digit.presentation.dto.UserPatchRequest;
import org.wagner.single_digit.presentation.dto.UserRequest;
import org.wagner.single_digit.presentation.dto.UserResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @Sql(scripts = {"/test-data/cleanup.sql", "/test-data/users.sql"})
    void findAll_givenSuccess_returnUsersList() {
        ResponseEntity<List<UserResponse>> response = restTemplate.exchange(
                "/users",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().size());
    }

    @Test
    @Sql(scripts = {"/test-data/cleanup.sql", "/test-data/users.sql"})
    void findById_givenSuccess_returnUser() {
        ResponseEntity<UserResponse> response = restTemplate.exchange(
                "/users/1",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().id());
        assertEquals("João Silva", response.getBody().name());
        assertEquals("joao@email.com", response.getBody().email());
    }

    @Test
    @Sql(scripts = {"/test-data/cleanup.sql", "/test-data/users.sql"})
    void findById_givenUserNotFound_return404() {
        ResponseEntity<UserResponse> response = restTemplate.exchange(
                "/users/100",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Sql(scripts = {"/test-data/cleanup.sql"})
    void create_givenSuccess_createUser() {
        var request = new UserRequest("name", "email", "publicKey");

        ResponseEntity<UserResponse> response = restTemplate.postForEntity(
                "/users",
                request,
                UserResponse.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("name", response.getBody().name());
        assertEquals("email", response.getBody().email());
        assertNotNull(response.getBody().id());
        assertNotNull(response.getBody().singleDigits());
    }

    @Test
    @Sql(scripts = {"/test-data/cleanup.sql", "/test-data/users.sql"})
    void modify_givenSuccess_modifyUser() {
        var request = new UserRequest("name", "email", "publicKey");

        ResponseEntity<UserResponse> response = restTemplate.exchange(
                "/users/1",
                HttpMethod.PUT,
                new HttpEntity<>(request),
                new ParameterizedTypeReference<>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("name", response.getBody().name());
        assertEquals("email", response.getBody().email());
    }

    @Test
    @Sql(scripts = {"/test-data/cleanup.sql", "/test-data/users.sql"})
    void modify_givenInvalidUser_returnUserNotFound() {
        var request = new UserRequest("name", "email", "publicKey");

        ResponseEntity<UserResponse> response = restTemplate.exchange(
                "/users/100",
                HttpMethod.PUT,
                new HttpEntity<>(request),
                new ParameterizedTypeReference<>() {}
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Sql(scripts = {"/test-data/cleanup.sql", "/test-data/users.sql"})
    void delete_givenValidUserId_deleteUser() {
        ResponseEntity<Void> response = restTemplate.exchange(
                "/users/1",
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    @Sql(scripts = {"/test-data/cleanup.sql", "/test-data/users.sql"})
    void delete_givenInvalidUserId_returnUserNotFound() {
        ResponseEntity<Void> response = restTemplate.exchange(
                "/users/100",
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    @Sql(scripts = {"/test-data/cleanup.sql", "/test-data/users.sql"})
    void updatePublicKey_givenValidUserId_returnNoContent() {
        var request = new UserPatchRequest("publicKey");

        ResponseEntity<Void> response = restTemplate.exchange(
                "/users/1",
                HttpMethod.PATCH,
                new HttpEntity<>(request),
                Void.class
        );

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    @Sql(scripts = {"/test-data/cleanup.sql", "/test-data/users.sql"})
    void updatePublicKey_givenInvalidUserId_returnUserNotFound() {
        var request = new UserPatchRequest("publicKey");

        ResponseEntity<Void> response = restTemplate.exchange(
                "/users/100",
                HttpMethod.PATCH,
                new HttpEntity<>(request),
                Void.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
}