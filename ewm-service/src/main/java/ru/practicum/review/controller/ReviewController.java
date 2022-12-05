package ru.practicum.review.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.review.dto.NewReviewDto;
import ru.practicum.review.dto.ReviewDto;
import ru.practicum.review.dto.UpdateReviewDto;
import ru.practicum.review.service.ReviewService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ReviewController {

    @Autowired
    ReviewService reviewService;

    @GetMapping("/events/{eventId}/reviews")
    List<ReviewDto> getReviews(@PathVariable Long eventId,
                               @RequestParam(required = false) Boolean positive,
                               @RequestParam(required = false) String text,
                               @RequestParam(required = false, defaultValue = "0") Integer from,
                               @RequestParam(required = false, defaultValue = "10") Integer size) {
        List<ReviewDto> reviews = reviewService.getReviews(eventId, positive, text, from, size);
        log.info("getReviews by event " + eventId + " " + reviews);
        return reviews;
    }

    @GetMapping("/users/{userId}/reviews")
    List<ReviewDto> getReviews(@PathVariable long userId) {
        List<ReviewDto> reviews = reviewService.getReviews(userId);
        log.info("getReviews by user " + userId + " " + reviews);
        return reviews;
    }

    @PostMapping("/users/{userId}/reviews")
    ReviewDto postReview(@PathVariable long userId,
                         @RequestBody @Valid NewReviewDto newReviewDto) {
        log.info("postReview " + userId + " " + newReviewDto);
        return reviewService.postReview(userId, newReviewDto);
    }

    @PatchMapping("/users/{userId}/reviews/{reviewId}")
    ReviewDto patchReview(@PathVariable long userId,
                          @PathVariable long reviewId,
                          @RequestBody @Valid UpdateReviewDto updateReviewDto) {
        log.info("patchReview " + userId + " " + reviewId + " " + updateReviewDto);
        return reviewService.patchReview(userId, reviewId, updateReviewDto);
    }

    @GetMapping("/users/{userId}/reviews/{reviewId}")
    ReviewDto getReviews(@PathVariable long userId, @PathVariable long reviewId) {
        ReviewDto review = reviewService.getReview(userId, reviewId);
        log.info("getReview " + userId + " " + reviewId + " " + review);
        return review;
    }

    @DeleteMapping("/users/{userId}/reviews/{reviewId}")
    void deleteReview(@PathVariable long userId, @PathVariable long reviewId) {
        log.info("deleteReview " + reviewId);
        reviewService.deleteReview(userId, reviewId);
    }

}
