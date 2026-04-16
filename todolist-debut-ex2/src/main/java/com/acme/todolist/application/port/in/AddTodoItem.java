package com.acme.todolist.application.port.in;

import com.acme.todolist.domain.TodoItem;

/**
 * Port d'entrée (use case) pour ajouter un TodoItem
 * Contrat entre le domaine et les adaptateurs externes
 */
public interface AddTodoItem {
	
	/**
	 * Ajoute un nouvel item à la liste de tâches
	 * 
	 * @param item l'item à ajouter
	 */
	void addTodoItem(TodoItem item);
}
