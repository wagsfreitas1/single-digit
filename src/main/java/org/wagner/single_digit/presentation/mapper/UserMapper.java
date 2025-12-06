package org.wagner.single_digit.presentation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.wagner.single_digit.domain.entity.User;
import org.wagner.single_digit.presentation.dto.UserRequest;
import org.wagner.single_digit.presentation.dto.UserResponse;

import java.util.List;

@Mapper(uses = SingleDigitMapper.class)
public interface UserMapper {

    UserResponse toResponse(User user);

    List<UserResponse> toResponseList(List<User> users);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "singleDigits", ignore = true)
    User toEntity(UserRequest user);
}
