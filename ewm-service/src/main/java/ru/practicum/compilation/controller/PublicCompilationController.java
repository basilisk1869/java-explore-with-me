package ru.practicum.compilation.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.service.PublicCompilationService;

import java.util.List;

@RestController
@RequestMapping(path="/compilations")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PublicCompilationController {

    @Autowired
    PublicCompilationService publicCompilationService;

    @GetMapping
    List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                         @RequestParam(required = false, defaultValue = "0") Integer from,
                                         @RequestParam(required = false, defaultValue = "10") Integer size) {
        return publicCompilationService.getCompilations(pinned, from, size);
    }

    @GetMapping(path = "/{compId}")
    CompilationDto getCompilation(@PathVariable long compId) {
        return publicCompilationService.getCompilation(compId);
    }

}
