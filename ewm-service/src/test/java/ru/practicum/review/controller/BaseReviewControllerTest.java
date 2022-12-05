package ru.practicum.review.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.request.dto.ParticipationRequestDto;
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

    protected UserDto getUser(long userId) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.addAll("ids", List.of(String.valueOf(userId)));
        try {
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                            .get("/admin/users")
                            .accept(MediaType.APPLICATION_JSON)
                            .queryParams(params))
                    .andExpect(status().isOk())
                    .andReturn();
            List<UserDto> users = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                    new TypeReference<List<UserDto>>(){});
            if (users.size() > 0) {
                return users.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected UserDto showRating(long userId, boolean showRating) {
        try {
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                            .patch("/users/" + userId)
                            .accept(MediaType.APPLICATION_JSON)
                            .queryParam("showRating", String.valueOf(showRating)))
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

    protected EventFullDto getEvent(long userId, long eventId) {
        try {
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                            .get("/users/" + userId + "/events/" + eventId)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();
            return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), EventFullDto.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected EventFullDto publishEvent(long eventId) {
        try {
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                            .patch("/admin/events/" + eventId + "/publish")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();
            return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), EventFullDto.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected ParticipationRequestDto createRequest(long userId, long eventId) {
        ParticipationRequestDto request = ParticipationRequestDto.builder()
                .requester(userId)
                .event(eventId)
                .build();
        try {
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                            .post("/users/" + userId + "/requests")
                            .queryParam("eventId", String.valueOf(eventId))
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andReturn();
            return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ParticipationRequestDto.class);
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

    protected List<ReviewDto> getReviews(long eventId, Boolean positive, String text) {
        try {
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/events/" + eventId + "/reviews")
                    .accept(MediaType.APPLICATION_JSON);
            if (positive != null) {
                requestBuilder.queryParam("positive", String.valueOf(positive));
            }
            if (text != null) {
                requestBuilder.queryParam("text", text);
            }
            MvcResult mvcResult = mockMvc.perform(requestBuilder)
                    .andExpect(status().isOk())
                    .andReturn();
            return objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                    new TypeReference<List<ReviewDto>>(){});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected ReviewDto confirmReview(long reviewId) {
        try {
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                            .patch("/admin/reviews/" + reviewId + "/confirm")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();
            return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ReviewDto.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected ReviewDto rejectReview(long reviewId) {
        try {
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                            .patch("/admin/reviews/" + reviewId + "/reject")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();
            return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ReviewDto.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
