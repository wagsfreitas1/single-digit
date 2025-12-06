package org.wagner.single_digit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wagner.single_digit.exception.UserNotFoundException;
import org.wagner.single_digit.model.User;
import org.wagner.single_digit.model.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public User modify(Integer id, User user) {
        var userEntity = findById(id);
        userEntity.updateFrom(user);
        return userRepository.save(userEntity);
    }

    public void deleteById(Integer id) {
        User user = findById(id);
        userRepository.delete(user);
    }
}
