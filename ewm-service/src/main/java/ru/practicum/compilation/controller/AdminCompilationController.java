package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.service.AdminCompilationService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
public class AdminCompilationController {

    @Autowired
    private final AdminCompilationService adminCompilationService;

    @PostMapping
    CompilationDto postCompilation(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        return adminCompilationService.postCompilation(newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    void deleteCompilation(@PathVariable long compId) {
        adminCompilationService.deleteCompilation(compId);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    void deleteEventFromCompilation(@PathVariable long compId, @PathVariable long eventId) {
        adminCompilationService.deleteEventFromCompilation(compId, eventId);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    void patchEventInCompilation(@PathVariable long compId, @PathVariable long eventId) {
        adminCompilationService.addEventInCompilation(compId, eventId);
    }

    @DeleteMapping("/{compId}/pin")
    void deletePin(@PathVariable long compId) {
        adminCompilationService.unpin(compId);
    }

    @PatchMapping("/{compId}/pin")
    void patchPin(@PathVariable long compId) {
        adminCompilationService.pin(compId);
    }

}
