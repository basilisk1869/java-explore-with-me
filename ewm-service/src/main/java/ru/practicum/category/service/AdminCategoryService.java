package ru.practicum.category.service;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;

import javax.validation.constraints.NotNull;

public interface AdminCategoryService {

    /**
     * Изменение категории
     * @param categoryDto Новые данные категории
     * @return Измененные данные категорию
     */
    @NotNull CategoryDto patchCategory(@NotNull CategoryDto categoryDto);

    /**
     * Добавление категории
     * @param newCategoryDto Новая категория
     * @return Новые данные категории
     */
    @NotNull CategoryDto postCategory(@NotNull NewCategoryDto newCategoryDto);

    /**
     * Удаление категории
     * @param catId Идентификатор категории
     */
    void deleteCategory(long catId);

}
