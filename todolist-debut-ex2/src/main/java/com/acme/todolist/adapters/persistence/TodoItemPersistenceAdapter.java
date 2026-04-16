package com.acme.todolist.adapters.persistence;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.acme.todolist.application.port.out.LoadTodoItem;
import com.acme.todolist.application.port.out.UpdateTodoItem;
import com.acme.todolist.domain.TodoItem;

/**
 * Adaptateur de persistance - Architecture hexagonale
 * Implémente les ports de sortie pour persister et charger les TodoItems
 * Convertit entre les entités du domaine et les entités JPA
 */
@Component
public class TodoItemPersistenceAdapter implements UpdateTodoItem, LoadTodoItem {

	private TodoItemRepository todoItemRepository;
	private TodoItemMapper todoItemMapper;

	/**
	 * Injection des dépendances
	 * 
	 * @param todoItemRepository le repository JPA
	 * @param todoItemMapper le mapper entre domaine et JPA
	 */
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


