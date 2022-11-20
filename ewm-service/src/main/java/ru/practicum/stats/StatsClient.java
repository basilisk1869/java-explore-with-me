package ru.practicum.stats;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatsClient {

    RestTemplate restTemplate;

    ObjectMapper objectMapper;

    public StatsClient(@Autowired RestTemplateBuilder builder, @Autowired ObjectMapper objectMapper) {
        restTemplate = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(
                        "https://stats-server:9090"))
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
}
