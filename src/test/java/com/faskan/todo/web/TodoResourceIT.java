package com.faskan.todo.web;

import com.faskan.todo.model.Todo;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TodoResourceIT {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void shouldReturnStatusOK() {
        var responseEntity = testRestTemplate.getForEntity(
                url(), String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void shouldSaveTodoAndReturnAllTodosOnGet() throws JSONException {
        ResponseEntity<Todo> responseEntity = testRestTemplate.postForEntity(url(),
                postEntity(), Todo.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().id()).isNotNull();
        String todosResponse = testRestTemplate.getForEntity(url(), String.class).getBody();
        JSONAssert.assertEquals("""
                [
                    {
                        "name" : "Deploy",
                        "description" : "Deploy to prod"
                    }
                ]
                """, todosResponse, JSONCompareMode.LENIENT);
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
