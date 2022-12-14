package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventRequest;
import ru.practicum.event.service.UserEventService;
import ru.practicum.request.dto.ParticipationRequestDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
@Slf4j
public class UserEventController {

    @Autowired
    private final UserEventService userEventService;

    @GetMapping
    List<EventFullDto> getEvents(@PathVariable long userId,
                                 @RequestParam(required = false, defaultValue = "0") Integer from,
                                 @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("getEvents");
        return userEventService.getEvents(userId, from, size);
    }

    @PatchMapping
    EventFullDto patchEvent(@PathVariable long userId,
                            @RequestBody @Valid UpdateEventRequest updateEventRequest) {
        log.info("patchEvent " + userId + " " + updateEventRequest);
        return userEventService.patchEvent(userId, updateEventRequest);
    }

    @PostMapping
    EventFullDto postEvent(@PathVariable long userId,
                           @RequestBody @Valid NewEventDto newEventDto) {
        log.info("postEvent " + userId + " " + newEventDto);
        return userEventService.postEvent(userId, newEventDto);
    }

    @GetMapping("/{eventId}")
    EventFullDto getEvent(@PathVariable long userId,
                          @PathVariable long eventId) {
        log.info("getEvent " + userId + " " + eventId);
        return userEventService.getEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    EventFullDto patchEventCancellation(@PathVariable long userId,
                                        @PathVariable long eventId) {
        log.info("patchEventCancellation " + userId + " " + eventId);
        return userEventService.cancelEvent(userId, eventId);
    }

    @GetMapping("/{eventId}/requests")
    List<ParticipationRequestDto> getRequests(@PathVariable long userId,
                                              @PathVariable long eventId) {
        log.info("patchEventCancellation " + userId + " " + eventId);
        return userEventService.getRequests(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/confirm")
    ParticipationRequestDto patchRequestConfirmation(@PathVariable long userId,
                                                     @PathVariable long eventId,
                                                     @PathVariable long reqId) {
        log.info("patchRequestConfirmation " + userId + " " + eventId + " " + reqId);
        return userEventService.confirmRequest(userId, eventId, reqId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/reject")
    ParticipationRequestDto patchRequestRejection(@PathVariable long userId,
                                                  @PathVariable long eventId,
                                                  @PathVariable long reqId) {
        log.info("patchRequestRejection " + userId + " " + eventId + " " + reqId);
        return userEventService.rejectRequest(userId, eventId, reqId);
    }

}
