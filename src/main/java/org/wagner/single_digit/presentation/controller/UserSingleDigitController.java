package org.wagner.single_digit.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.wagner.single_digit.application.service.SingleDigitService;
import org.wagner.single_digit.presentation.dto.SingleDigitRequest;
import org.wagner.single_digit.presentation.dto.SingleDigitResponse;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/single-digits")
@RequiredArgsConstructor
public class UserSingleDigitController {

    private final SingleDigitService singleDigitService;

    @PostMapping
    public Integer createSingleDigit(@PathVariable Integer userId, @RequestBody SingleDigitRequest request) {
        return singleDigitService.calculate(request, userId);
    }

    @GetMapping
    public List<SingleDigitResponse> getSingleDigits(@PathVariable Integer userId) {
        return singleDigitService.findByUserId(userId);
    }
}
