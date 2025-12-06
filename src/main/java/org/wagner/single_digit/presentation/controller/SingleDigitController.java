package org.wagner.single_digit.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wagner.single_digit.application.service.SingleDigitService;
import org.wagner.single_digit.presentation.dto.SingleDigitRequest;

@RestController
@RequestMapping("/single-digits")
@RequiredArgsConstructor
public class SingleDigitController {

    private final SingleDigitService singleDigitService;

    @PostMapping
    public Integer calculate(@RequestBody SingleDigitRequest request) {
        return singleDigitService.calculate(request);
    }
}
