package com.faskan.todo.web;

import com.faskan.todo.model.Todo;
import com.faskan.todo.repo.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.Arrays.asList;

@RestController
public class TodoResource {
    private final TodoRepository todoRepository;

    public TodoResource(TodoRepository todoRepository){
        this.todoRepository = todoRepository;
    }

    @GetMapping("/api/todos")
    public List<Todo> getTodos() {
        return todoRepository.findAll();
    }

    @PostMapping("/api/todos")
    public Todo createTodo(@RequestBody Todo todo) {
        return todoRepository.save(todo);
    }
}

