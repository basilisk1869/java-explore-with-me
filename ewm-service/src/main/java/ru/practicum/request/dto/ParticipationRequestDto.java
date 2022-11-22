package ru.practicum.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParticipationRequestDto {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime created;

    Long event;

    Long id;

    Long requester;

    String status;

}
