package ru.practicum.category.service;


import ru.practicum.category.dto.CategoryDto;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface PublicCategoryService {

    /**
     * Получение категорий, отсортированных по возрастанию идентификатора
     * @param from количество категорий, которые нужно пропустить для формирования текущего набора
     * @param size количество категорий в наборе
     * @return Список категорий
     */
    @NotNull List<CategoryDto> getCategories(int from, int size);

    /**
     * Получение категории
     * @param catId Идентификатор категории
     * @return Данные категории
     */
    @NotNull CategoryDto getCategory(long catId);

}
