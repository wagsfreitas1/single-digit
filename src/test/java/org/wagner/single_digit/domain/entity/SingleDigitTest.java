package org.wagner.single_digit.domain.entity;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class SingleDigitTest {

    @Test
    void sumDigits_givenResultLessThanTen_shouldNotEnterLoop() {
        var singleDigit = new SingleDigit();
        singleDigit.setBaseNumber("1");
        singleDigit.setRepetitionFactor(3);

        singleDigit.sumDigits();

        assertEquals(3, singleDigit.getResult());
    }

    @Test
    void sumDigits_givenResultGreaterThanTen_shouldEnterLoop() {
        var singleDigit = new SingleDigit();
        singleDigit.setBaseNumber("99");
        singleDigit.setRepetitionFactor(1);

        singleDigit.sumDigits();

        assertEquals(9, singleDigit.getResult());
    }

    @Test
    void sumDigits_givenResultExactlyTen_shouldEnterLoop() {
        var singleDigit = new SingleDigit();
        singleDigit.setBaseNumber("19");
        singleDigit.setRepetitionFactor(1);

        singleDigit.sumDigits();

        assertEquals(1, singleDigit.getResult());
    }
}