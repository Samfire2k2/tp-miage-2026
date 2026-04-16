package com.acme.todolist.adapters.persistence;

import org.springframework.stereotype.Component;

import com.acme.todolist.domain.TodoItem;

/**
 * Mapper pour convertir entre les entités du domaine et les entités JPA
 */
@Component
public class TodoItemMapper {
	
	/**
	 * Convertir une entité JPA en entité du domaine
	 * 
	 * @param todoItemJpaEntity l'entité JPA
	 * @return l'entité du domaine
	 */
	public TodoItem mapToTodoItem(TodoItemJpaEntity todoItemJpaEntity) {
		return new TodoItem(todoItemJpaEntity.getId(), todoItemJpaEntity.getTime(), todoItemJpaEntity.getContent());
	}
	
	/**
	 * Convertir une entité du domaine en entité JPA
	 * 
	 * @param todoItem l'entité du domaine
	 * @return l'entité JPA
	 */
	public TodoItemJpaEntity mapToTodoItemJpaEntity(TodoItem todoItem) {
		return new TodoItemJpaEntity(todoItem.getId(), todoItem.getTime(), todoItem.getContent(),true);
	}

}
