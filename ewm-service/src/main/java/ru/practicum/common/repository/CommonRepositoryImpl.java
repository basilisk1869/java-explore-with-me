package ru.practicum.common.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.AccessDeniedException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.review.model.Review;
import ru.practicum.review.repository.ReviewRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.OptionalDouble;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommonRepositoryImpl implements CommonRepository {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final EventRepository eventRepository;

    @Autowired
    private final CompilationRepository compilationRepository;

    @Autowired
    private final CategoryRepository categoryRepository;

    @Autowired
    private final RequestRepository requestRepository;

    @Autowired
    private final ReviewRepository reviewRepository;

    @Autowired
    private final ModelMapper modelMapper;

    @Override
    public @NotNull Category getCategory(long catId) {
        return categoryRepository.findById(catId).orElseThrow(() -> {
            throw new NotFoundException("category is not found");
        });
    }

    @Override
    public @NotNull Compilation getCompilation(long compId) {
        return compilationRepository.findById(compId).orElseThrow(() -> {
            throw new NotFoundException("compilation is not found");
        });
    }

    @Override
    public @NotNull Event getEvent(long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> {
            throw new NotFoundException("event is not found");
        });
    }

    @Override
    public @NotNull Event getEventByUser(long userId, long eventId) {
        User initiator = getUser(userId);
        Event event = getEvent(eventId);
        if (Objects.equals(event.getInitiator(), initiator)) {
            return event;
        } else {
            throw new AccessDeniedException("user has no access for this event");
        }
    }

    @Override
    public @NotNull Review getReview(long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow(() -> {
            throw new NotFoundException("review is not found");
        });
    }

    @Override
    public @NotNull Review getReviewByUser(long userId, long reviewId) {
        User reviewer = getUser(userId);
        Review review = getReview(reviewId);
        if (Objects.equals(review.getReviewer(), reviewer)) {
            return review;
        } else {
            throw new AccessDeniedException("user has no access for this review");
        }
    }

    @Override
    public @NotNull User getUser(long userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            throw new NotFoundException("user is not found");
        });
    }

    @Override
    public @NotNull Request getRequest(long reqId) {
        return requestRepository.findById(reqId).orElseThrow(() -> {
            throw new NotFoundException("request is not found");
        });
    }

    @Override
    public long getConfirmedRequests(@NotNull Event event) {
        return event.getRequests() == null ? 0L : event.getRequests().stream()
                .filter(request -> Objects.equals(request.getStatus(), RequestStatus.CONFIRMED))
                .count();
    }

    @Override
    public @NotNull EventFullDto mapEventToFullDto(@NotNull Event event) {
        EventFullDto eventFullDto = modelMapper.map(event, EventFullDto.class);
        eventFullDto.setConfirmedRequests(getConfirmedRequests(event));
        eventFullDto.setViews(0L);
        // if rating shown
        if (event.getInitiator().getShowRating()) {
            // set event rating
            OptionalDouble eventRating = reviewRepository.getEventRating(event.getId());
            if (eventRating.isPresent()) {
                eventFullDto.setRating(eventRating.getAsDouble());
            } else {
                eventFullDto.setRating(null);
            }
            // set initiator rating
            OptionalDouble initiatorRating = reviewRepository.getInitiatorRating(event.getInitiator().getId());
            if (initiatorRating.isPresent()) {
                eventFullDto.getInitiator().setRating(initiatorRating.getAsDouble());
            } else {
                eventFullDto.getInitiator().setRating(null);
            }
        }
        return eventFullDto;
    }

}
