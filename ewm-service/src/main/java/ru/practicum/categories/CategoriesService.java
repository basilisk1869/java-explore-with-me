package ru.practicum.categories;

public interface CategoriesService {

    CategoryDto patchCategory(CategoryDto categoryDto);

    CategoryDto postCategory(NewCategoryDto newCategoryDto);

    void deleteCategory(long categoryId);

}
