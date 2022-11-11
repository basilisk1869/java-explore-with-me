package ru.practicum.category.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.category.dto.NewCategoryDto;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminCategoryServiceImpl implements AdminCategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public CategoryDto patchCategory(CategoryDto categoryDto) {
        return null;
    }

    @Override
    public CategoryDto postCategory(NewCategoryDto newCategoryDto) {
        return null;
    }

    @Override
    public void deleteCategory(long categoryId) {

    }
}
