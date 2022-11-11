package ru.practicum.request.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.service.UserRequestService;

import java.util.List;

@RestController
@RequestMapping(path="/users/{userId}/requests")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserRequestController {

    @Autowired
    UserRequestService userRequestService;

    @GetMapping
    List<ParticipationRequestDto> getRequests(@PathVariable long userId) {
        return userRequestService.getRequests(userId);
    }

    @PostMapping
    ParticipationRequestDto postRequest(@PathVariable long userId,
                                        @RequestParam long eventId) {
        return userRequestService.postRequest(userId, eventId);
    }

    @PatchMapping(path = "/{requestId}/cancel")
    ParticipationRequestDto patchRequestCancellation(@PathVariable long userId,
                                                     @PathVariable long requestId) {
        return userRequestService.cancelRequest(userId, requestId);
    }

}
