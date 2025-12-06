package org.wagner.single_digit.presentation.dto;

import java.util.List;

public record UserResponse(Integer id, String name, String email, List<SingleDigitResponse> singleDigits) {
}
