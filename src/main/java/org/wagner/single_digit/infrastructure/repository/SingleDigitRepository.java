package org.wagner.single_digit.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.wagner.single_digit.domain.entity.SingleDigit;

import java.util.List;

@Repository
public interface SingleDigitRepository extends JpaRepository<SingleDigit, Integer> {

    List<SingleDigit> findByUserId(Integer userId);
}
