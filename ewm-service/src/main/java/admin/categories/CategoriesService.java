package admin.categories;

public interface CategoriesService {

    CategoryDto patchCategory(CategoryDto categoryDto);

    CategoryDto postCategory(NewCategoryDto newCategoryDto);

    void deleteCategory(int id);

}
