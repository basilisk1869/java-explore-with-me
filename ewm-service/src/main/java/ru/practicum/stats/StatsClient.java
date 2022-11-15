package ru.practicum.stats;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
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

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private static ResponseEntity<Object> prepareGatewayResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }
}
