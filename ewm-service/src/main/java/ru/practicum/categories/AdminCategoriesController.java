package ru.practicum.categories;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path="/admin/categories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminCategoriesController {

    @Autowired
    CategoriesService categoriesService;

    @PatchMapping
    CategoryDto patchCategory(@RequestBody @Valid CategoryDto categoryDto) {
        return categoriesService.patchCategory(categoryDto);
    }

    @PostMapping
    CategoryDto postCategory(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        return categoriesService.postCategory(newCategoryDto);
    }

    @DeleteMapping(path = "/{categoryId}")
    void deleteCategory(@PathVariable long categoryId) {
        categoriesService.deleteCategory(categoryId);
    }

}
