package ru.practicum.user.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.service.AdminUserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path="/admin/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminUserController {

    @Autowired
    AdminUserService adminUserService;

    @GetMapping
    List<UserDto> getUsers(@RequestParam(required = false) List<Long> ids,
                           @RequestParam(required = false, defaultValue = "0") int from,
                           @RequestParam(required = false, defaultValue = "10") int size) {
        return adminUserService.getUsers(ids, from, size);
    }

    @PostMapping
    UserDto postUser(@RequestBody @Valid NewUserRequest newUserRequest) {
        return adminUserService.postUser(newUserRequest);
    }

    @DeleteMapping("/{userId}")
    void deleteUser(@PathVariable long userId) {
        adminUserService.deleteUser(userId);
    }

}
