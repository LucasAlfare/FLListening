@file:Suppress("MemberVisibilityCanBePrivate")

package com.lucasalfare.fllistener

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


/**
 * This type represents a standard type of event description
 * to transit over the application flow.
 *
 * Even the library accepts events of type [Any], is recommended
 * to use this type instead.
 */
data class AppEvent(val identifier: Any)

/**
 * Creates an [AppEvent] instance with the supplied [identifier].
 */
fun eventFactory(identifier: Any) = AppEvent(identifier)


/**
 * This class is used to make children to be able to receive events from other [EventManageable]
 * instances. Also, this class is used to make any children be able to propagate events to other
 * interested [EventManageable] instances.
 *
 * All interested instances must be added to "this" in order to make then
 * receive the notifications fired from here.
 */
abstract class EventManageable {

  /**
   * Flags this manager as initiated or not initiated.
   */
  var initiated = false

  /**
   * Holds all current objects that are listening to this instance.
   */
  var listeners = mutableListOf<EventManageable>()

  /**
   * Method that performs a custom initialization for this manager.
   *
   * Normally this is the first function to be called, after
   * constructor and/or native init blocks.
   *
   * Also, this method checks, indefinitely for the initialization
   * of this manager by checking the [initiated] field. This is done
   * in order to switch between appropriated methods that runs when
   * this manager is not initiated and when this local managers is
   * finnally initiated.
   */
  internal fun initialize() {
    while (true) {
      if (!initiated) {
        onNotInitiated()
      } else {
        onInitiated()
        break
      }
    }
  }

  /**
   * This method is called infinite/indefinitely while this manager
   * don't flag itself as initiated through the field [initiated].
   */
  abstract fun onNotInitiated()

  /**
   * This method is called always that this manager flag itself as
   * initiated, through the field [initiated].
   */
  abstract fun onInitiated()

  /**
   * Used to handle any kind of incoming event/data from outside
   * the instance.
   */
  abstract fun onEvent(event: Any, data: Any?, origin: Any?)

  /**
   * Takes any object that can listen/handle events and adds it in this instance.
   * It will only add if is not added yet.
   */
  fun addListener(aListener: EventManageable) {
    if (!listeners.contains(aListener)) {
      listeners.add(aListener)
    }
  }

  fun removeListener(aListener: EventManageable) {
    listeners -= aListener
  }

  /**
   * Function that pass its params to all previously added listeners of this instance.
   *
   * Params:
   * - [event]: the object descriptor associated to the fired event
   * - [data]: any kind of data that can be associated to this event
   * - [origin]: optional field to hold the instance from where this event was fired
   */
  fun notifyListeners(event: Any, data: Any? = null, origin: Any? = null) {
    listeners.forEach {
      it.onEvent(event, data, origin)
    }
  }
}

/**
 * Custom implementation of a class that can be listened and emit
 * events to others.
 *
 * The appropriated use of this class is to work together programatic/declarative
 * UI components in order to make then emit application events and receive
 * then as well.
 */
class UIManager : EventManageable() {

  private val callbacks = mutableListOf<(Any, Any?) -> Unit>()

  override fun onNotInitiated() {
    initiated = true
  }

  override fun onInitiated() {

  }

  override fun onEvent(event: Any, data: Any?, origin: Any?) {
    callbacks.forEach { callback ->
      callback(event, data)
    }
  }

  fun addCallback(callback: (Any, Any?) -> Unit): (Any, Any?) -> Unit {
    if (!callbacks.contains(callback)) callbacks.add(callback)
    return callback
  }

  fun removeCallback(callback: (Any, Any?) -> Unit) {
    callbacks.remove(callback)
  }
}

/**
 * Custom helper method to define relationship between different
 * event manageables instances. This method also initializes all the
 * managers synchronously.
 */
suspend fun setupManagers(vararg managers: EventManageable) {
  // attaches all the managers to each other
  managers.forEach { m1 ->
    managers.forEach { m2 ->
      if (m2 != m1) {
        m1.addListener(m2)
      }
    }
  }

  // performs asynchronous initializations to all the managers
  coroutineScope {
    managers.forEach { manager ->
      launch {
        manager.initialize()
      }
    }
  }
}
