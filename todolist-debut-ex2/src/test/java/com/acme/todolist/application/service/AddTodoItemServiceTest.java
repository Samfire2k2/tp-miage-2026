package com.acme.todolist.application.service;

import static org.mockito.Mockito.*;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.acme.todolist.application.port.out.UpdateTodoItem;
import com.acme.todolist.domain.TodoItem;

/**
 * Tests unitaires pour le service applicatif AddTodoItemService
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AddTodoItemService - Orchestration des cas d'utilisation")
class AddTodoItemServiceTest {

    @Mock
    private UpdateTodoItem updateTodoItemPort;

    private AddTodoItemService addTodoItemService;

    @BeforeEach
    void setUp() {
        // Initialiser le service avec le mock du port de sortie
        addTodoItemService = new AddTodoItemService(updateTodoItemPort);
    }

    @Test
    @DisplayName("givenValidTodoItem_whenAddTodoItem_thenCallsUpdatePort")
    void testAddTodoItemCallsPort() {
        // Arrange : Créer un item valide
        Instant now = Instant.now();
        TodoItem item = new TodoItem("item-1", now, "Test item");

        // Act : Appeler le service
        addTodoItemService.addTodoItem(item);

        // Assert : Vérifier que le port de sortie est appelé une fois
        verify(updateTodoItemPort, times(1)).storeNewTodoItem(item);
    }

    @Test
    @DisplayName("givenMultipleTodoItems_whenAddTodoItem_thenCallsPortMultipleTimes")
    void testAddMultipleTodoItems() {
        // Arrange : Créer plusieurs items
        Instant now = Instant.now();
        TodoItem item1 = new TodoItem("item-1", now, "Item 1");
        TodoItem item2 = new TodoItem("item-2", now, "Item 2");

        // Act : Ajouter les items
        addTodoItemService.addTodoItem(item1);
        addTodoItemService.addTodoItem(item2);

        // Assert : Vérifier que le port est appelé 2 fois
        verify(updateTodoItemPort, times(2)).storeNewTodoItem(any(TodoItem.class));
    }
}

