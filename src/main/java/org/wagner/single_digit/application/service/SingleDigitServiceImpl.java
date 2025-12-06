package org.wagner.single_digit.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wagner.single_digit.domain.entity.SingleDigit;
import org.wagner.single_digit.domain.entity.User;
import org.wagner.single_digit.infrastructure.repository.SingleDigitRepository;
import org.wagner.single_digit.presentation.dto.SingleDigitRequest;
import org.wagner.single_digit.presentation.dto.SingleDigitResponse;
import org.wagner.single_digit.presentation.mapper.SingleDigitMapper;

import java.util.List;

@Service("singleDigitServiceImpl")
@RequiredArgsConstructor
public class SingleDigitServiceImpl implements SingleDigitService {

    private final SingleDigitMapper mapper;
    private final SingleDigitRepository singleDigitRepository;
    private final UserService userService;

    public Integer calculate(SingleDigitRequest request) {
        SingleDigit entity = mapper.toEntity(request);

        return entity.getResult();
    }

    @Transactional
    public Integer calculate(SingleDigitRequest request, Integer userId) {
        Integer result = calculate(request);

        return persistWithResult(request, userId, result);
    }

    @Override
    public Integer persistWithResult(SingleDigitRequest request, Integer userId, Integer result) {
        User user = userService.findEntityById(userId);
        SingleDigit entity = mapper.toEntity(request, user, result);
        user.addSingleDigit(entity);
        singleDigitRepository.save(entity);

        return entity.getResult();
    }

    public List<SingleDigitResponse> findByUserId(Integer userId) {
        List<SingleDigit> singleDigitList = singleDigitRepository.findByUserId(userId);
        return mapper.toResponseList(singleDigitList);
    }

}
