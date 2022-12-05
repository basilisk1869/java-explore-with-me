package ru.practicum.review.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.review.dto.ReviewDto;
import ru.practicum.review.service.PublicReviewService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PublicReviewController {

    @Autowired
    PublicReviewService publicReviewService;

    @GetMapping("/events/{eventId}/reviews")
    List<ReviewDto> getReviews(@PathVariable Long eventId,
                               @RequestParam(required = false) Boolean positive,
                               @RequestParam(required = false) String text,
                               @RequestParam(required = false, defaultValue = "0") Integer from,
                               @RequestParam(required = false, defaultValue = "10") Integer size) {
        List<ReviewDto> reviews = publicReviewService.getReviews(eventId, positive, text, from, size);
        log.info("getReviews by event " + eventId + " " + reviews);
        return reviews;
    }

}
