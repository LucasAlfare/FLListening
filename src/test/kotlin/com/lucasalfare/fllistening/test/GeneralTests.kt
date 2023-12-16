package com.lucasalfare.fllistening.test

import com.lucasalfare.fllistening.EventManageable
import com.lucasalfare.fllistening.setupManagers
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import kotlin.test.Test

class GeneralTests {

  /**
   * Test the initialization of the EventManageable instance.
   * Verifies that the 'initialize' method sets the 'initialized' flag to true.
   */
  @Test
  fun `test EventManageable initialization`() {
    // Arrange
    val eventManager = object : EventManageable() {
      override suspend fun initialize() {
        initialized = true
      }

      override fun onEvent(event: Any, data: Any?) {
        // Implementation not needed for this test
      }
    }

    // Act
    runBlocking {
      eventManager.initialize()
    }

    // Assert
    assertTrue(eventManager.initialized)
  }

  /**
   * Test the addition of listeners to an EventManageable instance.
   * Verifies that adding a listener results in the listener being present in the 'listeners' list.
   */
  @Test
  fun `test EventManager listeners`() {
    // Arrange
    val eventManager1 = object : EventManageable() {
      override suspend fun initialize() {
        // Implementation not needed for this test
      }

      override fun onEvent(event: Any, data: Any?) {
        // Implementation not needed for this test
      }
    }

    val eventManager2 = object : EventManageable() {
      override suspend fun initialize() {
        // Implementation not needed for this test
      }

      override fun onEvent(event: Any, data: Any?) {
        // Implementation not needed for this test
      }
    }

    // Act
    eventManager1.addListener(eventManager2)

    // Assert
    assertTrue(eventManager1.listeners.contains(eventManager2))
  }

  /**
   * Test the setup of multiple EventManageable instances.
   * Verifies that setupManagers correctly establishes listener relationships and initializes each manager.
   */
  @Test
  fun `test setupManagers`() {
    // Arrange
    val eventManager1 = object : EventManageable() {
      override suspend fun initialize() {
        initialized = true
      }

      override fun onEvent(event: Any, data: Any?) {
        // Implementation not needed for this test
      }
    }

    val eventManager2 = object : EventManageable() {
      override suspend fun initialize() {
        initialized = true
      }

      override fun onEvent(event: Any, data: Any?) {
        // Implementation not needed for this test
      }
    }

    // Act
    runBlocking {
      setupManagers(eventManager1, eventManager2)
    }

    // Assert
    assertTrue(eventManager1.listeners.contains(eventManager2))
    assertTrue(eventManager2.listeners.contains(eventManager1))
    assertTrue(eventManager1.initialized)
    assertTrue(eventManager2.initialized)
  }

  /**
   * Test event notification between two EventManageable instances.
   * Verifies that when one manager notifies listeners of an event, the other manager correctly receives
   * the event and associated data in their 'onEvent' method.
   */
  @Test
  fun `test event notification`() {
    // Arrange
    val eventManager1 = object : EventManageable() {
      override suspend fun initialize() {
        // Implementation not needed for this test
      }

      override fun onEvent(event: Any, data: Any?) {
        // Assert that the event and data are received correctly
        assertEquals("TestEvent", event)
        assertEquals("TestData", data)
      }
    }

    val eventManager2 = object : EventManageable() {
      override suspend fun initialize() {
        // Implementation not needed for this test
      }

      override fun onEvent(event: Any, data: Any?) {
        // Assert that the event and data are received correctly
        assertEquals("TestEvent", event)
        assertEquals("TestData", data)
      }
    }

    // Act
    runBlocking {
      setupManagers(eventManager1, eventManager2)

      // Notify listeners of the event
      eventManager1.notifyListeners("TestEvent", "TestData")
    }
  }
}