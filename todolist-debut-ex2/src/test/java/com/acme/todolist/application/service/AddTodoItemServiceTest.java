package com.acme.todolist.application.service;

import static org.mockito.ArgumentMatchers.any;
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
 *
 * @author étudiant
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

        // Assert : Vérifier que le port de sortie est appelé une fois avec le bon item
        verify(updateTodoItemPort, times(1)).storeNewTodoItem(item);
    }

    @Test
    @DisplayName("givenNullTodoItem_whenAddTodoItem_thenStillCallsPort")
    void testAddTodoItemWithNullItem() {
        // Act : Appeler le service avec null
        addTodoItemService.addTodoItem(null);

        // Assert : Vérifier que le port est appelé (le port gère lui-même la validation)
        verify(updateTodoItemPort, times(1)).storeNewTodoItem(null);
    }

    @Test
    @DisplayName("givenMultipleTodoItems_whenAddTodoItem_thenCallsPortMultipleTimes")
    void testAddMultipleTodoItems() {
        // Arrange : Créer plusieurs items
        Instant now = Instant.now();
        TodoItem item1 = new TodoItem("item-1", now, "Item 1");
        TodoItem item2 = new TodoItem("item-2", now, "Item 2");
        TodoItem item3 = new TodoItem("item-3", now, "Item 3");

        // Act : Ajouter les items
        addTodoItemService.addTodoItem(item1);
        addTodoItemService.addTodoItem(item2);
        addTodoItemService.addTodoItem(item3);

        // Assert : Vérifier que le port est appelé 3 fois
        verify(updateTodoItemPort, times(3)).storeNewTodoItem(any(TodoItem.class));
    }

    @Test
    @DisplayName("givenTodoItem_whenAddTodoItem_thenVerifyCorrectItemPassedToPort")
    void testCorrectItemPassedToPort() {
        // Arrange : Créer un item avec des propriétés spécifiques
        Instant specificTime = Instant.parse("2020-02-27T10:31:43Z");
        TodoItem item = new TodoItem("specific-id", specificTime, "Specific content");

        // Act : Appeler le service
        addTodoItemService.addTodoItem(item);

        // Assert : Vérifier que le port reçoit l'item avec les bonnes propriétés
        verify(updateTodoItemPort).storeNewTodoItem(argThat(
            receivedItem -> receivedItem.getId().equals("specific-id") &&
                           receivedItem.getContent().equals("Specific content") &&
                           receivedItem.getTime().equals(specificTime)
        ));
    }
}

