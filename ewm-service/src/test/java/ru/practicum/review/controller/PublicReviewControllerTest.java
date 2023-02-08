package ru.practicum.review.controller;

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
import ru.practicum.review.dto.ReviewDto;
import ru.practicum.review.model.ReviewStatus;
import ru.practicum.review.service.UserReviewService;
import ru.practicum.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
@SpyBean(UserReviewService.class)
public class PublicReviewControllerTest extends BaseReviewControllerTest {

    public PublicReviewControllerTest(@Autowired ObjectMapper objectMapper,
                                      @Autowired MockMvc mockMvc,
                                      @Autowired UserReviewService userReviewService) {
        super(objectMapper, mockMvc, userReviewService);
    }

    @Test
    void getReviews() {
        UserDto initiator = createUser();
        CategoryDto category = createCategory();
        EventFullDto event = createEvent(initiator.getId(), category.getId());
        event = publishEvent(event.getId());
        List<ReviewDto> expectedReviews = new ArrayList<>();
        // prepare pending reviews
        for (int i = 0; i < 10; i++) {
            UserDto requester = createUser();
            ParticipationRequestDto request = createRequest(requester.getId(), event.getId());
            ReviewDto review = createReview(requester.getId(), event);
            expectedReviews.add(review);
        }
        // should not any confirmed reviews
        List<ReviewDto> actualReviews = getReviews(event.getId(), null, null);
        assertEquals(List.of(), actualReviews);
        // confirm reviews and check again
        expectedReviews.forEach(review -> {
            confirmReview(review.getId());
            review.setStatus(ReviewStatus.CONFIRMED);
        });
        actualReviews = getReviews(event.getId(), null, null);
        assertEquals(expectedReviews, actualReviews);
        // get only positive reviews
        actualReviews = getReviews(event.getId(), true, null);
        assertEquals(expectedReviews.stream()
                        .filter(review -> review.getRating() > 5)
                        .collect(Collectors.toList()),
                actualReviews);
        // get only negative reviews
        actualReviews = getReviews(event.getId(), false, null);
        assertEquals(expectedReviews.stream()
                        .filter(review -> review.getRating() < 5)
                        .collect(Collectors.toList()),
                actualReviews);
        // search first review by text
        actualReviews = getReviews(event.getId(), null, expectedReviews.get(0).getReviewer().getName());
        assertEquals(List.of(expectedReviews.get(0)), actualReviews);
    }

}
