package ru.practicum.review.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.review.dto.NewReviewDto;
import ru.practicum.review.dto.ReviewDto;
import ru.practicum.review.dto.UpdateReviewDto;
import ru.practicum.review.repository.ReviewRepository;
import ru.practicum.review.service.UserReviewService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/reviews")
@RequiredArgsConstructor
@Slf4j
public class UserReviewController {
    private final ReviewRepository reviewRepository;

    @Autowired
    private final UserReviewService userReviewService;

    @GetMapping
    List<ReviewDto> getReviews(@PathVariable long userId) {
        log.info("getReviews by user " + userId);
        List<ReviewDto> reviews = userReviewService.getReviews(userId);
        log.info("getReviews returned " + reviews);
        return reviews;
    }

    @PostMapping
    ReviewDto postReview(@PathVariable long userId,
                         @RequestBody @Valid NewReviewDto newReviewDto) {
        log.info("postReview " + userId + " " + newReviewDto);
        ReviewDto review = userReviewService.postReview(userId, newReviewDto);
        log.info("postReview returned " + review);
        return review;
    }

    @PatchMapping("/{reviewId}")
    ReviewDto patchReview(@PathVariable long userId,
                          @PathVariable long reviewId,
                          @RequestBody @Valid UpdateReviewDto updateReviewDto) {
        log.info("patchReview " + userId + " " + reviewId + " " + updateReviewDto);
        ReviewDto review = userReviewService.patchReview(userId, reviewId, updateReviewDto);
        log.info("patchReview returned " + review);
        return review;
    }

    @GetMapping("/{reviewId}")
    ReviewDto getReviews(@PathVariable long userId, @PathVariable long reviewId) {
        log.info("getReview " + userId + " " + reviewId);
        ReviewDto review = userReviewService.getReview(userId, reviewId);
        log.info("getReview returned " + review);
        return review;
    }

    @DeleteMapping("/{reviewId}")
    void deleteReview(@PathVariable long userId, @PathVariable long reviewId) {
        log.info("deleteReview " + reviewId);
        userReviewService.deleteReview(userId, reviewId);
    }

}
