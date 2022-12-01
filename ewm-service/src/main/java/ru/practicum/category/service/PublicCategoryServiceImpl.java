package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.common.CommonRepository;
import ru.practicum.common.DataRange;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicCategoryServiceImpl implements PublicCategoryService {

    @Autowired
    private final CategoryRepository categoryRepository;

    @Autowired
    private final CommonRepository commonRepository;

    @Autowired
    private final ModelMapper modelMapper;

    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        DataRange<Category> dataRange = new DataRange<>(from, size, Sort.by(Sort.Direction.ASC, "id"));
        List<Category> categories = categoryRepository.findAll(dataRange.getPageable()).getContent();
        return dataRange.trimPage(categories).stream()
            .map(category -> modelMapper.map(category, CategoryDto.class))
            .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategory(long catId) {
        Category category = commonRepository.getCategory(catId);
        return modelMapper.map(category, CategoryDto.class);
    }
}
