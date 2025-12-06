package org.wagner.single_digit.presentation.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.wagner.single_digit.domain.entity.SingleDigit;
import org.wagner.single_digit.domain.entity.User;
import org.wagner.single_digit.presentation.dto.SingleDigitRequest;
import org.wagner.single_digit.presentation.dto.SingleDigitResponse;

import java.util.List;

@Mapper
public interface SingleDigitMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "result", ignore = true)
    SingleDigit toEntity(SingleDigitRequest request);

    @Mapping(target = "id", ignore = true)
    SingleDigit toEntity(SingleDigitRequest request, User user, Integer result);

    SingleDigitResponse toResponse(SingleDigit singleDigit);

    List<SingleDigitResponse> toResponseList(List<SingleDigit> singleDigits);

    @AfterMapping
    default void afterMapping(@MappingTarget SingleDigit entity) {
        if(entity.getResult() == null) {
            entity.sumDigits();
        }
    }
}
