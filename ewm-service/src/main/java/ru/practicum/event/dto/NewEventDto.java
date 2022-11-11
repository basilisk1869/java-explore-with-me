package ru.practicum.event.dto;

import ru.practicum.event.Location;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class NewEventDto {

    @Size(min = 20, max = 2000)
    String annotation;

    @NotNull
    Integer category;

    @Size(min = 20, max = 7000)
    String description;

    @NotNull
    LocalDateTime eventDate;

    @NotNull
    Location location;

    Boolean paid = false;

    Integer participantLimit = 0;

    Boolean requestModeration = true;

    @Size(min = 3, max = 120)
    String title;

}
