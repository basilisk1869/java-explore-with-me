package ru.practicum.review.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.review.dto.ReviewDto;
import ru.practicum.review.model.ReviewStatus;
import ru.practicum.review.service.AdminReviewService;

@RestController
@RequestMapping(path = "/admin/reviews")
@RequiredArgsConstructor
@Slf4j
public class AdminReviewController {

    private final AdminReviewService adminReviewService;

    @PatchMapping("/{reviewId}/confirm")
    ReviewDto patchReviewAsConfirmed(@PathVariable long reviewId) {
        log.info("patchReviewAsConfirmed " + reviewId);
        ReviewDto review = adminReviewService.setReviewStatus(reviewId, ReviewStatus.CONFIRMED);
        log.info("patchReviewAsConfirmed returned " + review);
        return review;
    }

    @PatchMapping("/{reviewId}/reject")
    ReviewDto patchReviewAsRejected(@PathVariable long reviewId) {
        log.info("patchReviewAsRejected " + reviewId);
        ReviewDto review = adminReviewService.setReviewStatus(reviewId, ReviewStatus.REJECTED);
        log.info("patchReviewAsRejected returned " + review);
        return review;
    }

}
