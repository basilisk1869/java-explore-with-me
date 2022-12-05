package ru.practicum.review.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.review.dto.ReviewDto;
import ru.practicum.review.service.AdminReviewService;

@RestController
@RequestMapping(path = "/admin/reviews")
@RequiredArgsConstructor
@Slf4j
public class AdminReviewController {

    @Autowired
    AdminReviewService adminReviewService;

    @PatchMapping("/{reviewId}")
    ReviewDto patchReviewStatus(@PathVariable long reviewId,
                                @RequestParam String status) {
        ReviewDto review = adminReviewService.setReviewStatus(reviewId, status);
        log.info("patchReviewStatus " + reviewId + " " + status + " " + review);
        return review;
    }

}
