package ru.practicum.users;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path="/admin/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminUsersController {

    @Autowired
    UsersService usersService;

    @GetMapping
    List<UserDto> getUsers(@RequestParam(required = false) List<Integer> ids,
                           @RequestParam(required = false, defaultValue = "0") int from,
                           @RequestParam(required = false, defaultValue = "10") int size) {
        return usersService.getUsers(ids, from, size);
    }

    @PostMapping
    UserDto postUser(@RequestBody @Valid NewUserRequest newUserRequest) {
        return usersService.postUser(newUserRequest);
    }

    @DeleteMapping("/{userId}")
    void deleteUser(@PathVariable long userId) {
        usersService.deleteUser(userId);
    }

}
