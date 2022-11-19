package ru.practicum.compilation.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.AlreadyExistsException;
import ru.practicum.exception.NotFoundException;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminCompilationServiceImpl implements AdminCompilationService {

    @Autowired
    CompilationRepository compilationRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public CompilationDto postCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = modelMapper.map(newCompilationDto, Compilation.class);
        compilationRepository.save(compilation);
        return modelMapper.map(compilation, CompilationDto.class);
    }

    @Override
    public void deleteCompilation(long compId) {
        Compilation compilation = getCompilation(compId);
        compilationRepository.delete(compilation);
    }

    @Override
    public void deleteEventFromCompilation(long compId, long eventId) {
        Compilation compilation = getCompilation(compId);
        Event event = getEvent(eventId);
        if (compilation.getEvents().contains(event)) {
            compilation.getEvents().remove(event);
            compilationRepository.save(compilation);
        } else {
            throw new NotFoundException("event is not found in compilation");
        }
    }

    @Override
    public void addEventInCompilation(long compId, long eventId) {
        Compilation compilation = getCompilation(compId);
        Event event = getEvent(eventId);
        if (!compilation.getEvents().contains(event)) {
            compilation.getEvents().add(event);
            compilationRepository.save(compilation);
        } else {
            throw new AlreadyExistsException("event is already exists in compilation");
        }
    }

    @Override
    public void pin(long compId) {

    }

    @Override
    public void unpin(long compId) {

    }

    private Compilation getCompilation(long compId) {
        return compilationRepository.findById(compId).orElseThrow(() -> {
            throw new NotFoundException("compilation is not found");
        });
    }

    private Event getEvent(long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> {
            throw new NotFoundException("event is not found");
        });
    }

}
