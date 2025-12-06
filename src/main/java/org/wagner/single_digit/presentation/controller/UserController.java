package org.wagner.single_digit.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.wagner.single_digit.application.service.UserService;
import org.wagner.single_digit.presentation.dto.UserRequest;
import org.wagner.single_digit.presentation.dto.UserResponse;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserResponse> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserResponse findById(@PathVariable Integer id) {
        return userService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(@RequestBody UserRequest userRequest) {
        return userService.save(userRequest);
    }

    @PutMapping("/{id}")
    public UserResponse modify(@PathVariable Integer id, @RequestBody UserRequest userRequest) {
        return userService.modify(id, userRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        userService.deleteById(id);
    }
}
