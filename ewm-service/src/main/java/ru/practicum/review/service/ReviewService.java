package ru.practicum.review.service;

import org.springframework.stereotype.Service;
import ru.practicum.review.dto.NewReviewDto;
import ru.practicum.review.dto.ReviewDto;
import ru.practicum.review.dto.UpdateReviewDto;

import java.util.List;

@Service
public interface ReviewService {

    List<ReviewDto> getReviews(long eventId, Boolean positive, String text, int from, int size);

    List<ReviewDto> getReviews(long userId);

    ReviewDto postReview(long userId, NewReviewDto newReviewDto);

    ReviewDto patchReview(long userId, long reviewId, UpdateReviewDto updateReviewDto);

    ReviewDto getReview(long userId, long reviewId);

    void deleteReview(long userId, long reviewId);



}
