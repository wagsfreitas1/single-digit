package org.wagner.single_digit.application.service;

import org.springframework.transaction.annotation.Transactional;
import org.wagner.single_digit.domain.entity.User;
import org.wagner.single_digit.presentation.dto.UserPatchRequest;
import org.wagner.single_digit.presentation.dto.UserRequest;
import org.wagner.single_digit.presentation.dto.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> findAll();

    UserResponse findById(Integer id);

    User findEntityById(Integer id);

    UserResponse save(UserRequest userRequest);

    @Transactional
    UserResponse modify(Integer id, UserRequest user);

    void deleteById(Integer id);

    @Transactional
    void updatePublicKey(Integer id, UserPatchRequest userPatchRequest);
}
