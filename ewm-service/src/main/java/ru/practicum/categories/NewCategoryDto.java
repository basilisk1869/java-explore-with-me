package ru.practicum.categories;

import javax.validation.constraints.NotBlank;

public class NewCategoryDto {

    @NotBlank
    String name;

}
