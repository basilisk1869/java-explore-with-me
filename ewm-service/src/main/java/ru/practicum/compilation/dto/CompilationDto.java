package ru.practicum.compilation.dto;

import ru.practicum.event.dto.EventShortDto;

import java.util.List;

public class CompilationDto {

    List<EventShortDto> events;

    Long id;

    Boolean pinned;

    String title;

}
