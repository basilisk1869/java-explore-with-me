package ru.practicum.review.dto;

import lombok.Data;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
public class ReviewDto {

    private Long id;

    private UserShortDto reviewer;

    private EventShortDto event;

    private LocalDateTime created;

    private Integer rating;

    private String text;

}
