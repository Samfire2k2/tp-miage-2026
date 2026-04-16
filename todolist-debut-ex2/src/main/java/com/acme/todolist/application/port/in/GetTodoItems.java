package com.acme.todolist.application.port.in;

import java.util.List;

import com.acme.todolist.domain.TodoItem;

/**
 * Port d'entrée (query/use case) pour récupérer les TodoItems
 * Contrat entre le domaine et les adaptateurs externes
 */
public interface GetTodoItems {
	
	/**
	 * Récupère tous les TodoItems persistés
	 * Applique la RG1 (marquage [LATE!] pour les items en retard)
	 * 
	 * @return la liste de tous les items
	 */
	List<TodoItem> getAllTodoItems();

}
