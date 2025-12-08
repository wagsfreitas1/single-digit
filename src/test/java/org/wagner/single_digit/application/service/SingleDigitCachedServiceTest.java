package org.wagner.single_digit.application.service;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.wagner.single_digit.presentation.dto.SingleDigitRequest;
import org.wagner.single_digit.presentation.dto.SingleDigitResponse;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class SingleDigitCachedServiceTest {


    @Mock
    private SingleDigitService delegate;
    @InjectMocks
    private SingleDigitCachedService service;

    @Test
    public void calculate_givenValidRequest_returnSingleDigitRequest() {
        var request = new SingleDigitRequest("5", 2);

        doReturn(5)
                .when(delegate)
                        .calculate(request);

        Integer result = service.calculate(request);

        assertEquals(5, result);
    }

    @Test
    public void calculate_givenValidRequestAndResultOnCache_dontCallDelegate() {
        var request = new SingleDigitRequest("5", 2);

        doReturn(5)
                .when(delegate)
                        .calculate(request);

        var results = new Integer[]{
                service.calculate(request),
                service.calculate(request)
            };

        assertEquals(5, results[0]);
        assertEquals(5, results[1]);

        verify(delegate, times(1)).calculate(request);
    }

    @Test
    public void calculate_givenValidRequestAndUserId_returnSingleDigitRequest() {
        var request = new SingleDigitRequest("5", 2);

        doReturn(5)
                .when(delegate)
                .calculate(request);

        doReturn(5)
                .when(delegate)
                .persistWithResult(request, 1, 5);

        Integer result = service.calculate(request, 1);

        assertEquals(5, result);
    }

    @Test
    public void persistWithResult_givenRequestAndUserIdAndResult_callDelegate() {
        var request = new SingleDigitRequest("5", 2);

        doReturn(5)
                .when(delegate)
                .persistWithResult(request, 1, 5);

        Integer result = service.persistWithResult(request, 1, 5);

        assertEquals(5, result);
    }

    @Test
    public void findByUserId_givenUserId_returnSingleDigitRequest() {
        List<SingleDigitResponse> toBeReturned = List.of(new SingleDigitResponse("1", 2, 3));
        doReturn(toBeReturned)
        .when(delegate)
                .findByUserId(1);

        List<SingleDigitResponse> result = service.findByUserId(1);

        assertEquals(toBeReturned, result);
    }

    @ParameterizedTest
    @MethodSource("cacheSizeTestCases")
    void calculate_whenAddingRequests_shouldMaintainMaxCacheSize(
            int numberOfRequests,
            boolean firstShouldBeRemoved
    ) {
        SingleDigitRequest[] requests = new SingleDigitRequest[numberOfRequests];
        for (int i = 0; i < numberOfRequests; i++) {
            requests[i] = new SingleDigitRequest(String.valueOf(i), 1);
            when(delegate.calculate(requests[i])).thenReturn(i);
        }

        for (SingleDigitRequest request : requests) {
            service.calculate(request);
        }

        service.calculate(requests[0]);
        int expectedCalls = firstShouldBeRemoved ? 2 : 1;
        verify(delegate, times(expectedCalls)).calculate(requests[0]);
    }

    static Stream<Arguments> cacheSizeTestCases() {
        return Stream.of(
                Arguments.of(5, false),
                Arguments.of(10, false),
                Arguments.of(11, true),
                Arguments.of(15, true)
        );
    }

    @Test
    void calculate_whenCalledConcurrently_shouldBeThreadSafe() throws InterruptedException {
        var request = new SingleDigitRequest("5", 2);
        when(delegate.calculate(request)).thenReturn(5);

        int threads = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(threads);

        for (int i = 0; i < threads; i++) {
            executor.submit(() -> {
                try {
                    service.calculate(request);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(5, TimeUnit.SECONDS);
        executor.shutdown();

        verify(delegate, times(1)).calculate(request);
    }
}