package ru.practicum.review.service;

import io.micrometer.core.lang.Nullable;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.common.repository.CommonRepository;
import ru.practicum.review.dto.ReviewDto;
import ru.practicum.review.repository.ReviewRepository;

import javax.validation.constraints.NotNull;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicReviewServiceImpl implements PublicReviewService {

    @Autowired
    private final CommonRepository commonRepository;

    @Autowired
    private final ReviewRepository reviewRepository;

    @Autowired
    private final ModelMapper modelMapper;

    @Override
    public @NotNull List<ReviewDto> getReviews(long eventId,
                                               @Nullable Boolean positive,
                                               @Nullable String text,
                                               int from,
                                               int size) {
        commonRepository.getEvent(eventId);
        return reviewRepository.getReviews(eventId, positive, text, from, size);
    }

}
