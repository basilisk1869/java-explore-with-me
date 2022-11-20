package ru.practicum.request.dto;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParticipationRequestDto {

    LocalDateTime created;

    Integer event;

    Integer id;

    Integer requester;

    String status;

}
