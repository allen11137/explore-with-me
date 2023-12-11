package ru.practicum.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.EndpointHitDto;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class Client {
    protected final RestTemplate rest;

    public Client(@Value("${STATS_SERVICE_URL}") String serverUrl, RestTemplateBuilder builder) {
        this.rest = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    public ResponseEntity<Object> addRequest(String ipResource, EndpointHitDto endpointHitDto) {
        return makeAndSendRequest(HttpMethod.POST, "/hit", ipResource, null, endpointHitDto);
    }

    public ResponseEntity<Object> getStat(String ipResource, String start, String end, String[] uris, boolean unique) {
        if (uris != null) {
            return makeAndSendRequest(HttpMethod.GET,
                    "/stats?start={start}&end={end}&uris={uris}&unique={unique}",
                    ipResource,
                    Map.of(
                            "start", start,
                            "end", end,
                            "uris", uris,
                            "unique", unique
                    ),
                    null);
        } else {
            return makeAndSendRequest(HttpMethod.GET,
                    "/stats?start={start}&end={end}&unique={unique}",
                    ipResource,
                    Map.of(
                            "start", start,
                            "end", end,
                            "unique", unique
                    ),
                    null);
        }
    }

    private <T> ResponseEntity<Object> makeAndSendRequest(HttpMethod method, String path, String ipResource, @Nullable Map<String, Object> parameters, @Nullable T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, defaultHeaders(ipResource));
        ResponseEntity<Object> statsServerResponse;
        try {
            if (parameters != null) {
                statsServerResponse = rest.exchange(path, method, requestEntity, Object.class, parameters);
            } else {
                statsServerResponse = rest.exchange(path, method, requestEntity, Object.class);
            }
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(e.getResponseBodyAsByteArray());
        }
        return prepareGatewayResponse(statsServerResponse);
    }

    private HttpHeaders defaultHeaders(String ipResource) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        if (Objects.nonNull(ipResource)) {
            headers.set("X-Stats-Resource-Ip", ipResource);
        }
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