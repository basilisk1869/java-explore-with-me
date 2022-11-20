package ru.practicum.compilation.service;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.common.DataRange;
import ru.practicum.common.GetterRepository;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;

import java.util.List;
import ru.practicum.user.model.User;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PublicCompilationServiceImpl implements PublicCompilationService {

    @Autowired
    CompilationRepository compilationRepository;

    @Autowired
    GetterRepository getterRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, int from, int size) {
        DataRange<User> dataRange = new DataRange<>(from, size, Sort.by(Sort.Direction.ASC, "id"));
        Stream<Compilation> result;
        if (pinned != null) {
            result = compilationRepository.findAllByPinned(pinned, dataRange.getPageable()).stream();
        } else {
            result = compilationRepository.findAll(dataRange.getPageable()).stream();
        }
        return result
            .map(compilation -> modelMapper.map(compilation, CompilationDto.class))
            .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilation(long compId) {
        Compilation compilation = getterRepository.getCompilation(compId);
        return modelMapper.map(compilation, CompilationDto.class);
    }
}
