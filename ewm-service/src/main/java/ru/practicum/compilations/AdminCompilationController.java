package ru.practicum.compilations;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path="/admin/compilations")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminCompilationController {

    @Autowired
    CompilationService compilationService;

    @PostMapping
    CompilationDto postCompilation(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        return compilationService.postCompilation(newCompilationDto);
    }

    @DeleteMapping(path = "/{compId}")
    void deleteCompilation(@PathVariable long compId) {
        compilationService.deleteCompilation(compId);
    }

    @DeleteMapping(path = "/{compId}/events/{eventId}")
    void deleteEventFromCompilation(@PathVariable long compId, @PathVariable long eventId) {
        compilationService.deleteEventFromCompilation(compId, eventId);
    }

    @PatchMapping(path = "/{compId}/events/{eventId}")
    void patchEventInCompilation(@PathVariable long compId, @PathVariable long eventId) {
        compilationService.addEventInCompilation(compId, eventId);
    }

    @DeleteMapping(path = "/{compId}/pin")
    void deletePin(@PathVariable long compId) {
        compilationService.unpin(compId);
    }

    @PatchMapping(path = "/{compId}/pin")
    void patchPin(@PathVariable long compId) {
        compilationService.pin(compId);
    }

}
