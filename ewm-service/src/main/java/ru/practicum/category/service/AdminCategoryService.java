package ru.practicum.category.service;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;

import java.util.Optional;

public interface AdminCategoryService {

    CategoryDto patchCategory(Optional<CategoryDto> categoryDto);

    CategoryDto postCategory(NewCategoryDto newCategoryDto);

    void deleteCategory(long catId);

}
