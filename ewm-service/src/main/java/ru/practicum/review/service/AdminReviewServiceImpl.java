package ru.practicum.review.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.common.repository.CommonRepository;
import ru.practicum.review.dto.ReviewDto;
import ru.practicum.review.model.Review;
import ru.practicum.review.model.ReviewStatus;
import ru.practicum.review.repository.ReviewRepository;

import javax.validation.constraints.NotNull;

@Service
@RequiredArgsConstructor
public class AdminReviewServiceImpl implements AdminReviewService {

    @Autowired
    private final CommonRepository commonRepository;

    @Autowired
    private final ReviewRepository reviewRepository;

    @Autowired
    private final ModelMapper modelMapper;

    @Override
    public @NotNull ReviewDto setReviewStatus(long reviewId, @NotNull ReviewStatus reviewStatus) {
        Review review = commonRepository.getReview(reviewId);
        review.setStatus(reviewStatus);
        reviewRepository.save(review);
        return modelMapper.map(review, ReviewDto.class);
    }

}
