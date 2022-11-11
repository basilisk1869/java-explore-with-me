package ru.practicum.compilation.dto;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class NewCompilationDto {

    List<Long> events;

    Boolean pinned;

    @NotBlank
    String title;

}
