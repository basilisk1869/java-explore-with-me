package ru.practicum.common;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.model.Request;

@Component
public class EwmModelMapper extends ModelMapper {

    public EwmModelMapper() {
        super();
        getConfiguration()
                .setPropertyCondition(Conditions.isNotNull())
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);
        // ParticipationRequestDto
        TypeMap<Request, ParticipationRequestDto> requestDtoPropertyMapper = createTypeMap(Request.class,
            ParticipationRequestDto.class);
        requestDtoPropertyMapper.addMapping(request -> request.getRequester().getId(),
            ParticipationRequestDto::setRequester);
        requestDtoPropertyMapper.addMapping(request -> request.getEvent().getId(),
            ParticipationRequestDto::setEvent);
    }
}
