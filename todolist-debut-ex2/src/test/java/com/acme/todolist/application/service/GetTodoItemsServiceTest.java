package com.acme.todolist.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.acme.todolist.application.port.out.LoadTodoItem;
import com.acme.todolist.domain.TodoItem;

/**
 * Tests unitaires pour le service applicatif GetTodoItemsService
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("GetTodoItemsService - Récupération des items")
class GetTodoItemsServiceTest {

    @Mock
    private LoadTodoItem loadTodoItemPort;

    private GetTodoItemsService getTodoItemsService;

    @BeforeEach
    void setUp() {
        getTodoItemsService = new GetTodoItemsService(loadTodoItemPort);
    }

    @Test
    @DisplayName("givenItemsInPort_whenGetAllTodoItems_thenReturnsAllItems")
    void testGetAllTodoItemsReturnsItems() {
        // Arrange : Préparer les items
        Instant now = Instant.now();
        TodoItem item1 = new TodoItem("item-1", now, "Item 1");
        TodoItem item2 = new TodoItem("item-2", now, "Item 2");
        List<TodoItem> expectedItems = Arrays.asList(item1, item2);

        when(loadTodoItemPort.loadAllTodoItems()).thenReturn(expectedItems);

        // Act : Appeler le service
        List<TodoItem> result = getTodoItemsService.getAllTodoItems();

        // Assert : Vérifier que les items sont retournés
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("givenItemsWithLateContent_whenGetAllTodoItems_thenAppliesRG1")
    void testGetAllTodoItemsAppliesRG1() {
        // Arrange : Créer un item avec finalContent() qui ajoute [LATE!]
        Instant twoHoursAgo = Instant.now().minusSeconds(48 * 3600);
        TodoItem item = new TodoItem("item-late", twoHoursAgo, "Payer facture");

        when(loadTodoItemPort.loadAllTodoItems()).thenReturn(Arrays.asList(item));

        // Act : Appeler le service
        List<TodoItem> result = getTodoItemsService.getAllTodoItems();

        // Assert : Vérifier que finalContent() a été appelée et [LATE!] est ajouté
        assertTrue(result.get(0).getContent().startsWith("[LATE!]"));
    }
}

