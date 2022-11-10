package admin.categories;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path="/admin/categories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoriesController {

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

    @DeleteMapping(path = "/{id}")
    void deleteCategory(@PathVariable int id) {
        categoriesService.deleteCategory(id);
    }

}
