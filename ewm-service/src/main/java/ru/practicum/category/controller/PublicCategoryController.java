package ru.practicum.category.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.service.PublicCategoryService;

import java.util.List;

@RestController
@RequestMapping(path = "/categories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PublicCategoryController {

    @Autowired
    PublicCategoryService publicCategoryService;

    @GetMapping
    List<CategoryDto> getCategories(@RequestParam(required = false, defaultValue = "0") Integer from,
                                    @RequestParam(required = false, defaultValue = "10") Integer size) {
        return publicCategoryService.getCategories(from, size);
    }

    @GetMapping(path = "/{catId}")
    CategoryDto getCategory(@PathVariable long catId) {
        return publicCategoryService.getCategory(catId);
    }

}
