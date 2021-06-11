package com.faskan.todo.web;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TodoResourceIT {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void shouldReturnAllTodos() throws JSONException {
        var responseEntity = testRestTemplate.getForEntity(
                "http://localhost:" + port + "/api/todos", String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        JSONAssert.assertEquals("[\n" +
                "  {\n" +
                "    \"name\" : \"todo1\",\n" +
                "    \"description\" : \"Todo1 Description\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\" : \"todo2\",\n" +
                "    \"description\" : \"Todo2 Description\"\n" +
                "  }\n" +
                "]", responseEntity.getBody(), JSONCompareMode.STRICT);
    }
}
