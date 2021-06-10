package com.faskan.todo.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class TodoResource {

    @GetMapping("/api/todos")
    public List<Todo> getTodos() {
        return Arrays.asList(
                new Todo("todo1", "Todo1 Description"),
                new Todo("todo2", "Todo2 Description")
        );
    }
}
