package ru.practicum.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

@Component
@Primary
public class StatsObjectMapper extends ObjectMapper {

    public StatsObjectMapper() {
        registerModule(new JavaTimeModule());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        setDateFormat(df);
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

}
