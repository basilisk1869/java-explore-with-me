package ru.practicum.review.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.common.repository.CommonRepository;
import ru.practicum.event.model.Event;
import ru.practicum.review.dto.ReviewDto;
import ru.practicum.review.repository.ReviewRepository;

import java.util.List;

@Service
public class PublicReviewServiceImpl implements PublicReviewService {

    @Autowired
    CommonRepository commonRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<ReviewDto> getReviews(long eventId, Boolean positive, String text, int from, int size) {
        Event event = commonRepository.getEvent(eventId);
        return reviewRepository.getReviews(event, positive, text, from, size);
    }

}
