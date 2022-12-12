package ru.practicum.review.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final UserReviewService userReviewService;

    private final ObjectMapper objectMapper;

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
        try {
            log.info("postReview " + userId + " " + objectMapper.writeValueAsString(newReviewDto));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        ReviewDto review = userReviewService.postReview(userId, newReviewDto);
        log.info("postReview returned " + review);
        return review;
    }

    @PatchMapping("/{reviewId}")
    ReviewDto patchReview(@PathVariable long userId,
                          @PathVariable long reviewId,
                          @RequestBody @Valid UpdateReviewDto updateReviewDto) {
        try {
            log.info("patchReview " + userId + " " + reviewId + " " + objectMapper.writeValueAsString(updateReviewDto));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
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
