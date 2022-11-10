package ru.practicum.categories;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CategoryDto {

    @NotNull
    Long id;

    @NotBlank
    String name;

}
