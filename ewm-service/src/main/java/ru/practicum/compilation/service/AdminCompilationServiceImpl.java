package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.common.repository.CommonRepositoryImpl;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.AlreadyExistsException;
import ru.practicum.exception.NotFoundException;

import javax.validation.constraints.NotNull;

@Service
@RequiredArgsConstructor
public class AdminCompilationServiceImpl implements AdminCompilationService {

    @Autowired
    private final CompilationRepository compilationRepository;

    @Autowired
    private final CommonRepositoryImpl commonRepository;

    @Autowired
    private final EventRepository eventRepository;

    @Autowired
    private final ModelMapper modelMapper;

    @Override
    public @NotNull CompilationDto postCompilation(@NotNull NewCompilationDto newCompilationDto) {
        Compilation compilation = modelMapper.map(newCompilationDto, Compilation.class);
        compilation.setEvents(eventRepository.findAllById(newCompilationDto.getEvents()));
        compilationRepository.save(compilation);
        return modelMapper.map(compilation, CompilationDto.class);
    }

    @Override
    public void deleteCompilation(long compId) {
        Compilation compilation = commonRepository.getCompilation(compId);
        compilationRepository.delete(compilation);
    }

    @Override
    public void deleteEventFromCompilation(long compId, long eventId) {
        Compilation compilation = commonRepository.getCompilation(compId);
        Event event = commonRepository.getEvent(eventId);
        if (compilation.getEvents().contains(event)) {
            compilation.getEvents().remove(event);
            compilationRepository.save(compilation);
        } else {
            throw new NotFoundException("event is not found in compilation");
        }
    }

    @Override
    public void addEventInCompilation(long compId, long eventId) {
        Compilation compilation = commonRepository.getCompilation(compId);
        Event event = commonRepository.getEvent(eventId);
        if (!compilation.getEvents().contains(event)) {
            compilation.getEvents().add(event);
            compilationRepository.save(compilation);
        } else {
            throw new AlreadyExistsException("event is already exists in compilation");
        }
    }

    @Override
    public void pin(long compId) {
        Compilation compilation = commonRepository.getCompilation(compId);
        compilation.setPinned(true);
        compilationRepository.save(compilation);
    }

    @Override
    public void unpin(long compId) {
        Compilation compilation = commonRepository.getCompilation(compId);
        compilation.setPinned(false);
        compilationRepository.save(compilation);
    }
}
