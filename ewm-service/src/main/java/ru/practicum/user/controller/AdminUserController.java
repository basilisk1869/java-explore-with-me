package ru.practicum.user.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminUserController {

    @Autowired
    AdminUserService adminUserService;

    @GetMapping
    List<UserDto> getUsers(@RequestParam(required = false) List<Long> ids,
                           @RequestParam(required = false, defaultValue = "0") int from,
                           @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("getUsers");
        return adminUserService.getUsers(ids, from, size);
    }

    @PostMapping
    UserDto postUser(@RequestBody @Valid NewUserRequest newUserRequest) {
        log.info("postUser " + newUserRequest);
        return adminUserService.postUser(newUserRequest);
    }

    @DeleteMapping("/{userId}")
    void deleteUser(@PathVariable long userId) {
        log.info("deleteUser " + userId);
        adminUserService.deleteUser(userId);
    }

}
