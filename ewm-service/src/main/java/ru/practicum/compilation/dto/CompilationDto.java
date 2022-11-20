package ru.practicum.compilation.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.event.dto.EventShortDto;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationDto {

    List<EventShortDto> events;

    Long id;

    Boolean pinned;

    String title;

}
