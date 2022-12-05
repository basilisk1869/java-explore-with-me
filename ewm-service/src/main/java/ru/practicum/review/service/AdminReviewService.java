package ru.practicum.review.service;

import ru.practicum.review.dto.ReviewDto;
import ru.practicum.review.model.ReviewStatus;

public interface AdminReviewService {

    ReviewDto setReviewStatus(long reviewId, ReviewStatus reviewStatus);

}
