package com.busguidance;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;

// Integration tests for DriverRepository using real TXT files.
// Tests verify that drivers are stored, retrieved, updated, and counted correctly.
public class DriverIntegrationTest {

    private DriverRepository repo;

    // Valid test driver data
    private static final String VALID_ID = "23@#abcdAB";
    private static final String VALID_NAME = "John Smith";
    private static final int VALID_EXPERIENCE = 5;
    private static final String VALID_LICENSE = "Heavy";
    private static final String VALID_ADDRESS = "12|Main St|Melbourne|VIC|Australia";
    private static final String VALID_BIRTHDATE = "15-06-1990";

    // Set up a fresh repository before each test
    @BeforeEach
    void setUp() {
        repo = new DriverRepository();
        repo.clearAll(); // start with empty TXT file
    }

    // Clean up TXT file after each test
    @AfterEach
    void tearDown() {
        repo.clearAll();
    }

    // Integration Test 1: Valid driver is stored correctly in TXT file
    @Test
    void testIT1_ValidDriver_StoredCorrectly() throws IOException {
        Driver driver = new Driver(VALID_ID, VALID_NAME, VALID_EXPERIENCE,
                VALID_LICENSE, VALID_ADDRESS, VALID_BIRTHDATE);
        boolean result = repo.add(driver);
        assertTrue(result);

        // Verify it can be retrieved from the TXT file
        Driver retrieved = repo.retrieve(VALID_ID);
        assertNotNull(retrieved);
        assertEquals(VALID_ID, retrieved.getDriverID());
        assertEquals(VALID_NAME, retrieved.getName());
        assertEquals(VALID_EXPERIENCE, retrieved.getExperienceYears());
        assertEquals(VALID_LICENSE, retrieved.getLicenseType());
        assertEquals(VALID_ADDRESS, retrieved.getAddress());
        assertEquals(VALID_BIRTHDATE, retrieved.getBirthdate());
    }

    // Integration Test 2: Duplicate driver ID is rejected
    @Test
    void testIT2_DuplicateDriverID_IsRejected() throws IOException {
        Driver driver1 = new Driver(VALID_ID, VALID_NAME, VALID_EXPERIENCE,
                VALID_LICENSE, VALID_ADDRESS, VALID_BIRTHDATE);
        repo.add(driver1);

        // Try to add another driver with the same ID
        Driver driver2 = new Driver(VALID_ID, "Jane Doe", 3,
                "Light", "10|Queen St|Sydney|NSW|Australia", "20-01-1995");
        assertThrows(IllegalArgumentException.class, () -> repo.add(driver2));
    }

    // Integration Test 3: Update is persisted correctly in TXT file
    @Test
    void testIT3_Update_PersistedCorrectly() throws IOException {
        Driver driver = new Driver(VALID_ID, VALID_NAME, VALID_EXPERIENCE,
                VALID_LICENSE, VALID_ADDRESS, VALID_BIRTHDATE);
        repo.add(driver);

        // Update the driver with new details
        Driver updatedDriver = new Driver(VALID_ID, VALID_NAME, 8,
                VALID_LICENSE, "99|King St|Brisbane|QLD|Australia", VALID_BIRTHDATE);
        boolean result = repo.update(updatedDriver);
        assertTrue(result);

        // Verify update was saved to TXT file
        Driver retrieved = repo.retrieve(VALID_ID);
        assertNotNull(retrieved);
        assertEquals(8, retrieved.getExperienceYears());
        assertEquals("99|King St|Brisbane|QLD|Australia", retrieved.getAddress());
    }

    // Integration Test 4: Record count is updated correctly
    @Test
    void testIT4_RecordCount_UpdatedCorrectly() throws IOException {
        assertEquals(0, repo.count());

        // Add first driver
        Driver driver1 = new Driver(VALID_ID, VALID_NAME, VALID_EXPERIENCE,
                VALID_LICENSE, VALID_ADDRESS, VALID_BIRTHDATE);
        repo.add(driver1);
        assertEquals(1, repo.count());

        // Add second driver
        Driver driver2 = new Driver("34@#abcdCD", "Jane Doe", 3,
                "Light", "10|Queen St|Sydney|NSW|Australia", "20-01-1995");
        repo.add(driver2);
        assertEquals(2, repo.count());
    }

    // Integration Test 5: Invalid driver is rejected and not stored
    @Test
    void testIT5_InvalidDriver_NotStored() throws IOException {
        // Try to create a driver with invalid ID - should throw before reaching repo
        assertThrows(IllegalArgumentException.class, () -> {
            Driver invalidDriver = new Driver("INVALID", VALID_NAME, VALID_EXPERIENCE,
                    VALID_LICENSE, VALID_ADDRESS, VALID_BIRTHDATE);
            repo.add(invalidDriver);
        });

        // Verify nothing was stored
        assertEquals(0, repo.count());
    }

    // Integration Test 6: Update non-existent driver returns false
    @Test
    void testIT6_UpdateNonExistentDriver_ReturnsFalse() throws IOException {
        Driver driver = new Driver(VALID_ID, VALID_NAME, VALID_EXPERIENCE,
                VALID_LICENSE, VALID_ADDRESS, VALID_BIRTHDATE);
        boolean result = repo.update(driver);
        assertFalse(result);
    }
}