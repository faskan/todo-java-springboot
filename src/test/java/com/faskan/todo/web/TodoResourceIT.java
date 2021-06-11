package com.faskan.todo.web;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Arrays.asList;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TodoResourceIT {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void shouldReturnStatusOK() throws JSONException {
        var responseEntity = testRestTemplate.getForEntity(
                url(), String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        JSONAssert.assertEquals("""
                [
                    {
                        "name" : "todo1",
                        "description" : "Todo1 Description"
                    },
                    {
                        "name" : "todo2",
                        "description" : "Todo2 Description"
                    }
                ]
                """, responseEntity.getBody(), JSONCompareMode.STRICT);
    }

    @Test
    void shouldSaveTodoAndReturnAllTodosWhenGet() {
        ResponseEntity<String> responseEntity = testRestTemplate.exchange(url(), HttpMethod.POST,
                postEntity(), String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private HttpEntity<String> postEntity() {
        return new HttpEntity<>("""
                {
                    "name" : "Deploy",
                    "description" : "Deploy to prod"
                }
                """, headers());
    }

    private MultiValueMap<String, String> headers() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        return headers;
    }

    private String url() {
        return "http://localhost:" + port + "/api/todos";
    }
}
