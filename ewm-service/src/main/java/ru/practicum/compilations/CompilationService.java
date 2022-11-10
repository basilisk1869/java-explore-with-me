package ru.practicum.compilations;

public interface CompilationService {

    CompilationDto postCompilation(NewCompilationDto newCompilationDto);

    void deleteCompilation(long compId);

    void deleteEventFromCompilation(long compId, long eventId);

    void addEventInCompilation(long compId, long eventId);

    void pin(long compId);

    void unpin(long compId);

}
