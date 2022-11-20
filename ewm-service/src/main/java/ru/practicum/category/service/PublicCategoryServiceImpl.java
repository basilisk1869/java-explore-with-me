package ru.practicum.category.service;

import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.common.DataRange;
import ru.practicum.common.GetterRepository;
import ru.practicum.exception.NotFoundException;

import java.util.List;
import ru.practicum.user.model.User;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PublicCategoryServiceImpl implements PublicCategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    GetterRepository getterRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        DataRange<User> dataRange = new DataRange<>(from, size, Sort.by(Sort.Direction.ASC, "id"));
        return categoryRepository.findAll(dataRange.getPageable()).stream()
            .map(category -> modelMapper.map(category, CategoryDto.class))
            .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategory(long catId) {
        Category category = getterRepository.getCategory(catId);
        return modelMapper.map(category, CategoryDto.class);
    }
}
