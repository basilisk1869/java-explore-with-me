package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import ru.practicum.location.dto.LocationDto;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateEventRequest {

  @Length(min = 20, max = 2000)
  String annotation;

  Long category;

  @Length(min = 20, max = 7000)
  String description;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  LocalDateTime eventDate;

  Long eventId;

  Boolean paid;

  Integer participantLimit;

  @Size(min = 3, max = 120)
  String title;
}
