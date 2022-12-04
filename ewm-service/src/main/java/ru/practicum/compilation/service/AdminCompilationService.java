package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;

import javax.validation.constraints.NotNull;

public interface AdminCompilationService {

    /**
     * Добавление новой подборки событий
     * @param newCompilationDto Новая подборка событий
     * @return Данные новой подборки событий
     */
    @NotNull CompilationDto postCompilation(@NotNull NewCompilationDto newCompilationDto);

    /**
     * Удаление подборки событий
     * @param compId Идентификатор подборки событий
     */
    void deleteCompilation(long compId);

    /**
     * Удаление события из подборки
     * @param compId Идентификатор подборки
     * @param eventId Идентификатор события
     */
    void deleteEventFromCompilation(long compId, long eventId);

    /**
     * Добавление события в подборку
     * @param compId Идентификатор подборки
     * @param eventId Идентификатор события
     */
    void addEventInCompilation(long compId, long eventId);

    /**
     * Закрепление подборки на главной странице сайта
     * @param compId Идентификатор подборки
     */
    void pin(long compId);

    /**
     * Удаление подборки с главной страницы сайта
     * @param compId Идентификатор подборки
     */
    void unpin(long compId);

}
