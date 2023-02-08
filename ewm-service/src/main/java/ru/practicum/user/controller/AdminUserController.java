package ru.practicum.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.service.AdminUserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
@Slf4j
public class AdminUserController {

    @Autowired
    private final AdminUserService adminUserService;

    @GetMapping
    List<UserDto> getUsers(@RequestParam(required = false) List<Long> ids,
                           @RequestParam(required = false, defaultValue = "0") int from,
                           @RequestParam(required = false, defaultValue = "20") int size) {
        List<UserDto> result = adminUserService.getUsers(ids, from, size);
        log.info("getUsers " + result);
        return result;
    }

    @PostMapping
    UserDto postUser(@RequestBody @Valid NewUserRequest newUserRequest) {
        UserDto userDto = adminUserService.postUser(newUserRequest);
        log.info("postUser " + userDto);
        return userDto;
    }

    @DeleteMapping("/{userId}")
    void deleteUser(@PathVariable long userId) {
        log.info("deleteUser " + userId);
        adminUserService.deleteUser(userId);
    }

}
