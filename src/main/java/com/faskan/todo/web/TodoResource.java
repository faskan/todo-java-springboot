package com.faskan.todo.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TodoResource {

    @GetMapping("/api/todos")
    public String getTodos() {
        return "todos";
    }
}
