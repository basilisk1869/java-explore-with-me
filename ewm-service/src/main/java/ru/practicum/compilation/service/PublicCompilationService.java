package ru.practicum.compilation.service;

import org.springframework.lang.Nullable;
import ru.practicum.compilation.dto.CompilationDto;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface PublicCompilationService {

    /**
     * Получение все подборок событий
     * @param pinned искать только закрепленные/не закрепленные подборки (null - любые)
     * @param from количество элементов, которые нужно пропустить для формирования текущего набора
     * @param size количество элементов в наборе
     * @return Подборки событий
     */
    @NotNull List<CompilationDto> getCompilations(@Nullable Boolean pinned, int from, int size);

    /**
     * Получение подборки событий
     * @param compId Идентификатор подборки
     * @return Подборку событий
     */
    @NotNull CompilationDto getCompilation(long compId);

}
