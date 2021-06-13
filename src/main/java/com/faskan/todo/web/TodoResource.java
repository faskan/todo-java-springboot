package com.faskan.todo.web;

import com.faskan.todo.model.Todo;
import com.faskan.todo.repo.TodoRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TodoResource {
    public static final String API_TODOS = "/api/todos";
    private final TodoRepository todoRepository;

    public TodoResource(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @GetMapping(API_TODOS)
    public List<Todo> getTodos() {
        return todoRepository.findAll();
    }

    @PostMapping(API_TODOS)
    public Todo createTodo(@RequestBody Todo todo) {
        return todoRepository.save(todo);
    }
}
