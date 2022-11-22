package ru.practicum.stats;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatsClient {

    RestTemplate restTemplate;

    ObjectMapper objectMapper;

    public StatsClient(@Autowired RestTemplateBuilder builder, @Autowired ObjectMapper objectMapper) {
        Optional<Properties> properties = getApplicationProperties();
        String statsServerUrl = "http://stats-server:9090";
        if (properties.isPresent()) {
            statsServerUrl = properties.get().getProperty("stats-server-url", statsServerUrl);
        }
        restTemplate = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(statsServerUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
        this.objectMapper = objectMapper;
    }

    public ResponseEntity<Object> postEndpointHit(EndpointHitDto endpointHitDto) {
        HttpEntity<Object> requestEntity = new HttpEntity<>(endpointHitDto, defaultHeaders());
        return restTemplate.exchange("/hit", HttpMethod.POST,
                requestEntity, Object.class);
    }

    public ResponseEntity<List<ViewStats>> getUrlViews(LocalDateTime start, LocalDateTime end,
        List<String> uris, Boolean unique) {
        Map<String, Object> parameters = Map.of(
            "start", start,
            "end", end,
            "uris", uris,
            "unique", unique);
        HttpEntity<Object> requestEntity = new HttpEntity<>(null, defaultHeaders());
        return restTemplate.exchange("/views?start={start}&end={end}&uris={uris}&unique={unique}",
            HttpMethod.POST, requestEntity, new ParameterizedTypeReference<List<ViewStats>>(){}, parameters);
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private Optional<Properties> getApplicationProperties() {
        try {
            Properties properties = new Properties();
            properties.load(getClass().getClassLoader().getResourceAsStream("application.properties"));
            return Optional.of(properties);
        } catch (IOException e) {
            return Optional.empty();
        }
    }
}
