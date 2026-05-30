package com.busguidance;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// Unit Test Cases for Bus Class
public class BusUnitTest {

    // ── B1: Bus ID Rules ──────────────────────────────────────────────────────

    // B1 Valid 8-digit bus ID should be accepted
    @Test
    void testB1_ValidBusID_ShouldReturnTrue() {
        assertTrue(Bus.isValidBusID("12345678"));
    }

    // B1 Bus ID shorter than 8 characters should be rejected
    @Test
    void testB1_TooShortID_ShouldReturnFalse() {
        assertFalse(Bus.isValidBusID("1234567"));
    }

    // B1 Bus ID longer than 8 characters should be rejected
    @Test
    void testB1_TooLongID_ShouldReturnFalse() {
        assertFalse(Bus.isValidBusID("123456789"));
    }

    // B1 Bus ID containing non-digit characters should be rejected
    @Test
    void testB1_ContainsLetters_ShouldReturnFalse() {
        assertFalse(Bus.isValidBusID("1234567A"));
    }

    // B1 Constructor should throw IllegalArgumentException for invalid bus ID
    @Test
    void testB1_InvalidID_ConstructorShouldThrow() {
        assertThrows(IllegalArgumentException.class, () ->
                new Bus("INVALID!", 40, 80.0, "Diesel"));
    }

    // B1 Bus ID of all zeros should be accepted (valid 8-digit boundary)
    @Test
    void testB1_AllZerosID_ShouldReturnTrue() {
        assertTrue(Bus.isValidBusID("00000000"));
    }

    // ── B2: Capacity Update Restriction ──────────────────────────────────────

    // B2 Decreasing capacity via setCapacity should be allowed
    @Test
    void testB2_DecreaseCapacity_ShouldSucceed() {
        Bus bus = new Bus("12345678", 60, 80.0, "Diesel");
        bus.setCapacity(50);
        assertEquals(50, bus.getCapacity());
    }

    // B2 Increasing capacity via setCapacity should throw IllegalArgumentException
    @Test
    void testB2_IncreaseCapacity_ShouldThrow() {
        Bus bus = new Bus("12345678", 40, 80.0, "Diesel");
        assertThrows(IllegalArgumentException.class, () ->
                bus.setCapacity(50));
    }

    // B2 Setting capacity to the same value should be allowed
    @Test
    void testB2_SameCapacity_ShouldSucceed() {
        Bus bus = new Bus("12345678", 40, 80.0, "Diesel");
        bus.setCapacity(40);
        assertEquals(40, bus.getCapacity());
    }

    // ── B3: Driver Age Restriction ────────────────────────────────────────────

    // B3 Driver aged exactly 50 on a bus with capacity exactly 50 should be eligible
    @Test
    void testB3_DriverAge50_Capacity50_ShouldReturnTrue() {
        Driver driver = new Driver("23@#abcdAB", "John", 10,
                "Heavy", "12|Main St|Melbourne|VIC|Australia", "15-06-1974");
        Bus bus = new Bus("12345678", 50, 80.0, "Diesel");
        assertTrue(bus.isDriverEligible(driver));
    }

    // B3 Driver older than 50 on a bus with capacity 50 or more should not be eligible
    @Test
    void testB3_DriverOver50_Capacity50_ShouldReturnFalse() {
        Driver driver = new Driver("23@#abcdAB", "John", 10,
                "Heavy", "12|Main St|Melbourne|VIC|Australia", "15-06-1973");
        Bus bus = new Bus("12345678", 50, 80.0, "Diesel");
        assertFalse(bus.isDriverEligible(driver));
    }

    // B3 Driver older than 50 on a bus with capacity below 50 should be eligible
    @Test
    void testB3_DriverOver50_CapacityUnder50_ShouldReturnTrue() {
        Driver driver = new Driver("23@#abcdAB", "John", 10,
                "Heavy", "12|Main St|Melbourne|VIC|Australia", "15-06-1970");
        Bus bus = new Bus("12345678", 49, 80.0, "Diesel");
        assertTrue(bus.isDriverEligible(driver));
    }

