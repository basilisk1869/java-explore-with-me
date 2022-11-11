package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;

public interface AdminCompilationService {

    CompilationDto postCompilation(NewCompilationDto newCompilationDto);

    void deleteCompilation(long compId);

    void deleteEventFromCompilation(long compId, long eventId);

    void addEventInCompilation(long compId, long eventId);

    void pin(long compId);

    void unpin(long compId);

}
