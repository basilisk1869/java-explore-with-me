package ru.practicum.events;

import ru.practicum.categories.CategoryDto;
import ru.practicum.users.UserShortDto;

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
