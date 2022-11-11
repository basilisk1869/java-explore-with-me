package ru.practicum.event.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.service.UserEventService;
import ru.practicum.request.dto.ParticipationRequestDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path="/users/{userId}/events")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserEventController {

    @Autowired
    UserEventService userEventService;

    @GetMapping
    List<EventFullDto> getEvents(@PathVariable long userId,
                                 @RequestParam(required = false, defaultValue = "0") Integer from,
                                 @RequestParam(required = false, defaultValue = "10") Integer size) {
        return userEventService.getEvents(userId, from, size);
    }

    @PatchMapping
    EventFullDto patchEvent(@PathVariable long userId,
                            @RequestBody @Valid NewEventDto newEventDto) {
        return userEventService.patchEvent(userId, newEventDto);
    }

    @PostMapping
    EventFullDto postEvent(@PathVariable long userId,
                           @RequestBody @Valid NewEventDto newEventDto) {
        return userEventService.postEvent(userId, newEventDto);
    }

    @GetMapping(path = "/{eventId}")
    EventFullDto getEvent(@PathVariable long userId,
                          @PathVariable long eventId) {
        return userEventService.getEvent(userId, eventId);
    }

    @PatchMapping(path = "/{eventId}")
    EventFullDto patchEventCancellation(@PathVariable long userId,
                                        @PathVariable long eventId) {
        return userEventService.cancelEvent(userId, eventId);
    }

    @GetMapping(path = "/{eventId}/requests")
    List<ParticipationRequestDto> getRequests(@PathVariable long userId,
                                              @PathVariable long eventId) {
        return userEventService.getRequests(userId, eventId);
    }

    @PatchMapping(path = "/{eventId}/requests/{reqId}/confirm")
    ParticipationRequestDto patchRequestConfirmation(@PathVariable long userId,
                                                     @PathVariable long eventId,
                                                     @PathVariable long reqId) {
        return userEventService.confirmRequest(userId, eventId, reqId);
    }

    @PatchMapping(path = "/{eventId}/requests/{reqId}/reject")
    ParticipationRequestDto patchRequestRejection(@PathVariable long userId,
                                                  @PathVariable long eventId,
                                                  @PathVariable long reqId) {
        return userEventService.rejectRequest(userId, eventId, reqId);
    }

}
