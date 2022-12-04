package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.practicum.common.DataRange;
import ru.practicum.common.repository.CommonRepositoryImpl;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicCompilationServiceImpl implements PublicCompilationService {

    @Autowired
    private final CompilationRepository compilationRepository;

    @Autowired
    private final CommonRepositoryImpl commonRepository;

    @Autowired
    private final ModelMapper modelMapper;

    @Override
    public @NotNull List<CompilationDto> getCompilations(@Nullable Boolean pinned, int from, int size) {
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
    public @NotNull CompilationDto getCompilation(long compId) {
        Compilation compilation = commonRepository.getCompilation(compId);
        return modelMapper.map(compilation, CompilationDto.class);
    }

}
