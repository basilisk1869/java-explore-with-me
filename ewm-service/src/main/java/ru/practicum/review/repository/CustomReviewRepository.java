package ru.practicum.review.repository;

import ru.practicum.event.model.Event;
import ru.practicum.review.dto.ReviewDto;
import ru.practicum.user.model.User;

import java.util.List;
import java.util.OptionalDouble;

public interface CustomReviewRepository {

    List<ReviewDto> getReviews(Event event, Boolean positive, String text, int from, int size);

    OptionalDouble eventRating(Event event);

    OptionalDouble initiatorRating(User initiator);

}
