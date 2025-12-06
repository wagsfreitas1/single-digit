package org.wagner.single_digit.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.wagner.single_digit.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
