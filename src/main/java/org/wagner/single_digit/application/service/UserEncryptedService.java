package org.wagner.single_digit.application.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.wagner.single_digit.application.service.crypto.CryptoService;
import org.wagner.single_digit.domain.entity.User;
import org.wagner.single_digit.presentation.dto.UserPatchRequest;
import org.wagner.single_digit.presentation.dto.UserRequest;
import org.wagner.single_digit.presentation.dto.UserResponse;
import org.wagner.single_digit.presentation.mapper.UserMapper;

import java.util.List;

@Service
@Primary
public class UserEncryptedService implements UserService {

    private final UserServiceImpl delegate;
    private final UserMapper userMapper;
    private final CryptoService cryptoService;

    public UserEncryptedService(@Qualifier("userServiceImpl") UserServiceImpl delegate, UserMapper userMapper, CryptoService cryptoService) {
        this.delegate = delegate;
        this.userMapper = userMapper;
        this.cryptoService = cryptoService;
    }

    @Override
    public List<UserResponse> findAll() {
        return delegate.findAll();
    }

    @Override
    public UserResponse findById(Integer id) {
        User user = findEntityById(id);
        UserResponse userResponse = userMapper.toResponse(user);

        if(user.getPublicKey() == null) {
            return userResponse;
        }

        return new UserResponse(
                user.getId(),
                cryptoService.encrypt(user.getName(), user.getPublicKey()),
                cryptoService.encrypt(user.getEmail(), user.getPublicKey()),
                userResponse.singleDigits()
        );
    }

    @Override
    public User findEntityById(Integer id) {
        return delegate.findEntityById(id);
    }

    @Override
    public UserResponse save(UserRequest userRequest) {
        return delegate.save(userRequest);
    }

    @Override
    public UserResponse modify(Integer id, UserRequest user) {
        return delegate.modify(id, user);
    }

    @Override
    public void deleteById(Integer id) {
        delegate.deleteById(id);
    }

    @Override
    public void updatePublicKey(Integer id, UserPatchRequest userPatchRequest) {
        delegate.updatePublicKey(id, userPatchRequest);
    }
}
