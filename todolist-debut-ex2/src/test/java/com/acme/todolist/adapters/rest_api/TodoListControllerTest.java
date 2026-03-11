package com.acme.todolist.adapters.rest_api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.acme.todolist.application.port.in.AddTodoItem;
import com.acme.todolist.application.port.in.GetTodoItems;
import com.acme.todolist.domain.TodoItem;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Tests intégration pour le contrôleur REST TodoListController
 * Utilise @WebMvcTest pour tester les endpoints sans lancer toute l'application
 *
 * @author étudiant
 */
@WebMvcTest(controllers = TodoListController.class)
@DisplayName("TodoListController - API REST")
class TodoListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AddTodoItem addTodoItemPort;

    @MockBean
    private GetTodoItems getTodoItemsPort;

    @Test
    @DisplayName("givenValidJsonBody_whenPostTodos_thenReturns201Created")
    void testCreateTodoItemReturns201() throws Exception {
        // Arrange : Préparer le JSON
        String jsonBody = """
            {
                "id": "item-1",
                "time": "2026-03-11T14:30:00Z",
                "content": "Faire les courses"
            }
            """;

        // Act & Assert : Envoyer la requête POST et vérifier le code 201
        mockMvc.perform(post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
            .andExpect(status().isCreated())
            .andExpect(content().string(""));
    }

    @Test
    @DisplayName("givenValidJsonBody_whenPostTodos_thenCallsAddTodoItemPort")
    void testCreateTodoItemCallsPort() throws Exception {
        // Arrange : Préparer le JSON
        String jsonBody = """
            {
                "id": "item-test",
                "time": "2026-03-11T14:30:00Z",
                "content": "Test content"
            }
            """;

        // Act : Envoyer la requête POST
        mockMvc.perform(post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
            .andExpect(status().isCreated());

        // Assert : Vérifier que le port est appelé une fois
        verify(addTodoItemPort, times(1)).addTodoItem(any(TodoItem.class));
    }

    @Test
    @DisplayName("givenGetRequest_whenGetTodos_thenReturns200OkWithItems")
    void testGetTodoItemsReturns200() throws Exception {
        // Arrange : Préparer les items à retourner
        Instant now = Instant.now();
        List<TodoItem> items = Arrays.asList(
            new TodoItem("item-1", now, "Item 1"),
            new TodoItem("item-2", now, "Item 2")
        );
        when(getTodoItemsPort.getAllTodoItems()).thenReturn(items);

        // Act & Assert : Envoyer la requête GET et vérifier la réponse
        mockMvc.perform(get("/todos"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("givenEmptyList_whenGetTodos_thenReturns200OkWithEmptyArray")
    void testGetTodoItemsWithEmptyList() throws Exception {
        // Arrange : Retourner une liste vide
        when(getTodoItemsPort.getAllTodoItems()).thenReturn(Arrays.asList());

        // Act & Assert : Envoyer la requête GET et vérifier la réponse
        mockMvc.perform(get("/todos"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("givenItemsWithLateOnes_whenGetTodos_thenReturnsItemsWithLatePrefix")
    void testGetTodoItemsWithLateItems() throws Exception {
        // Arrange : Créer des items, certains avec le préfixe [LATE!]
        Instant now = Instant.now();
        Instant twoHoursAgo = Instant.now().minusSeconds(48 * 3600);

        TodoItem recentItem = new TodoItem("item-1", now, "Tâche récente");
        TodoItem lateItem = new TodoItem("item-2", twoHoursAgo, "[LATE!] Tâche en retard");

        List<TodoItem> items = Arrays.asList(recentItem, lateItem);
        when(getTodoItemsPort.getAllTodoItems()).thenReturn(items);

        // Act & Assert : Vérifier que le préfixe [LATE!] est dans la réponse
        mockMvc.perform(get("/todos"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[1].content").value("[LATE!] Tâche en retard"));
    }

    @Test
    @DisplayName("givenMalformedJson_whenPostTodos_thenReturns400BadRequest")
    void testCreateTodoItemWithMalformedJson() throws Exception {
        // Arrange : JSON mal formé
        String malformedJson = """
            {
                "id": "item-1",
                "time": "invalid-date",
            }
            """;

        // Act & Assert : Vérifier que la requête retourne 400
        mockMvc.perform(post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(malformedJson))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("givenEmptyJsonBody_whenPostTodos_thenHandlesGracefully")
    void testCreateTodoItemWithEmptyBody() throws Exception {
        // Act & Assert : Envoyer un body vide
        mockMvc.perform(post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("givenValidItem_whenPostTodos_thenPortReceivesCorrectData")
    void testCreateTodoItemPortReceivesCorrectData() throws Exception {
        // Arrange
        String jsonBody = """
            {
                "id": "specific-id",
                "time": "2020-02-27T10:31:43Z",
                "content": "Specific content"
            }
            """;

        // Act
        mockMvc.perform(post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
            .andExpect(status().isCreated());

        // Assert : Vérifier que le port reçoit l'item avec les bonnes valeurs
        verify(addTodoItemPort).addTodoItem(argThat(
            item -> item.getId().equals("specific-id") &&
                   item.getContent().equals("Specific content")
        ));
    }
}

