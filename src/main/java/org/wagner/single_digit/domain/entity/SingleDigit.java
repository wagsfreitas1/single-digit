package org.wagner.single_digit.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "single_digit")
public class SingleDigit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String baseNumber;

    private Integer repetitionFactor;

    private Integer result;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void sumDigits() {
        String completeNumber = baseNumber.repeat(repetitionFactor);

        int summedDigits = sumDigits(completeNumber);

        while (summedDigits >= 10) {
            summedDigits = sumDigits(String.valueOf(summedDigits));
        }

        result = summedDigits;
    }

    private int sumDigits(String completeNumber) {
        return completeNumber
                .chars()
                .map(Character::getNumericValue)
                .sum();
    }
}
