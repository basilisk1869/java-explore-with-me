package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.CompilationDto;

import java.util.List;

public interface PublicCompilationService {

    List<CompilationDto> getCompilations(boolean pinned, int from, int size);

    CompilationDto getCompilation(long compId);

}
