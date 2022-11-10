package admin.categories;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CategoryDto {

    @NotNull
    Integer id;

    @NotBlank
    String name;

}
