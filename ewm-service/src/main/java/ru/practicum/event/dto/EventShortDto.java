package ru.practicum.event.dto;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.user.dto.UserShortDto;

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
