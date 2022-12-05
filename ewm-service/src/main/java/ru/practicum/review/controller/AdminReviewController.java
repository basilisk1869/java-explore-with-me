package ru.practicum.review.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.review.dto.ReviewDto;
import ru.practicum.review.model.ReviewStatus;
import ru.practicum.review.service.AdminReviewService;

@RestController
@RequestMapping(path = "/admin/reviews")
@RequiredArgsConstructor
@Slf4j
public class AdminReviewController {

    @Autowired
    AdminReviewService adminReviewService;

    @PatchMapping("/{reviewId}/confirm")
    ReviewDto patchReviewAsConfirmed(@PathVariable long reviewId) {
        ReviewDto review = adminReviewService.setReviewStatus(reviewId, ReviewStatus.CONFIRMED);
        log.info("patchReviewAsConfirmed " + reviewId + " " + review);
        return review;
    }

    @PatchMapping("/{reviewId}/reject")
    ReviewDto patchReviewAsRejected(@PathVariable long reviewId) {
        ReviewDto review = adminReviewService.setReviewStatus(reviewId, ReviewStatus.REJECTED);
        log.info("patchReviewAsConfirmed " + reviewId + " " + review);
        return review;
    }

}