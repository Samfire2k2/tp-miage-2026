package com.acme.todolist.adapters.persistence;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.acme.todolist.application.port.out.LoadTodoItem;
import com.acme.todolist.application.port.out.UpdateTodoItem;
import com.acme.todolist.domain.TodoItem;

/**
 * Adaptateur de persistance pour les TodoItems
 * Implémente les ports de sortie UpdateTodoItem et LoadTodoItem
 *
 * @author bflorat
 */
@Component
public class TodoItemPersistenceAdapter implements UpdateTodoItem, LoadTodoItem {

    private TodoItemRepository todoItemRepository;
    private TodoItemMapper todoItemMapper;

    @Inject
    public TodoItemPersistenceAdapter(TodoItemRepository todoItemRepository, TodoItemMapper todoItemMapper) {
        this.todoItemRepository = todoItemRepository;
        this.todoItemMapper = todoItemMapper;
    }

    @Override
    public void storeNewTodoItem(TodoItem item) {
        TodoItemJpaEntity jpaEntity = todoItemMapper.mapToTodoItemJpaEntity(item);
        todoItemRepository.save(jpaEntity);
    }

    @Override
    public List<TodoItem> loadAllTodoItems() {
        return todoItemRepository.findAll().stream()
                .map(todoItemMapper::mapToTodoItem)
                .collect(Collectors.toList());
    }
}


