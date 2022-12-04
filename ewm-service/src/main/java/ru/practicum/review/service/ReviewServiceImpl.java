package ru.practicum.review.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.common.repository.CommonRepository;
import ru.practicum.event.model.Event;
import ru.practicum.review.dto.NewReviewDto;
import ru.practicum.review.dto.ReviewDto;
import ru.practicum.review.dto.UpdateReviewDto;
import ru.practicum.review.model.Review;
import ru.practicum.review.repository.ReviewRepository;
import ru.practicum.user.model.User;

import java.util.List;

public class ReviewServiceImpl implements ReviewService {

    @Autowired
    CommonRepository commonRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<ReviewDto> getReviews(long eventId, Boolean positive, String text, int from, int size) {
        Event event = commonRepository.getEvent(eventId);
        return reviewRepository.getReviews(event, positive, text, from, size);
    }

    @Override
    public ReviewDto getReview(long eventId, long reviewId) {
        return null;
    }

    @Override
    public ReviewDto postReview(long userId, NewReviewDto newReviewDto) {
        User reviewer = commonRepository.getUser(userId);
        Review review = modelMapper.map(newReviewDto, Review.class);
        review.setReviewer(reviewer);
        reviewRepository.save(review);
        return modelMapper.map(review, ReviewDto.class);
    }

    @Override
    public ReviewDto patchReview(long userId, long reviewId, UpdateReviewDto updateReviewDto) {

        return null;
    }

    @Override
    public void deleteReview(long userId, long reviewId) {

    }

}
