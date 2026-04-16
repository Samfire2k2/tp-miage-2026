package com.acme.todolist.application.port.out;

import com.acme.todolist.domain.TodoItem;

/**
 * Port de sortie pour persister les TodoItems
 * Contrat entre le domaine et les adaptateurs de persistance
 */
public interface UpdateTodoItem {
	
	/**
	 * Stocke un nouveau TodoItem en base de données
	 * 
	 * @param item l'item à stocker
	 */
	void storeNewTodoItem(TodoItem item);

}
