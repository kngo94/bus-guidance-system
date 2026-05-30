package com.busguidance;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;

// Integration tests for BusRepository using real TXT files.
// Tests verify that buses are stored, retrieved, updated, and counted correctly.
public class BusIntegrationTest {

    private BusRepository repo;

    // Valid test bus data
    private static final String VALID_BUS_ID = "12345678";
    private static final int VALID_CAPACITY = 40;
    private static final double VALID_FUEL_LEVEL = 75.0;
    private static final String VALID_FUEL_TYPE = "Diesel";

    // Set up a fresh repository before each test
    @BeforeEach
    void setUp() {
        repo = new BusRepository();
        repo.clearAll(); // start with empty TXT file
    }

    // Clean up TXT file after each test
    @AfterEach
    void tearDown() {
        repo.clearAll();
    }

    // Integration Test 1: Valid bus is stored correctly in TXT file
    @Test
    void testIT1_ValidBus_StoredCorrectly() throws IOException {
        Bus bus = new Bus(VALID_BUS_ID, VALID_CAPACITY, VALID_FUEL_LEVEL, VALID_FUEL_TYPE);
        boolean result = repo.add(bus);
        assertTrue(result);

        // Verify it can be retrieved from the TXT file
        Bus retrieved = repo.retrieve(VALID_BUS_ID);
        assertNotNull(retrieved);
        assertEquals(VALID_BUS_ID, retrieved.getBusID());
        assertEquals(VALID_CAPACITY, retrieved.getCapacity());
        assertEquals(VALID_FUEL_LEVEL, retrieved.getFuelLevel());
        assertEquals(VALID_FUEL_TYPE, retrieved.getFuelType());
    }

    // Integration Test 2: Duplicate bus ID is rejected
    @Test
    void testIT2_DuplicateBusID_IsRejected() throws IOException {
        Bus bus1 = new Bus(VALID_BUS_ID, VALID_CAPACITY, VALID_FUEL_LEVEL, VALID_FUEL_TYPE);
        repo.add(bus1);

        // Try to add another bus with the same ID
        Bus bus2 = new Bus(VALID_BUS_ID, 30, 50.0, "Hybrid");
        assertThrows(IllegalArgumentException.class, () -> repo.add(bus2));
    }

    // Integration Test 3: Update is persisted correctly in TXT file
    @Test
    void testIT3_Update_PersistedCorrectly() throws IOException {
        Bus bus = new Bus(VALID_BUS_ID, VALID_CAPACITY, VALID_FUEL_LEVEL, VALID_FUEL_TYPE);
        repo.add(bus);

        // Update the bus with new details - capacity decreased
        Bus updatedBus = new Bus(VALID_BUS_ID, 30, 50.0, "Diesel");
        boolean result = repo.update(updatedBus);
        assertTrue(result);

        // Verify update was saved to TXT file
        Bus retrieved = repo.retrieve(VALID_BUS_ID);
        assertNotNull(retrieved);
        assertEquals(30, retrieved.getCapacity());
        assertEquals(50.0, retrieved.getFuelLevel());
    }

    // Integration Test 4: Record count is updated correctly
    @Test
    void testIT4_RecordCount_UpdatedCorrectly() throws IOException {
        assertEquals(0, repo.count());

        // Add first bus
        Bus bus1 = new Bus(VALID_BUS_ID, VALID_CAPACITY, VALID_FUEL_LEVEL, VALID_FUEL_TYPE);
        repo.add(bus1);
        assertEquals(1, repo.count());

        // Add second bus
        Bus bus2 = new Bus("87654321", 50, 60.0, "Hybrid");
        repo.add(bus2);
        assertEquals(2, repo.count());
    }

    // Integration Test 5: Invalid bus is rejected and not stored
    @Test
    void testIT5_InvalidBus_NotStored() throws IOException {
        // Try to create a bus with invalid ID - should throw before reaching repo
        assertThrows(IllegalArgumentException.class, () -> {
            Bus invalidBus = new Bus("INVALID", VALID_CAPACITY, VALID_FUEL_LEVEL, VALID_FUEL_TYPE);
            repo.add(invalidBus);
        });

        // Verify nothing was stored
        assertEquals(0, repo.count());
    }

    // Integration Test 6: Capacity increase during update is rejected.
    @Test
    void testIT6_CapacityIncrease_IsRejected() throws IOException {
        Bus bus = new Bus(VALID_BUS_ID, VALID_CAPACITY, VALID_FUEL_LEVEL, VALID_FUEL_TYPE);
        repo.add(bus);

        // Try to increase capacity - should throw
        assertThrows(IllegalArgumentException.class, () -> {
            Bus updatedBus = new Bus(VALID_BUS_ID, 60, VALID_FUEL_LEVEL, VALID_FUEL_TYPE);
            repo.update(updatedBus);
        });
    }

    // Integration Test 7: Update non-existent bus returns false
    @Test
    void testIT7_UpdateNonExistentBus_ReturnsFalse() throws IOException {
        Bus bus = new Bus(VALID_BUS_ID, VALID_CAPACITY, VALID_FUEL_LEVEL, VALID_FUEL_TYPE);
        boolean result = repo.update(bus);
        assertFalse(result);
    }
}