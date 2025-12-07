package org.wagner.single_digit.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wagner.single_digit.presentation.dto.UserPatchRequest;
import org.wagner.single_digit.presentation.mapper.UserMapper;
import org.wagner.single_digit.presentation.dto.UserRequest;
import org.wagner.single_digit.presentation.dto.UserResponse;
import org.wagner.single_digit.presentation.exception.UserNotFoundException;
import org.wagner.single_digit.domain.entity.User;
import org.wagner.single_digit.infrastructure.repository.UserRepository;

import java.util.List;

@Service("userServiceImpl")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserResponse> findAll() {
        List<User> userList = userRepository.findAllBy();
        return userMapper.toResponseList(userList);
    }

    @Override
    public UserResponse findById(Integer id) {
        User user = findEntityById(id);
        return userMapper.toResponse(user);
    }

    @Override
    public User findEntityById(Integer id) {
        return userRepository
                .findWithSingleDigitsById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public UserResponse save(UserRequest userRequest) {
        User user = userMapper.toEntity(userRequest);
        User saved = userRepository.save(user);
        return userMapper.toResponse(saved);
    }

    @Transactional
    @Override
    public UserResponse modify(Integer id, UserRequest user) {
        var userEntity = findEntityById(id);
        userEntity.updateFrom(userMapper.toEntity(user));
        User saved = userRepository.save(userEntity);
        return userMapper.toResponse(saved);
    }

    @Override
    public void deleteById(Integer id) {
        User user = findEntityById(id);
        userRepository.delete(user);
    }

    @Transactional
    @Override
    public void updatePublicKey(Integer id, UserPatchRequest userPatchRequest) {
        User user = findEntityById(id);
        user.setPublicKey(userPatchRequest.publicKey());
        userRepository.save(user);
    }
}
