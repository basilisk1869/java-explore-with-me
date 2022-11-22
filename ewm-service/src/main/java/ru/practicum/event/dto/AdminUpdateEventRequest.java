package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import ru.practicum.location.dto.LocationDto;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminUpdateEventRequest {

  @Length(min = 20, max = 2000)
  String annotation;

  Long category;

  @Length(min = 20, max = 7000)
  String description;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  LocalDateTime eventDate;

  LocationDto location;

  Boolean paid;

  Integer participantLimit;

  Boolean requestModeration;

  @Size(min = 3, max = 120)
  String title;
}
