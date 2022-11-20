package ru.practicum.category.service;

import java.util.Optional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.common.GetterRepository;
import ru.practicum.exception.AlreadyExistsException;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminCategoryServiceImpl implements AdminCategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    GetterRepository getterRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public CategoryDto patchCategory(CategoryDto categoryDto) {
        Optional<Category> category = categoryRepository.findById(categoryDto.getId());
        if (category.isPresent()) {
            modelMapper.map(categoryDto, category);
        } else {
            category = Optional.of(modelMapper.map(categoryDto, Category.class));
        }
        categoryRepository.save(category.get());
        return modelMapper.map(category.get(), CategoryDto.class);
    }

    @Override
    public CategoryDto postCategory(NewCategoryDto newCategoryDto) {
        if (categoryRepository.findByName(newCategoryDto.getName()).isPresent()) {
            throw new AlreadyExistsException("category is already exists");
        }
        Category category = modelMapper.map(newCategoryDto, Category.class);
        categoryRepository.save(category);
        return modelMapper.map(category, CategoryDto.class);
    }

    @Override
    public void deleteCategory(long catId) {
        Category category = getterRepository.getCategory(catId);
        categoryRepository.delete(category);
    }

}
