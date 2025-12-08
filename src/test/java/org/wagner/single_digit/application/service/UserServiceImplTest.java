package org.wagner.single_digit.application.service;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.wagner.single_digit.domain.entity.User;
import org.wagner.single_digit.infrastructure.repository.UserRepository;
import org.wagner.single_digit.presentation.dto.UserPatchRequest;
import org.wagner.single_digit.presentation.dto.UserRequest;
import org.wagner.single_digit.presentation.dto.UserResponse;
import org.wagner.single_digit.presentation.exception.UserNotFoundException;
import org.wagner.single_digit.presentation.mapper.UserMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void findByAll_givenSuccess_returnUserList() {
        List<User> userList = List.of(mock(User.class));
        List<UserResponse> responseList = List.of(mock(UserResponse.class));

        doReturn(userList)
            .when(userRepository)
                .findAllBy();
        doReturn(responseList)
            .when(userMapper)
                .toResponseList(userList);

        List<UserResponse> result = userService.findAll();

        assertEquals(responseList, result);
    }

    @Test
    void findById_givenSuccess_returnUser() {
        User user = mock(User.class);
        UserResponse response = mock(UserResponse.class);

        doReturn(Optional.of(user))
            .when(userRepository)
                .findWithSingleDigitsById(2);
        doReturn(response)
            .when(userMapper)
                .toResponse(user);

        UserResponse result = userService.findById(2);

        assertEquals(response, result);
    }

    @Test
    void findEntityById_givenUserId_returnUser() {
        User user = mock(User.class);

        doReturn(Optional.of(user))
                .when(userRepository)
                .findWithSingleDigitsById(2);

        User result = userService.findEntityById(2);

        assertEquals(user, result);
    }

    @Test
    void findEntityById_givenUserNotFound_throwsUserNotFoundException() {
        doReturn(Optional.empty())
            .when(userRepository)
                .findWithSingleDigitsById(2);

        assertThrows(UserNotFoundException.class, () -> userService.findEntityById(2));
    }

    @Test
    void save_givenUserRequest_saveUser() {
        var userRequest = mock(UserRequest.class);
        var user = mock(User.class);
        var userSaved = new User();
        var userResponse = mock(UserResponse.class);

        doReturn(user)
            .when(userMapper)
                .toEntity(userRequest);
        doReturn(userSaved)
            .when(userRepository)
                .save(user);
        doReturn(userResponse)
            .when(userMapper)
                .toResponse(userSaved);

        UserResponse result = userService.save(userRequest);

        assertEquals(userResponse, result);
    }

    @Test
    void modify_givenUserRequest_updateUser() {
        var userRequest = mock(UserRequest.class);
        var user = mock(User.class);
        var userChanges = new User();
        var userResponse = mock(UserResponse.class);

        doReturn(Optional.of(user))
            .when(userRepository)
                .findWithSingleDigitsById(2);
        doReturn(userChanges)
            .when(userMapper)
                .toEntity(userRequest);
        doReturn(userChanges)
            .when(userRepository)
                .save(user);
        doReturn(userResponse)
            .when(userMapper)
                .toResponse(userChanges);

        var result = userService.modify(2, userRequest);

        assertEquals(userResponse, result);
        verify(user, times(1))
                .updateFrom(userChanges);
    }

    @Test
    void delete_givenUserId_deleteUser() {
        var user = mock(User.class);

        doReturn(Optional.of(user))
                .when(userRepository)
                .findWithSingleDigitsById(3);

        userService.deleteById(3);

        verify(userRepository, times(1))
            .delete(user);
    }

    @Test
    void updatePublicKey_givenUserIdAndUserPatchRequest_updateUser() {
        var userPatchRequest = new UserPatchRequest("publicKey");
        var user = mock(User.class);

        doReturn(Optional.of(user))
                .when(userRepository)
                .findWithSingleDigitsById(18);

        userService.updatePublicKey(18, userPatchRequest);

        verify(userRepository, times(1))
                .save(user);
        verify(user, times(1))
            .setPublicKey("publicKey");
    }
}