package ru.practicum.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.service.UserRequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/requests")
@RequiredArgsConstructor
@Slf4j
public class UserRequestController {

    @Autowired
    private final UserRequestService userRequestService;

    @GetMapping
    List<ParticipationRequestDto> getRequests(@PathVariable long userId) {
        log.info("getRequests " + userId);
        return userRequestService.getRequests(userId);
    }

    @PostMapping
    ParticipationRequestDto postRequest(@PathVariable long userId,
                                        @RequestParam long eventId) {
        log.info("postRequest " + userId + " " + eventId);
        return userRequestService.postRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    ParticipationRequestDto patchRequestCancellation(@PathVariable long userId,
                                                     @PathVariable long requestId) {
        log.info("patchRequestCancellation " + userId + " " + requestId);
        return userRequestService.cancelRequest(userId, requestId);
    }

}
