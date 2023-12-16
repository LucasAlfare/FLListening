@file:Suppress("MemberVisibilityCanBePrivate")

package com.lucasalfare.fllistening

/**
 * A base class for managing events and listeners.
 */
abstract class EventManageable {
  /**
   * A volatile flag indicating whether the instance has been initialized.
   * The use of 'volatile' ensures that reads and writes to this property are atomic,
   * preventing potential visibility issues in a multi-threaded environment.
   */
  @Volatile
  var initialized = false

  /**
   * List to store registered listeners
   */
  internal val listeners = mutableListOf<EventManageable>()

  /**
   * Abstract method to be implemented by subclasses for initialization logic.
   */
  abstract suspend fun initialize()

  /**
   * Notifies all registered listeners about the occurrence of an event.
   *
   * @param event The event to notify about.
   * @param data Additional data associated with the event (optional).
   */
  fun notifyListeners(event: Any, data: Any? = null) {
    listeners.forEach { it.onEvent(event, data) }
  }

  /**
   * Abstract method to be implemented by subclasses for handling events.
   *
   * @param event The event to handle.
   * @param data Additional data associated with the event (optional).
   */
  abstract fun onEvent(event: Any, data: Any?)

  /**
   * Adds a listener to the list of registered listeners if not already present.
   *
   * @param listener The listener to add.
   */
  fun addListener(listener: EventManageable) {
    if (!listeners.contains(listener)) {
      listeners += listener
    }
  }

  /**
   * Removes a listener from the list of registered listeners.
   *
   * @param listener The listener to remove.
   */
  fun removeListener(listener: EventManageable) {
    listeners -= listener
  }
}