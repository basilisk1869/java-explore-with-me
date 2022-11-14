package ru.practicum.category.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.service.AdminCategoryService;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping(path="/admin/categories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminCategoryController {

    @Autowired
    AdminCategoryService adminCategoryService;

    @PatchMapping
    CategoryDto patchCategory(@RequestBody @Valid CategoryDto categoryDto) {
        log.info("patchCategory " + categoryDto);
        return adminCategoryService.patchCategory(categoryDto);
    }

    @PostMapping
    CategoryDto postCategory(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        log.info("postCategory " + newCategoryDto);
        return adminCategoryService.postCategory(newCategoryDto);
    }

    @DeleteMapping("/{catId}")
    void deleteCategory(@PathVariable long catId) {
        log.info("deleteCategory " + catId);
        adminCategoryService.deleteCategory(catId);
    }

}
