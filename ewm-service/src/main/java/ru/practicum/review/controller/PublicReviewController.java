package ru.practicum.review.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.review.dto.ReviewDto;
import ru.practicum.review.service.PublicReviewService;

import java.util.List;

@RestController
@RequestMapping(path = "/events/{eventId}/reviews")
@RequiredArgsConstructor
@Slf4j
public class PublicReviewController {

    @Autowired
    PublicReviewService publicReviewService;

    @GetMapping
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