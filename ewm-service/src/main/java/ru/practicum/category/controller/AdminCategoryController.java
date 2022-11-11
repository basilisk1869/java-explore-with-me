package ru.practicum.category.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.service.AdminCategoryService;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;

import javax.validation.Valid;

@RestController
@RequestMapping(path="/admin/categories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminCategoryController {

    @Autowired
    AdminCategoryService adminCategoryService;

    @PatchMapping
    CategoryDto patchCategory(@RequestBody @Valid CategoryDto categoryDto) {
        return adminCategoryService.patchCategory(categoryDto);
    }

    @PostMapping
    CategoryDto postCategory(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        return adminCategoryService.postCategory(newCategoryDto);
    }

    @DeleteMapping(path = "/{categoryId}")
    void deleteCategory(@PathVariable long categoryId) {
        adminCategoryService.deleteCategory(categoryId);
    }

}
