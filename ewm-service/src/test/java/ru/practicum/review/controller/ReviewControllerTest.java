package ru.practicum.review.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.review.dto.NewReviewDto;
import ru.practicum.review.dto.ReviewDto;
import ru.practicum.review.dto.UpdateReviewDto;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class ReviewControllerTest {

    private final ObjectMapper objectMapper;

    private final MockMvc mockMvc;

    private final Random random = new Random();

    public ReviewControllerTest(@Autowired ObjectMapper objectMapper, @Autowired MockMvc mockMvc) {
        this.objectMapper = objectMapper;
        this.mockMvc = mockMvc;
    }

    @Test
    void postReview() {
        UserDto user = createUser();
        CategoryDto category = createCategory();
        EventFullDto event = createEvent(user.getId(), category.getId());
        createReview(user.getId(), event.getId());
    }

    @Test
    void deleteReview() {
        UserDto user = createUser();
        CategoryDto category = createCategory();
        EventFullDto event = createEvent(user.getId(), category.getId());
        ReviewDto review = createReview(user.getId(), event.getId());
        deleteReview(user.getId(), review.getId());
    }

    @Test
    void getReview() {
        UserDto user = createUser();
        CategoryDto category = createCategory();
        EventFullDto event = createEvent(user.getId(), category.getId());
        ReviewDto review1 = createReview(user.getId(), event.getId());
        ReviewDto review2 = getReview(event.getId(), review1.getId());
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

    private UserDto createUser() {
        NewUserRequest user = NewUserRequest.builder()
                .name("User " + UUID.randomUUID())
                .email(UUID.randomUUID() + "@yandex.ru")
                .build();
        try {
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                            .post("/admin/users")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(user)))
                    .andExpect(status().isOk())
                    .andReturn();
            return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), UserDto.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private CategoryDto createCategory() {
        NewCategoryDto category = NewCategoryDto.builder()
                .name("Category " + UUID.randomUUID())
                .build();
        try {
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                            .post("/admin/categories")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(category)))
                    .andExpect(status().isOk())
                    .andReturn();
            return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CategoryDto.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private EventFullDto createEvent(long userId, long catId) {
        NewEventDto event = NewEventDto.builder()
                .annotation("It's a very interesting and amazing idea!")
                .category(catId)
                .description("It's a crazy event! Very good nice!!!")
                .eventDate(LocalDateTime.now().plusDays(1))
                .location(new LocationDto((float)60, (float)30))
                .title("Crazy event " + UUID.randomUUID())
                .build();
        try {
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                            .post("/users/" + userId + "/events")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(event)))
                    .andExpect(status().isOk())
                    .andReturn();
            return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), EventFullDto.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ReviewDto createReview(long userId, long eventId) {
        NewReviewDto review = NewReviewDto.builder()
                .event(eventId)
                .rating(random.nextInt(20) - 10)
                .text("Some random review text... " + UUID.randomUUID())
                .build();
        try {
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                            .post("/users/" + userId + "/reviews")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(review)))
                    .andExpect(status().isOk())
                    .andReturn();
            return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ReviewDto.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ReviewDto patchReview(long userId, long reviewId) {
        UpdateReviewDto review = UpdateReviewDto.builder()
                .text("Some random updated text... " + UUID.randomUUID())
                .build();
        try {
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                            .patch("/users/" + userId + "/reviews/" + reviewId)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(review)))
                    .andExpect(status().isOk())
                    .andReturn();
            return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ReviewDto.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteReview(long userId, long reviewId) {
        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .delete("/users/" + userId + "/reviews/" + reviewId))
                    .andExpect(status().isOk())
                    .andReturn();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ReviewDto getReview(long userId, long reviewId) {
        try {
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                            .get("/users/" + userId + "/reviews/" + reviewId)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();
            return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ReviewDto.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<ReviewDto> getReviews(long userId) {
        try {
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                            .get("/users/" + userId + "/reviews")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();
            return objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                    new TypeReference<List<ReviewDto>>(){});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
