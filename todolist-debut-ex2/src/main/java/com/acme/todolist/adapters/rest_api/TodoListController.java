package com.acme.todolist.adapters.rest_api;

import java.util.List;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.acme.todolist.application.port.in.AddTodoItem;
import com.acme.todolist.application.port.in.GetTodoItems;
import com.acme.todolist.domain.TodoItem;

/**
 * Adaptateur REST du contrôleur - Architecture hexagonale
 * Expose les endpoints HTTP et délègue au ports d'entrée
 */
@RestController
public class TodoListController {
	
	/**
	 * Port d'entrée pour récupérer les TodoItems
	 */
	private GetTodoItems getTodoItemsQuery;
	
	/**
	 * Port d'entrée pour ajouter un TodoItem
	 */
	private AddTodoItem addTodoItemCommand;

	/**
	 * Injection des ports d'entrée via le constructeur
	 * 
	 * @param getTodoItemsQuery port pour récupérer les items
	 * @param addTodoItemCommand port pour ajouter un item
	 */
	@Inject
	public TodoListController(GetTodoItems getTodoItemsQuery, AddTodoItem addTodoItemCommand) {
		this.getTodoItemsQuery = getTodoItemsQuery;
		this.addTodoItemCommand = addTodoItemCommand;
	}
	
	@GetMapping("/todos")
	public List<TodoItem> getAllTodoItems() {
		return this.getTodoItemsQuery.getAllTodoItems();
	}
	
	@PostMapping("/todos")
	@ResponseStatus(code = HttpStatus.CREATED)
	public void ajouterItem(@RequestBody TodoItem item) {
		this.addTodoItemCommand.addTodoItem(item);
	}

}
