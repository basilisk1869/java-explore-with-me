package ru.practicum.review.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.review.dto.ReviewDto;
import ru.practicum.review.service.UserReviewService;
import ru.practicum.user.dto.UserDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@SpyBean(UserReviewService.class)
public class UserReviewControllerTest extends BaseReviewControllerTest {

    public UserReviewControllerTest(@Autowired ObjectMapper objectMapper,
                                    @Autowired MockMvc mockMvc,
                                    @Autowired UserReviewService userReviewService) {
        super(objectMapper, mockMvc, userReviewService);
    }

    @Test
    void postReview() {
        UserDto initiator = createUser();
        CategoryDto category = createCategory();
        EventFullDto event = createEvent(initiator.getId(), category.getId());
        event = publishEvent(event.getId());
        UserDto requester = createUser();
        ParticipationRequestDto request = createRequest(requester.getId(), event.getId());
        createReview(requester.getId(), event);
    }

    @Test
    void deleteReview() {
        UserDto initiator = createUser();
        CategoryDto category = createCategory();
        EventFullDto event = createEvent(initiator.getId(), category.getId());
        event = publishEvent(event.getId());
        UserDto requester = createUser();
        ParticipationRequestDto request = createRequest(requester.getId(), event.getId());
        ReviewDto review = createReview(requester.getId(), event);
        deleteReview(requester.getId(), review.getId());
    }

    @Test
    void getReview() {
        UserDto initiator = createUser();
        CategoryDto category = createCategory();
        EventFullDto event = createEvent(initiator.getId(), category.getId());
        event = publishEvent(event.getId());
        UserDto requester = createUser();
        ParticipationRequestDto request = createRequest(requester.getId(), event.getId());
        ReviewDto review1 = createReview(requester.getId(), event);
        ReviewDto review2 = getReview(requester.getId(), review1.getId());
        assertEquals(review1, review2);
    }

    @Test
    void getReviewsByUser() {
        UserDto initiator = createUser();
        CategoryDto category = createCategory();
        UserDto requester = createUser();
        // make review1 for event1
        EventFullDto event1 = createEvent(initiator.getId(), category.getId());
        event1 = publishEvent(event1.getId());
        ParticipationRequestDto request1 = createRequest(requester.getId(), event1.getId());
        ReviewDto review1 = createReview(requester.getId(), event1);
        // make review2 for event2
        EventFullDto event2 = createEvent(initiator.getId(), category.getId());
        event2 = publishEvent(event2.getId());
        ParticipationRequestDto request2 = createRequest(requester.getId(), event2.getId());
        ReviewDto review2 = createReview(requester.getId(), event2);
        // get requester reviews
        List<ReviewDto> reviews = getReviews(requester.getId());
        assertEquals(List.of(review1, review2), reviews);
    }

    @Test
    void patchReview() {
        UserDto initiator = createUser();
        CategoryDto category = createCategory();
        EventFullDto event = createEvent(initiator.getId(), category.getId());
        event = publishEvent(event.getId());
        UserDto requester = createUser();
        ParticipationRequestDto request = createRequest(requester.getId(), event.getId());
        ReviewDto review1 = createReview(requester.getId(), event);
        ReviewDto review2 = getReview(requester.getId(), review1.getId());
        assertEquals(review1, review2);
        ReviewDto review3 = patchReview(requester.getId(), review1.getId());
        ReviewDto review4 = getReview(requester.getId(), review1.getId());
        assertEquals(review3, review4);
    }

}
