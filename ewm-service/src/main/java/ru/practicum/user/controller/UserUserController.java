package ru.practicum.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.service.UserUserService;

@RestController
@RequestMapping(path = "/users/{userId}")
@RequiredArgsConstructor
@Slf4j
public class UserUserController {

    @Autowired
    private final UserUserService userUserService;

    @PatchMapping
    UserDto patchShowRating(@PathVariable long userId, @RequestParam boolean showRating) {
        UserDto result = userUserService.showRating(userId, showRating);
        log.info("patchShowRating " + result);
        return result;
    }

}
