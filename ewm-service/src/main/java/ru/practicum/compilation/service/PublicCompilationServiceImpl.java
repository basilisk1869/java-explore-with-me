package ru.practicum.compilation.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.repository.CompilationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PublicCompilationServiceImpl implements PublicCompilationService {

    @Autowired
    CompilationRepository compilationRepository;

    @Override
    public List<CompilationDto> getCompilations(boolean pinned, int from, int size) {
        return null;
    }

    @Override
    public CompilationDto getCompilation(long compId) {
        return null;
    }
}
