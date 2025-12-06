package org.wagner.single_digit.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wagner.single_digit.presentation.mapper.UserMapper;
import org.wagner.single_digit.presentation.dto.UserRequest;
import org.wagner.single_digit.presentation.dto.UserResponse;
import org.wagner.single_digit.presentation.exception.UserNotFoundException;
import org.wagner.single_digit.domain.entity.User;
import org.wagner.single_digit.infrastructure.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserResponse> findAll() {
        List<User> userList = userRepository.findAllBy();
        return userMapper.toResponseList(userList);
    }

    public UserResponse findById(Integer id) {
        User user = findEntityById(id);
        return userMapper.toResponse(user);
    }

    public User findEntityById(Integer id) {
        return userRepository
                .findWithSingleDigitsById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public UserResponse save(UserRequest userRequest) {
        User user = userMapper.toEntity(userRequest);
        User saved = userRepository.save(user);
        return userMapper.toResponse(saved);
    }

    @Transactional
    public UserResponse modify(Integer id, UserRequest user) {
        var userEntity = findEntityById(id);
        userEntity.updateFrom(userMapper.toEntity(user));
        User saved = userRepository.save(userEntity);
        return userMapper.toResponse(saved);
    }

    public void deleteById(Integer id) {
        User user = findEntityById(id);
        userRepository.delete(user);
    }
}
