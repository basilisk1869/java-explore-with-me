package ru.practicum.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.review.controller.BaseReviewControllerTest;
import ru.practicum.review.service.UserReviewService;
import ru.practicum.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
public class UserUserControllerTest extends BaseReviewControllerTest {

    public UserUserControllerTest(@Autowired ObjectMapper objectMapper,
                                  @Autowired MockMvc mockMvc,
                                  @Autowired UserReviewService userReviewService) {
        super(objectMapper, mockMvc, userReviewService);
    }

    @Test
    void showRating() {
        UserDto user = createUser();
        user = getUser(user.getId());
        assertFalse(user.getShowRating());
        showRating(user.getId(), true);
        user = getUser(user.getId());
        assertTrue(user.getShowRating());
    }

}
