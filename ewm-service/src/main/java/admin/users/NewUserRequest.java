package admin.users;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class NewUserRequest {

    @NotBlank @Email
    String email;

    @NotBlank
    String name;

}
