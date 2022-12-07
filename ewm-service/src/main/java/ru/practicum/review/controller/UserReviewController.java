package ru.practicum.review.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.review.dto.NewReviewDto;
import ru.practicum.review.dto.ReviewDto;
import ru.practicum.review.dto.UpdateReviewDto;
import ru.practicum.review.service.UserReviewService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/reviews")
@RequiredArgsConstructor
@Slf4j
public class UserReviewController {

    @Autowired
    private final UserReviewService userReviewService;

    @GetMapping
    List<ReviewDto> getReviews(@PathVariable long userId) {
        List<ReviewDto> reviews = userReviewService.getReviews(userId);
        log.info("getReviews by user " + userId + " " + reviews);
        return reviews;
    }

    @PostMapping
    ReviewDto postReview(@PathVariable long userId,
                         @RequestBody @Valid NewReviewDto newReviewDto) {
        log.info("postReview " + userId + " " + newReviewDto);
        return userReviewService.postReview(userId, newReviewDto);
    }

    @PatchMapping("/{reviewId}")
    ReviewDto patchReview(@PathVariable long userId,
                          @PathVariable long reviewId,
                          @RequestBody @Valid UpdateReviewDto updateReviewDto) {
        log.info("patchReview " + userId + " " + reviewId + " " + updateReviewDto);
        return userReviewService.patchReview(userId, reviewId, updateReviewDto);
    }

    @GetMapping("/{reviewId}")
    ReviewDto getReviews(@PathVariable long userId, @PathVariable long reviewId) {
        ReviewDto review = userReviewService.getReview(userId, reviewId);
        log.info("getReview " + userId + " " + reviewId + " " + review);
        return review;
    }

    @DeleteMapping("/{reviewId}")
    void deleteReview(@PathVariable long userId, @PathVariable long reviewId) {
        log.info("deleteReview " + reviewId);
        userReviewService.deleteReview(userId, reviewId);
    }

}
