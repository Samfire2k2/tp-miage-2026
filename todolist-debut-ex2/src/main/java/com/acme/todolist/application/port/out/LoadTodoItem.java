package com.acme.todolist.application.port.out;

import java.util.List;

import com.acme.todolist.domain.TodoItem;

/**
 * Port de sortie pour charger les TodoItems depuis une source de persistance
 * Contrat entre le domaine et les adaptateurs de persistance
 */
public interface LoadTodoItem {
	
	/**
	 * Récupère tous les TodoItems stockés en base de données
	 * 
	 * @return la liste de tous les items en base
	 */
	List<TodoItem> loadAllTodoItems();

}