    // ── B4: Electric Bus Restriction ─────────────────────────────────────────

    // B4 Driver with at least 5 years experience can drive an electric bus
    @Test
    void testB4_ExperienceAtLeast5_Electric_ShouldReturnTrue() {
        Driver driver = new Driver("23@#abcdAB", "John", 5,
                "Heavy", "12|Main St|Melbourne|VIC|Australia", "15-06-1990");
        Bus bus = new Bus("12345678", 40, 100.0, "Electricity");
        assertTrue(bus.isDriverEligible(driver));
    }

    // B4 Driver with less than 5 years experience cannot drive an electric bus
    @Test
    void testB4_ExperienceUnder5_Electric_ShouldReturnFalse() {
        Driver driver = new Driver("23@#abcdAB", "John", 4,
                "Heavy", "12|Main St|Melbourne|VIC|Australia", "15-06-1990");
        Bus bus = new Bus("12345678", 40, 100.0, "Electricity");
        assertFalse(bus.isDriverEligible(driver));
    }

    // B4 Driver with exactly 5 years experience is on the boundary and should be eligible
    @Test
    void testB4_ExperienceExactly5_Electric_ShouldReturnTrue() {
        Driver driver = new Driver("23@#abcdAB", "John", 5,
                "Heavy", "12|Main St|Melbourne|VIC|Australia", "15-06-1990");
        Bus bus = new Bus("12345678", 40, 100.0, "Electricity");
        assertTrue(bus.isDriverEligible(driver));
    }

    // ── B5: Driver Licence Restriction ───────────────────────────────────────

    // B5 Driver with Heavy licence can operate an electric bus
    @Test
    void testB5_HeavyLicence_Electric_ShouldReturnTrue() {
        Driver driver = new Driver("23@#abcdAB", "John", 5,
                "Heavy", "12|Main St|Melbourne|VIC|Australia", "15-06-1990");
        Bus bus = new Bus("12345678", 40, 100.0, "Electricity");
        assertTrue(bus.isDriverEligible(driver));
    }

    // B5 Driver with PublicTransport licence can operate a hybrid bus
    @Test
    void testB5_PublicTransportLicence_Hybrid_ShouldReturnTrue() {
        Driver driver = new Driver("23@#abcdAB", "John", 5,
                "PublicTransport", "12|Main St|Melbourne|VIC|Australia", "15-06-1990");
        Bus bus = new Bus("12345678", 40, 80.0, "Hybrid");
        assertTrue(bus.isDriverEligible(driver));
    }

    // B5 Driver with Light licence cannot operate an electric bus
    @Test
    void testB5_LightLicence_Electric_ShouldReturnFalse() {
        Driver driver = new Driver("23@#abcdAB", "John", 5,
                "Light", "12|Main St|Melbourne|VIC|Australia", "15-06-1990");
        Bus bus = new Bus("12345678", 40, 100.0, "Electricity");
        assertFalse(bus.isDriverEligible(driver));
    }

    // B5 Driver with Medium licence cannot operate a hybrid bus
    @Test
    void testB5_MediumLicence_Hybrid_ShouldReturnFalse() {
        Driver driver = new Driver("23@#abcdAB", "John", 5,
                "Medium", "12|Main St|Melbourne|VIC|Australia", "15-06-1990");
        Bus bus = new Bus("12345678", 40, 80.0, "Hybrid");
        assertFalse(bus.isDriverEligible(driver));
    }

    // B5 Driver with Light licence can operate a diesel bus (no restriction applies)
    @Test
    void testB5_LightLicence_Diesel_ShouldReturnTrue() {
        Driver driver = new Driver("23@#abcdAB", "John", 5,
                "Light", "12|Main St|Melbourne|VIC|Australia", "15-06-1990");
        Bus bus = new Bus("12345678", 40, 80.0, "Diesel");
        assertTrue(bus.isDriverEligible(driver));
    }
}