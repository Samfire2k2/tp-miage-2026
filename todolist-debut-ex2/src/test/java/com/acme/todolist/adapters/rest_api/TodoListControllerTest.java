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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.acme.todolist.application.port.in.AddTodoItem;
import com.acme.todolist.application.port.in.GetTodoItems;
import com.acme.todolist.domain.TodoItem;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Tests d'intégration pour le contrôleur REST TodoListController
 * Utilise @WebMvcTest pour tester les endpoints sans lancer toute l'application
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
        // Préparation
        String jsonBody = "{\"id\":\"item-1\",\"time\":\"2026-03-11T14:30:00Z\",\"content\":\"Faire les courses\"}";

        // Exécution et vérification
        mockMvc.perform(post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
            .andExpect(status().isCreated())
            .andExpect(content().string(""));
    }

    @Test
    @DisplayName("givenGetRequest_whenGetTodos_thenReturns200OkWithItems")
    void testGetTodoItemsReturns200() throws Exception {
        // Préparation
        Instant now = Instant.now();
        List<TodoItem> items = Arrays.asList(
            new TodoItem("item-1", now, "Item 1"),
            new TodoItem("item-2", now, "Item 2")
        );
        when(getTodoItemsPort.getAllTodoItems()).thenReturn(items);

        // Exécution et vérification
        mockMvc.perform(get("/todos"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("givenMalformedJson_whenPostTodos_thenReturns400BadRequest")
    void testCreateTodoItemWithMalformedJson() throws Exception {
        // Préparation
        String malformedJson = "{\"id\":\"item-1\",\"time\":\"invalid-date\",}";

        // Exécution et vérification
        mockMvc.perform(post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(malformedJson))
            .andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("givenValidItem_whenPostTodos_thenPortReceivesCorrectData")
    void testCreateTodoItemPortReceivesCorrectData() throws Exception {
        // Préparation
        String jsonBody = "{\"id\":\"specific-id\",\"time\":\"2020-02-27T10:31:43Z\",\"content\":\"Specific content\"}";

        // Exécution
        mockMvc.perform(post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
            .andExpect(status().isCreated());

        // Vérification
        verify(addTodoItemPort).addTodoItem(argThat(
            item -> item.getId().equals("specific-id") &&
                   item.getContent().equals("Specific content")
        ));
    }
}

