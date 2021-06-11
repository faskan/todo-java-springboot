package com.faskan.todo.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.Arrays.asList;

@RestController
public class TodoResource {

    @GetMapping("/api/todos")
    public List<Todo> getTodos() {
        return asList(
                new Todo("todo1", "Todo1 Description"),
                new Todo("todo2", "Todo2 Description")
        );
    }

    @PostMapping("/api/todos")
    public Todo createTodo(@RequestBody Todo todo) {
        return todo;
    }
}

record Todo(String name, String description) {
}
