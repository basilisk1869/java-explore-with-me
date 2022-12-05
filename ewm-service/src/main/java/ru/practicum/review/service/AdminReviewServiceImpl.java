package ru.practicum.review.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.common.repository.CommonRepository;
import ru.practicum.review.dto.ReviewDto;
import ru.practicum.review.model.Review;
import ru.practicum.review.model.ReviewStatus;
import ru.practicum.review.repository.ReviewRepository;

@Service
public class AdminReviewServiceImpl implements AdminReviewService {

    @Autowired
    CommonRepository commonRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public ReviewDto setReviewStatus(long reviewId, String status) {
        Review review = commonRepository.getReview(reviewId);
        ReviewStatus reviewStatus = ReviewStatus.valueOf(status);
        review.setStatus(reviewStatus);
        reviewRepository.save(review);
        return modelMapper.map(review, ReviewDto.class);
    }

}
