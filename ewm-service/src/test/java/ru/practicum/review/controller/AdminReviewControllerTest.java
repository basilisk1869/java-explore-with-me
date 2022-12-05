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
import ru.practicum.review.model.ReviewStatus;
import ru.practicum.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class AdminReviewControllerTest extends BaseReviewControllerTest {

    public AdminReviewControllerTest(@Autowired ObjectMapper objectMapper, @Autowired MockMvc mockMvc) {
        super(objectMapper, mockMvc);
    }

    @Test
    void confirmReview() {
        UserDto initiator = createUser();
        CategoryDto category = createCategory();
        EventFullDto event = createEvent(initiator.getId(), category.getId());
        event = publishEvent(event.getId());
        UserDto requester = createUser();
        ParticipationRequestDto request = createRequest(requester.getId(), event.getId());
        // check pending review
        ReviewDto review1 = createReview(requester.getId(), event.getId());
        ReviewDto review2 = getReview(requester.getId(), review1.getId());
        assertEquals(review1, review2);
        assertEquals(ReviewStatus.PENDING, review2.getStatus());
        // check confirmed review
        ReviewDto review3 = confirmReview(review2.getId());
        ReviewDto review4 = getReview(requester.getId(), review1.getId());
        assertEquals(review3, review4);
        assertEquals(ReviewStatus.CONFIRMED, review4.getStatus());
    }

    @Test
    void rejectReview() {
        UserDto initiator = createUser();
        CategoryDto category = createCategory();
        EventFullDto event = createEvent(initiator.getId(), category.getId());
        event = publishEvent(event.getId());
        UserDto requester = createUser();
        ParticipationRequestDto request = createRequest(requester.getId(), event.getId());
        // check pending review
        ReviewDto review1 = createReview(requester.getId(), event.getId());
        ReviewDto review2 = getReview(requester.getId(), review1.getId());
        assertEquals(review1, review2);
        assertEquals(ReviewStatus.PENDING, review2.getStatus());
        // check confirmed review
        ReviewDto review3 = rejectReview(review2.getId());
        ReviewDto review4 = getReview(requester.getId(), review1.getId());
        assertEquals(review3, review4);
        assertEquals(ReviewStatus.REJECTED, review4.getStatus());
    }

}
