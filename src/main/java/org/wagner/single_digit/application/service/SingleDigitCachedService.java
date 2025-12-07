package org.wagner.single_digit.application.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.wagner.single_digit.presentation.dto.SingleDigitRequest;
import org.wagner.single_digit.presentation.dto.SingleDigitResponse;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Primary
public class SingleDigitCachedService implements SingleDigitService {

    private final SingleDigitService delegate;
    private static final int MAX_SIZE = 10;
    private final Map<String, Integer> cachedList = Collections.synchronizedMap(
            new LinkedHashMap<>() {
                @Override
                protected boolean removeEldestEntry(Map.Entry<String, Integer> eldest) {
                    return size() > MAX_SIZE;
                }
            });

    public SingleDigitCachedService(@Qualifier("singleDigitServiceImpl") SingleDigitService delegate) {
        this.delegate = delegate;
    }

    @Override
    public Integer calculate(SingleDigitRequest request) {
        synchronized (cachedList) {
            return cachedList
                    .computeIfAbsent(request.toString(), key -> delegate.calculate(request));
        }
    }

    @Override
    @Transactional
    public Integer calculate(SingleDigitRequest request, Integer userId) {
        Integer result = calculate(request);
        return persistWithResult(request, userId, result);
    }

    @Override
    public Integer persistWithResult(SingleDigitRequest request, Integer userId, Integer result) {
        return delegate.persistWithResult(request, userId, result);
    }

    @Override
    public List<SingleDigitResponse> findByUserId(Integer userId) {
        return delegate.findByUserId(userId);
    }
}
