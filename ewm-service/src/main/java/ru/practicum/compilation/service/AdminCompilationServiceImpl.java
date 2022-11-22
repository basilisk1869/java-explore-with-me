package ru.practicum.compilation.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.common.GetterRepository;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.event.model.Event;
import ru.practicum.exception.AlreadyExistsException;
import ru.practicum.exception.NotFoundException;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminCompilationServiceImpl implements AdminCompilationService {

    @Autowired
    CompilationRepository compilationRepository;

    @Autowired
    GetterRepository getterRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public CompilationDto postCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = modelMapper.map(newCompilationDto, Compilation.class);
        compilation.setEvents(newCompilationDto.getEvents().stream()
                .map(getterRepository::getEvent)
                .collect(Collectors.toList()));
        compilationRepository.save(compilation);
        return modelMapper.map(compilation, CompilationDto.class);
    }

    @Override
    public void deleteCompilation(long compId) {
        Compilation compilation = getterRepository.getCompilation(compId);
        compilationRepository.delete(compilation);
    }

    @Override
    public void deleteEventFromCompilation(long compId, long eventId) {
        Compilation compilation = getterRepository.getCompilation(compId);
        Event event = getterRepository.getEvent(eventId);
        if (compilation.getEvents().contains(event)) {
            compilation.getEvents().remove(event);
            compilationRepository.save(compilation);
        } else {
            throw new NotFoundException("event is not found in compilation");
        }
    }

    @Override
    public void addEventInCompilation(long compId, long eventId) {
        Compilation compilation = getterRepository.getCompilation(compId);
        Event event = getterRepository.getEvent(eventId);
        if (!compilation.getEvents().contains(event)) {
            compilation.getEvents().add(event);
            compilationRepository.save(compilation);
        } else {
            throw new AlreadyExistsException("event is already exists in compilation");
        }
    }

    @Override
    public void pin(long compId) {
        Compilation compilation = getterRepository.getCompilation(compId);
        compilation.setPinned(true);
        compilationRepository.save(compilation);
    }

    @Override
    public void unpin(long compId) {
        Compilation compilation = getterRepository.getCompilation(compId);
        compilation.setPinned(false);
        compilationRepository.save(compilation);
    }
}
