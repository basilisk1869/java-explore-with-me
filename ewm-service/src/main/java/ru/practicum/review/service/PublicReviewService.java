package ru.practicum.review.service;

import org.springframework.stereotype.Service;
import ru.practicum.review.dto.ReviewDto;

import java.util.List;

@Service
public interface PublicReviewService {

    List<ReviewDto> getReviews(long eventId, Boolean positive, String text, int from, int size);

}
