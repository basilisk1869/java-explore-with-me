package ru.practicum.review.repository;

import ru.practicum.event.model.Event;
import ru.practicum.review.dto.ReviewDto;

import java.util.List;

public interface CustomReviewRepository {

    List<ReviewDto> getReviews(Event event, Boolean positive, String text, int from, int size);

}
