package com.lotfy.logback.loki.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;

public class RestUtils {

    private RestUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static ResponseEntity post(String uri, HttpHeaders httpHeaders, String body) {
        UriComponents url = UriComponentsBuilder.fromHttpUrl(uri).build();
        HttpEntity requestEntity = new HttpEntity(body, httpHeaders);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        ResponseEntity response = restTemplate.exchange(
                url.toUri(), HttpMethod.POST, requestEntity,
                String.class);
        return response;
    }
}
