package org.wagner.single_digit.application.service;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.wagner.single_digit.application.service.crypto.CryptoService;
import org.wagner.single_digit.domain.entity.User;
import org.wagner.single_digit.presentation.dto.UserPatchRequest;
import org.wagner.single_digit.presentation.dto.UserRequest;
import org.wagner.single_digit.presentation.dto.UserResponse;
import org.wagner.single_digit.presentation.mapper.UserMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserEncryptedServiceTest {

    @Mock
    private UserServiceImpl delegate;
    @Mock
    private UserMapper userMapper;
    @Mock
    private CryptoService cryptoService;
    @InjectMocks
    private UserEncryptedService service;

    @Test
    void updateUser_givenSuccess_returnUserList() {
        var toBeReturned = List.of(mock(UserResponse.class));

        doReturn(toBeReturned)
            .when(delegate)
                .findAll();

        var result = service.findAll();

        assertEquals(toBeReturned, result);
    }

    @Test
    void findById_givenSuccessNullPublicKey_returnUser() {
        var userResponse = mock(UserResponse.class);
        var user = mock(User.class);

        doReturn(user)
                .when(delegate)
                .findEntityById(1);
        doReturn(userResponse)
                .when(userMapper)
                .toResponse(user);
        doReturn(null)
                .when(user)
                .getPublicKey();

        var result = service.findById(1);

        assertEquals(userResponse, result);
    }

    @Test
    void findById_givenSuccessAndUserHasPublicKey_returnUserCryptographed() {
        var userResponse = mock(UserResponse.class);
        var user = new User();
        user.setId(2);
        user.setName("name");
        user.setEmail("email");
        user.setPublicKey("publicKey");
        var expected = new UserResponse(2, "cowabanga", "cowabanga", List.of());

        doReturn(user)
                .when(delegate)
                .findEntityById(2);
        doReturn(userResponse)
                .when(userMapper)
                .toResponse(user);
        doReturn("cowabanga")
                .when(cryptoService)
                .encrypt(anyString(), anyString());

        var result = service.findById(2);

        verify(cryptoService, times(1))
                .encrypt("name", "publicKey");
        verify(cryptoService, times(1))
                .encrypt("email", "publicKey");
        assertEquals(expected, result);
    }

    @Test
    void findEntityById_givenUserId_returnUser() {
        var user = mock(User.class);

        doReturn(user)
                .when(delegate)
                .findEntityById(3);

        var result = service.findEntityById(3);

        assertEquals(user, result);
    }

    @Test
    void save_givenUserRequest_saveUser() {
        var userResponse = mock(UserResponse.class);
        var userRequest = mock(UserRequest.class);

        doReturn(userResponse)
            .when(delegate)
                .save(userRequest);

        var result = service.save(userRequest);

        assertEquals(userResponse, result);
    }

    @Test
    void modify_givenUserRequest_modifyUser() {
        var userResponse = mock(UserResponse.class);
        var userRequest = mock(UserRequest.class);

        doReturn(userResponse)
            .when(delegate)
                .modify(5, userRequest);

        var result = service.modify(5, userRequest);

        assertEquals(userResponse, result);
    }

    @Test
    void deleteById_givenUserId_deleteUser() {
        service.deleteById(5);

        verify(delegate, times(1)).deleteById(5);
    }

    @Test
    void updatePublicKey_givenUserIdAndUserPatchRequest_updateUser() {
        UserPatchRequest userPatchRequest = mock(UserPatchRequest.class);

        service.updatePublicKey(5, userPatchRequest);

        verify(delegate, times(1)).updatePublicKey(5, userPatchRequest);
    }
}