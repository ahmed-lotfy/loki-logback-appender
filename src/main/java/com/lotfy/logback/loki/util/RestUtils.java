package com.lotfy.logback.loki.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

public class RestUtils {

    private RestUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static ResponseEntity post(String uri, HttpHeaders httpHeaders, String body) {
        UriComponents url = UriComponentsBuilder.fromHttpUrl(uri).build();
        HttpEntity requestEntity = new HttpEntity(body, httpHeaders);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity response = restTemplate.exchange(
                url.toUri(), HttpMethod.POST, requestEntity,
                String.class);
        return response;
    }
}
