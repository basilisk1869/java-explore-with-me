package ru.practicum.category.service;

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
import ru.practicum.common.CommonRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PublicCategoryServiceImpl implements PublicCategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CommonRepository commonRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        DataRange<Category> dataRange = new DataRange<>(from, size, Sort.by(Sort.Direction.ASC, "id"));
        return dataRange.trimPage(categoryRepository.findAll(dataRange.getPageable()).getContent()).stream()
            .map(category -> modelMapper.map(category, CategoryDto.class))
            .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategory(long catId) {
        Category category = commonRepository.getCategory(catId);
        return modelMapper.map(category, CategoryDto.class);
    }
}
