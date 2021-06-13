package com.faskan.todo.web;

import com.faskan.todo.model.Todo;
import com.faskan.todo.repo.TodoRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    @GetMapping(API_TODOS+"/{id}")
    public Optional<Todo> getTodo(String id) {
        return todoRepository.findById(id);
    }

    @PostMapping(API_TODOS)
    public Todo createTodo(@RequestBody Todo todo) {
        return todoRepository.save(todo);
    }

    @PutMapping(API_TODOS+"/{id}")
    public Todo createTodo(@PathVariable String id, @RequestBody Todo todo) {
        Todo updatedTodo = new Todo(id, todo.name(), todo.description());
        return todoRepository.save(updatedTodo);
    }

    @DeleteMapping(API_TODOS+"/{id}")
    public void deleteTodo(@PathVariable String id) {
        todoRepository.delete(new Todo(id,"", ""));
    }
}
