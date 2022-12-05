package ru.practicum.review.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.common.repository.CommonRepository;
import ru.practicum.event.model.Event;
import ru.practicum.exception.AccessDeniedException;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.review.dto.NewReviewDto;
import ru.practicum.review.dto.ReviewDto;
import ru.practicum.review.dto.UpdateReviewDto;
import ru.practicum.review.model.Review;
import ru.practicum.review.model.ReviewStatus;
import ru.practicum.review.repository.ReviewRepository;
import ru.practicum.user.model.User;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserReviewServiceImpl implements UserReviewService {

    @Autowired
    CommonRepository commonRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    RequestRepository requestRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<ReviewDto> getReviews(long userId) {
        User reviewer = commonRepository.getUser(userId);
        return reviewRepository.findAllByReviewer(reviewer).stream()
                .map(review -> modelMapper.map(review, ReviewDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public ReviewDto postReview(long userId, NewReviewDto newReviewDto) {
        User reviewer = commonRepository.getUser(userId);
        Event event = commonRepository.getEvent(newReviewDto.getEvent());
        // check reviewer is not initiator
        if (Objects.equals(reviewer, event.getInitiator())) {
            throw new AccessDeniedException("initiator cannot make review of own event");
        }
        // check reviewer participation
        Optional<Request> request = requestRepository.findByRequesterAndEvent(reviewer, event);
        if (request.isEmpty() || !Objects.equals(request.get().getStatus(), RequestStatus.CONFIRMED)) {
            throw new AccessDeniedException("user is not participated in this event");
        }
        // check if review is already exists
        if (reviewRepository.findByEventAndReviewer(event, reviewer).isPresent()) {
            throw new AccessDeniedException("user is reviewed this event already");
        }
        // create review
        Review review = modelMapper.map(newReviewDto, Review.class);
        if (review.getText() == null) {
            review.setStatus(ReviewStatus.CONFIRMED);
        }
        review.setReviewer(reviewer);
        review.setEvent(event);
        reviewRepository.save(review);
        return modelMapper.map(review, ReviewDto.class);
    }

    @Override
    public ReviewDto patchReview(long userId, long reviewId, UpdateReviewDto updateReviewDto) {
        Review review = commonRepository.getReviewByUser(userId, reviewId);
        modelMapper.map(updateReviewDto, review);
        reviewRepository.save(review);
        return modelMapper.map(review, ReviewDto.class);
    }

    @Override
    public ReviewDto getReview(long userId, long reviewId) {
        Review review = commonRepository.getReviewByUser(userId, reviewId);
        return modelMapper.map(review, ReviewDto.class);
    }

    @Override
    public void deleteReview(long userId, long reviewId) {
        Review review = commonRepository.getReviewByUser(userId, reviewId);
        reviewRepository.delete(review);
    }

}
