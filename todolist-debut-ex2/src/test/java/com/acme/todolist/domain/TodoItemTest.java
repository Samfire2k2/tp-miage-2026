package com.acme.todolist.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests unitaires pour la règle de gestion RG1 : affichage du préfixe [LATE!]
 * pour les items datés de plus de 24h
 *
 * @author étudiant
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
        assertTrue(result.contains("Payer facture"), "Le contenu original doit être préservé");
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
        assertEquals("Faire les courses", result, "Le contenu doit être inchangé");
    }

    @Test
    @DisplayName("givenItemCreatedExactly24HoursAgo_whenFinalContent_thenNotPrefixedWithLate")
    void testFinalContentWithExactly24HoursOldItem() {
        // Arrange : Créer un item datant de exactement 24h ago
        Instant exactlyTwentyFourHoursAgo = Instant.now().minus(24, ChronoUnit.HOURS);
        TodoItem item = new TodoItem("item-3", exactlyTwentyFourHoursAgo, "Appeler le médecin");

        // Act : Appeler finalContent()
        String result = item.finalContent();

        // Assert : Vérifier que le préfixe n'est pas ajouté (la limite est > 24h)
        assertFalse(result.startsWith("[LATE!]"), "Un item de exactement 24h ne doit pas être marqué comme late");
        assertEquals("Appeler le médecin", result);
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
        assertTrue(result.contains("Rencontrer client"));
    }

    @Test
    @DisplayName("givenItemWithEmptyContent_whenFinalContent_thenAddsPrefixIfLate")
    void testFinalContentWithEmptyContent() {
        // Arrange : Créer un item en retard avec contenu vide
        Instant twoHoursAgo = Instant.now().minus(48, ChronoUnit.HOURS);
        TodoItem item = new TodoItem("item-5", twoHoursAgo, "");

        // Act : Appeler finalContent()
        String result = item.finalContent();

        // Assert : Vérifier que le préfixe [LATE!] est bien ajouté même avec contenu vide
        assertEquals("[LATE!] ", result, "Le préfixe doit être ajouté même pour un contenu vide");
    }

    @Test
    @DisplayName("givenItemWithSpecialCharacters_whenFinalContent_thenPreservesContent")
    void testFinalContentWithSpecialCharacters() {
        // Arrange : Créer un item en retard avec caractères spéciaux
        Instant twoHoursAgo = Instant.now().minus(48, ChronoUnit.HOURS);
        TodoItem item = new TodoItem("item-6", twoHoursAgo, "Tâche avec @#$% caractères spéciaux");

        // Act : Appeler finalContent()
        String result = item.finalContent();

        // Assert : Vérifier que le contenu spécial est préservé
        assertEquals("[LATE!] Tâche avec @#$% caractères spéciaux", result);
    }

    @Test
    @DisplayName("givenTwoIdenticalItems_whenEquals_thenAreEqual")
    void testEqualsWithIdenticalIds() {
        // Arrange : Créer deux items avec le même ID
        Instant time = Instant.now();
        TodoItem item1 = new TodoItem("item-same", time, "Contenu 1");
        TodoItem item2 = new TodoItem("item-same", time, "Contenu 2");

        // Act & Assert : Vérifier l'égalité
        assertEquals(item1, item2, "Deux items avec le même ID doivent être égaux");
    }

    @Test
    @DisplayName("givenTwoDifferentItems_whenEquals_thenAreNotEqual")
    void testEqualsWithDifferentIds() {
        // Arrange : Créer deux items avec des IDs différents
        Instant time = Instant.now();
        TodoItem item1 = new TodoItem("item-1", time, "Contenu");
        TodoItem item2 = new TodoItem("item-2", time, "Contenu");

        // Act & Assert : Vérifier l'inégalité
        assertNotEquals(item1, item2, "Deux items avec des IDs différents ne doivent pas être égaux");
    }
}

