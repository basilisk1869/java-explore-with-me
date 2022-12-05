package ru.practicum.review.repository;

import ru.practicum.review.dto.ReviewDto;

import java.util.List;
import java.util.OptionalDouble;

public interface CustomReviewRepository {

    List<ReviewDto> getReviews(long eventId, Boolean positive, String text, int from, int size);

    OptionalDouble getEventRating(long eventId);

    OptionalDouble getInitiatorRating(long userId);

}
