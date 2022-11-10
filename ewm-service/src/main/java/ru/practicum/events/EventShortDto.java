package ru.practicum.events;

import ru.practicum.categories.CategoryDto;
import ru.practicum.users.UserShortDto;

import java.time.LocalDateTime;

public class EventShortDto {

    String annotation;

    CategoryDto categoryDto;

    Integer confirmedRequests;

    LocalDateTime eventDate;

    Long id;

    UserShortDto initiator;

    Boolean paid;

    String title;

    Long views;

}
