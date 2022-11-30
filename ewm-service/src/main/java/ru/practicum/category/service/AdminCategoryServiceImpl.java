package ru.practicum.category.service;

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
import ru.practicum.common.CommonRepository;
import ru.practicum.exception.AccessDeniedException;
import ru.practicum.exception.AlreadyExistsException;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminCategoryServiceImpl implements AdminCategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CommonRepository commonRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public CategoryDto patchCategory(Optional<CategoryDto> categoryDto) {
        if (categoryDto.isEmpty()) {
            throw new IllegalArgumentException("CategoryDto is null");
        }
        Optional<Category> categoryByName = categoryRepository.findByName(categoryDto.get().getName());
        Optional<Category> category = categoryRepository.findById(categoryDto.get().getId());
        if (category.isPresent()) {
            if (categoryByName.isPresent() && !Objects.equals(categoryByName.get(), category.get())) {
                throw new AlreadyExistsException("category is already exists");
            }
            modelMapper.map(categoryDto, category.get());
        } else {
            if (categoryByName.isPresent()) {
                throw new AlreadyExistsException("category is already exists");
            }
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
        Category category = commonRepository.getCategory(catId);
        // с категорией не должно быть связано ни одного события
        if (category.getEvents().size() > 0) {
            throw new AccessDeniedException("cannot delete category with events");
        }
        categoryRepository.delete(category);
    }

}
