package com.faskan.todo.web;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TodoResourceIT {
    @LocalServerPort
    private int port;

    private TestRestTemplate testRestTemplate = new TestRestTemplate();

    @Test
    void test() throws JSONException {
        ResponseEntity<String> responseEntity = testRestTemplate.getForEntity("http://localhost:" + port + "/api/todos", String.class);
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
