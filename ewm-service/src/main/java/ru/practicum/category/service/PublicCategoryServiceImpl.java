package ru.practicum.category.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.repository.CategoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PublicCategoryServiceImpl implements PublicCategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        return null;
    }

    @Override
    public CategoryDto getCategory(long catId) {
        return modelMapper.map(categoryRepository.findById(catId).get(), CategoryDto.class);
    }

}
