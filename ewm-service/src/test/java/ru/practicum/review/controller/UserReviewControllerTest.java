package ru.practicum.review.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.review.dto.ReviewDto;
import ru.practicum.user.dto.UserDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class UserReviewControllerTest extends BaseReviewControllerTest {

    public UserReviewControllerTest(@Autowired ObjectMapper objectMapper, @Autowired MockMvc mockMvc) {
        super(objectMapper, mockMvc);
    }

    @Test
    void postReview() {
        UserDto initiator = createUser();
        CategoryDto category = createCategory();
        EventFullDto event = createEvent(initiator.getId(), category.getId());
        event = publishEvent(event.getId());
        UserDto requester = createUser();
        ParticipationRequestDto request = createRequest(requester.getId(), event.getId());
        createReview(requester.getId(), event.getId());
    }

    @Test
    void deleteReview() {
        UserDto initiator = createUser();
        CategoryDto category = createCategory();
        EventFullDto event = createEvent(initiator.getId(), category.getId());
        event = publishEvent(event.getId());
        UserDto requester = createUser();
        ParticipationRequestDto request = createRequest(requester.getId(), event.getId());
        ReviewDto review = createReview(requester.getId(), event.getId());
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
        ReviewDto review1 = createReview(requester.getId(), event.getId());
        ReviewDto review2 = getReview(requester.getId(), review1.getId());
        assertEquals(review1, review2);
    }

    @Test
    void getReviewsByUser() {
        UserDto user = createUser();
        CategoryDto category = createCategory();
        EventFullDto event = createEvent(user.getId(), category.getId());
        ReviewDto review1 = createReview(user.getId(), event.getId());
        ReviewDto review2 = createReview(user.getId(), event.getId());
        List<ReviewDto> reviews = getReviews(user.getId());
        assertEquals(List.of(review1, review2), reviews);
    }

    @Test
    void patchReview() {
        UserDto user = createUser();
        CategoryDto category = createCategory();
        EventFullDto event = createEvent(user.getId(), category.getId());
        ReviewDto review1 = createReview(user.getId(), event.getId());
        ReviewDto review2 = getReview(event.getId(), review1.getId());
        assertEquals(review1, review2);
        ReviewDto review3 = patchReview(user.getId(), review1.getId());
        ReviewDto review4 = getReview(event.getId(), review1.getId());
        assertEquals(review3, review4);
    }

}
