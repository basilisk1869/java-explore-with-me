package admin.categories;

import javax.validation.constraints.NotBlank;

public class NewCategoryDto {

    @NotBlank
    String name;

}
