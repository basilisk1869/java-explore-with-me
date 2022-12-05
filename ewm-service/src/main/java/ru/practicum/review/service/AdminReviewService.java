package ru.practicum.review.service;

import ru.practicum.review.dto.ReviewDto;

public interface AdminReviewService {

    ReviewDto setReviewStatus(long reviewId, String status);

}
