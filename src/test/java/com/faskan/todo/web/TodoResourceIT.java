package com.faskan.todo.web;

import com.faskan.todo.model.Todo;
import com.faskan.todo.repo.TodoRepository;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TodoResourceIT {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private TodoRepository todoRepository;

    @BeforeEach
    void init() {
        todoRepository.deleteAll();
    }

    @Test
    void shouldReturnAllTodos() throws JSONException {
        todoRepository.save(new Todo(null, "Find", "Find the letter F"));
        todoRepository.save(new Todo(null, "Replace", "Replace id with K"));
        var todosResponse = testRestTemplate.getForEntity(
                url(), String.class);
        assertThat(todosResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        JSONAssert.assertEquals("""
                [
                    {
                        "name" : "Find",
                        "description" : "Find the letter F"
                    },
                    {
                        "name" : "Replace",
                        "description" : "Replace id with K"
                    }
                ]
                """, todosResponse.getBody(), JSONCompareMode.LENIENT);
    }

    @Test
    void shouldReturnTodoById() throws JSONException {
        Todo todo = todoRepository.save(new Todo(null, "Find", "Find the letter F"));
        todoRepository.save(new Todo(null, "SomeThingElse", "Something else"));
        var todosResponse = testRestTemplate.getForEntity(
                url() + "/{id}", String.class, todo.id());
        assertThat(todosResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        JSONAssert.assertEquals("""
                    {
                        "name" : "Find",
                        "description" : "Find the letter F"
                    }
                """, todosResponse.getBody(), JSONCompareMode.LENIENT);
    }
    @Test
    void shouldReturn404ForAnUnknownId() throws JSONException {
        var todosResponse = testRestTemplate.getForEntity(
                url() + "/{id}", String.class, "someUnknownId");
        assertThat(todosResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
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

    @Test
    void shouldUpdateAndReturnTheNewTodo() throws JSONException {
        Todo todo = todoRepository.save(new Todo(null, "Dont Deploy", "Do not deploy to prod"));
        testRestTemplate.put(url() + "/{id}", postEntity(), todo.id());
        var todosResponse = testRestTemplate.getForEntity(
                url() + "/{id}", String.class, todo.id());
        assertThat(todosResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        JSONAssert.assertEquals("""
                    {
                        "name" : "Deploy",
                        "description" : "Deploy to prod"
                    }
                """, todosResponse.getBody(), JSONCompareMode.LENIENT);
    }
    @Test
    void shouldDeleteTheTodo() throws JSONException {
        Todo todo = todoRepository.save(new Todo(null, "Dont Deploy", "Do not deploy to prod"));
        testRestTemplate.delete(url() + "/{id}", todo.id());
        var todosResponse = testRestTemplate.getForEntity(
                url() + "/", String.class, todo.id());
        assertThat(todosResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        JSONAssert.assertEquals("""
                [
                ]
                """, todosResponse.getBody(), JSONCompareMode.STRICT);
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
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
        return headers;
    }

    private String url() {
        return "http://localhost:" + port + "/api/todos";
    }
}
