package org.wagner.single_digit.application.service;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.wagner.single_digit.domain.entity.SingleDigit;
import org.wagner.single_digit.domain.entity.User;
import org.wagner.single_digit.infrastructure.repository.SingleDigitRepository;
import org.wagner.single_digit.presentation.dto.SingleDigitRequest;
import org.wagner.single_digit.presentation.dto.SingleDigitResponse;
import org.wagner.single_digit.presentation.mapper.SingleDigitMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class SingleDigitServiceImplTest {

    @Mock
    private SingleDigitMapper mapper;
    @Mock
    private SingleDigitRepository singleDigitRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private SingleDigitServiceImpl service;

    @Test
    public void calculate_givenRequest_returnResult() {
        var request = new SingleDigitRequest("2", 3);
        SingleDigit toBeReturned = new SingleDigit();
        toBeReturned.setResult(6);

        doReturn(toBeReturned)
                .when(mapper)
                .toEntity(request);

        Integer result = service.calculate(request);

        assertEquals(6, result);
    }

    @Test
    public void calculate_givenRequestAndUserId_returnResult() {
        var request = new SingleDigitRequest("2", 3);
        SingleDigit toBeReturned = new SingleDigit();
        toBeReturned.setResult(6);
        User user = new User();
        SingleDigit singleDigit = new  SingleDigit();
        singleDigit.setResult(6);

        doReturn(toBeReturned)
            .when(mapper)
                .toEntity(request);
        doReturn(user)
            .when(userService)
                .findEntityById(1);
        doReturn(singleDigit)
            .when(mapper)
                .toEntity(request, user, 6);

        Integer result = service.calculate(request, 1);

        assertEquals(6, result);
    }

    @Test
    public void persistWithResult_givenRequestAndUserId_returnResult() {
        var request = new SingleDigitRequest("2", 3);
        var user = mock(User.class);
        var singleDigit = new  SingleDigit();
        singleDigit.setResult(8);

        doReturn(user)
                .when(userService)
                .findEntityById(1);
        doReturn(singleDigit)
                .when(mapper)
                .toEntity(request, user, 8);

        Integer result = service.persistWithResult(request, 1, 8);

        assertEquals(8, result);
        verify(user, times(1)).addSingleDigit(singleDigit);
    }

    @Test
    public void findByUserId_givenUserId_returnResultList() {
        var toBeReturned = List.of(new SingleDigit());
        var toBeMapped = List.of(new SingleDigitResponse("1", 2, 3));

        doReturn(toBeReturned)
            .when(singleDigitRepository)
                .findByUserId(1);
        doReturn(toBeMapped)
            .when(mapper)
                .toResponseList(toBeReturned);

        List<SingleDigitResponse> result = service.findByUserId(1);

        assertEquals(toBeMapped, result);
    }
}