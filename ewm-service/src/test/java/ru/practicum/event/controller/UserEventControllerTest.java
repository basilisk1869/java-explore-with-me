package ru.practicum.event.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.review.controller.BaseReviewControllerTest;
import ru.practicum.review.dto.ReviewDto;
import ru.practicum.review.service.UserReviewService;
import ru.practicum.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@SpyBean(UserReviewService.class)
public class UserEventControllerTest extends BaseReviewControllerTest {

    public UserEventControllerTest(@Autowired ObjectMapper objectMapper,
                                   @Autowired MockMvc mockMvc,
                                   @Autowired UserReviewService userReviewService) {
        super(objectMapper, mockMvc, userReviewService);
    }

    @Test
    void getEventRating() {
        UserDto initiator = createUser();
        CategoryDto category = createCategory();
        EventFullDto event = createEvent(initiator.getId(), category.getId());
        event = publishEvent(event.getId());
        List<ReviewDto> reviews = new ArrayList<>();
        // prepare event reviews
        for (int i = 0; i < 10; i++) {
            UserDto requester = createUser();
            ParticipationRequestDto request = createRequest(requester.getId(), event.getId());
            ReviewDto review = createReview(requester.getId(), event);
            confirmReview(review.getId());
            reviews.add(review);
        }
        // check that event has null rating
        event = getEvent(initiator.getId(), event.getId());
        assertNull(event.getRating());
        // show user rating and check again
        showRating(initiator.getId(), true);
        event = getEvent(initiator.getId(), event.getId());
        assertNotNull(event.getRating());
        assertEquals(reviews.stream().mapToDouble(ReviewDto::getRating).average().getAsDouble(), event.getRating());
    }

    @Test
    void getInitiatorRating() {
        UserDto initiator = createUser();
        CategoryDto category = createCategory();
        // make event1
        EventFullDto event1 = createEvent(initiator.getId(), category.getId());
        event1 = publishEvent(event1.getId());
        // make event2
        EventFullDto event2 = createEvent(initiator.getId(), category.getId());
        event2 = publishEvent(event2.getId());
        // prepare event reviews
        List<ReviewDto> reviews = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            UserDto requester = createUser();
            // create review for event1
            ParticipationRequestDto request1 = createRequest(requester.getId(), event1.getId());
            ReviewDto review1 = createReview(requester.getId(), event1);
            confirmReview(review1.getId());
            reviews.add(review1);
            // create review for event2
            ParticipationRequestDto request2 = createRequest(requester.getId(), event2.getId());
            ReviewDto review2 = createReview(requester.getId(), event2);
            confirmReview(review2.getId());
            reviews.add(review2);
        }
        // check that event initiator has null rating
        event1 = getEvent(initiator.getId(), event1.getId());
        assertNull(event1.getInitiator().getRating());
        // show user rating and check again
        showRating(initiator.getId(), true);
        event1 = getEvent(initiator.getId(), event1.getId());
        assertNotNull(event1.getInitiator().getRating());
        assertEquals(reviews.stream().mapToDouble(ReviewDto::getRating).average().getAsDouble(),
                event1.getInitiator().getRating());
    }

}
