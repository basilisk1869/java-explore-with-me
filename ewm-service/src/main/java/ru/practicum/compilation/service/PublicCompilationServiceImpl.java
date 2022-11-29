package ru.practicum.compilation.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.common.DataRange;
import ru.practicum.common.CommonRepository;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PublicCompilationServiceImpl implements PublicCompilationService {

    @Autowired
    CompilationRepository compilationRepository;

    @Autowired
    CommonRepository commonRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, int from, int size) {
        DataRange<Compilation> dataRange = new DataRange<>(from, size, Sort.by(Sort.Direction.ASC, "id"));
        List<Compilation> result;
        if (pinned != null) {
            result = compilationRepository.findAllByPinned(pinned, dataRange.getPageable());
        } else {
            result = compilationRepository.findAll(dataRange.getPageable()).getContent();
        }
        return dataRange.trimPage(result).stream()
            .map(compilation -> modelMapper.map(compilation, CompilationDto.class))
            .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilation(long compId) {
        Compilation compilation = commonRepository.getCompilation(compId);
        return modelMapper.map(compilation, CompilationDto.class);
    }
}
