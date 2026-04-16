package com.acme.todolist.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests unitaires pour la règle de gestion RG1 : affichage du préfixe [LATE!]
 * pour les items datés de plus de 24h
 */
@DisplayName("TodoItem - Règle de gestion RG1")
class TodoItemTest {

    @Test
    @DisplayName("givenItemCreatedMoreThan24HoursAgo_whenFinalContent_thenPrefixedWithLate")
    void testFinalContentWithLateItem() {
        // Arrange : Créer un item datant de 48h ago (plus de 24h)
        Instant twoHoursAgo = Instant.now().minus(48, ChronoUnit.HOURS);
        TodoItem item = new TodoItem("item-1", twoHoursAgo, "Payer facture");

        // Act : Appeler finalContent()
        String result = item.finalContent();

        // Assert : Vérifier que le préfixe [LATE!] est bien ajouté
        assertTrue(result.startsWith("[LATE!] "), "Le contenu doit commencer par [LATE!]");
        assertEquals("[LATE!] Payer facture", result);
    }

    @Test
    @DisplayName("givenItemCreatedLessThan24HoursAgo_whenFinalContent_thenNotPrefixedWithLate")
    void testFinalContentWithRecentItem() {
        // Arrange : Créer un item datant de 1h ago (moins de 24h)
        Instant oneHourAgo = Instant.now().minus(1, ChronoUnit.HOURS);
        TodoItem item = new TodoItem("item-2", oneHourAgo, "Faire les courses");

        // Act : Appeler finalContent()
        String result = item.finalContent();

        // Assert : Vérifier que le préfixe [LATE!] n'est pas ajouté
        assertFalse(result.startsWith("[LATE!]"), "Le contenu ne doit pas commencer par [LATE!]");
        assertEquals("Faire les courses", result);
    }

    @Test
    @DisplayName("givenItemCreatedJustAfter24Hours_whenFinalContent_thenPrefixedWithLate")
    void testFinalContentWithJustAfter24HoursItem() {
        // Arrange : Créer un item datant de 24h + 1 minute ago
        Instant justAfterTwentyFourHours = Instant.now().minus(24, ChronoUnit.HOURS).minus(1, ChronoUnit.MINUTES);
        TodoItem item = new TodoItem("item-4", justAfterTwentyFourHours, "Rencontrer client");

        // Act : Appeler finalContent()
        String result = item.finalContent();

        // Assert : Vérifier que le préfixe [LATE!] est ajouté
        assertTrue(result.startsWith("[LATE!]"), "Un item > 24h doit être marqué comme late");
    }
}


