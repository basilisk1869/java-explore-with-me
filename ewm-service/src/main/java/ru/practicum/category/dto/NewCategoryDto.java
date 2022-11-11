package ru.practicum.category.dto;

import javax.validation.constraints.NotBlank;

public class NewCategoryDto {

    @NotBlank
    String name;

}
