package ru.practicum.review.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BaseReviewControllerTest {

    private final ObjectMapper objectMapper;

    private final MockMvc mockMvc;

    private final Random random = new Random();

    public BaseReviewControllerTest(ObjectMapper objectMapper, MockMvc mockMvc) {
        this.objectMapper = objectMapper;
        this.mockMvc = mockMvc;
    }

    protected UserDto createUser() {
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
    protected CategoryDto createCategory() {
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

    protected EventFullDto createEvent(long userId, long catId) {
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

    protected ReviewDto createReview(long userId, long eventId) {
        NewReviewDto review = NewReviewDto.builder()
                .event(eventId)
                .rating(random.nextInt(10))
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

    protected ReviewDto patchReview(long userId, long reviewId) {
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

    protected void deleteReview(long userId, long reviewId) {
        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .delete("/users/" + userId + "/reviews/" + reviewId))
                    .andExpect(status().isOk())
                    .andReturn();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected ReviewDto getReview(long userId, long reviewId) {
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

    protected List<ReviewDto> getReviews(long userId) {
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
