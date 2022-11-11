package ru.practicum.event.dto;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.event.Location;
import ru.practicum.user.dto.UserShortDto;

import java.time.LocalDateTime;

public class EventFullDto {

    String annotation;

    CategoryDto categoryDto;

    Integer confirmedRequests;

    LocalDateTime createdOn;

    String description;

    LocalDateTime eventDate;

    Long id;

    UserShortDto initiator;

    Location location;

    Boolean paid;

    Integer participantLimit;

    LocalDateTime publishedOn;

    Boolean requestModeration;

    String title;

    Long views;

}
