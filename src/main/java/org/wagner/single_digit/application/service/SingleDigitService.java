package org.wagner.single_digit.application.service;

import org.wagner.single_digit.presentation.dto.SingleDigitRequest;
import org.wagner.single_digit.presentation.dto.SingleDigitResponse;

import java.util.List;

public interface SingleDigitService {

    Integer calculate(SingleDigitRequest request);

    Integer calculate(SingleDigitRequest request, Integer userId);

    Integer persistWithResult(SingleDigitRequest request, Integer userId, Integer result);

    List<SingleDigitResponse> findByUserId(Integer userId);
}
