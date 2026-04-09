package com.ibm.ads.metadata.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.ibm.ads.metadata.model.DecisionMetadata;

/**
 * Unit tests for DecisionRepository
 */
class DecisionRepositoryTest {

    @Test
    void testGetAllDecisions() {
        DecisionRepository repository = DecisionRepository.getInstance();
        List<DecisionMetadata> decisions = repository.getAllDecisions();
        assertNotNull(decisions);
        assertEquals(3, decisions.size());
    }

    @Test
    void testGetDecisionById() {
        DecisionRepository repository = DecisionRepository.getInstance();
        DecisionMetadata decision = repository.getDecisionById("TEST-001");
        assertNotNull(decision);
        assertEquals("Test Decision 1", decision.getName());
    }

    @Test
    void testGetDecisionByIdNotFound() {
        DecisionRepository repository = DecisionRepository.getInstance();
        DecisionMetadata decision = repository.getDecisionById("DEC-999");
        assertNull(decision);
    }

    @Test
    void testSearchDecisionsByName() {
        DecisionRepository repository = DecisionRepository.getInstance();
        List<DecisionMetadata> decisions = repository.searchDecisionsByName("Test Decision 3");
        assertEquals(1, decisions.size());
        assertEquals("TEST-003", decisions.get(0).getId());
    }

    @Test
    void testGetDecisionCount() {
        DecisionRepository repository = DecisionRepository.getInstance();
        int count = repository.getDecisionCount();
        assertEquals(3, count);
    }

}

// Made with Bob
