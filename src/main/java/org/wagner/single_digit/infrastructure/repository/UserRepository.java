package org.wagner.single_digit.infrastructure.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.wagner.single_digit.domain.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @EntityGraph(attributePaths = "singleDigits")
    Optional<User> findWithSingleDigitsById(Integer id);

    @EntityGraph(attributePaths = "singleDigits")
    List<User> findAllBy();
}
