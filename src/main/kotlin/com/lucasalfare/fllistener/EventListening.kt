@file:Suppress("MemberVisibilityCanBePrivate")

package com.lucasalfare.fllistener


/**
 * This type represents a padronized type of event description
 * to transit over the application flow.
 */
data class AppEvent(val identifier: Any)


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
   * Holds all current objects that are listening to this instance.
   */
  var listeners = mutableListOf<EventManageable>()

  /**
   * Function that offers a custom initialization block.
   *
   * Normally this is the first function to be called, after
   * constructor and/or native init blocks.
   */
  abstract fun init()

  /**
   * Used to handle any kind of incoming event/data from outside
   * the instance.
   */
  abstract fun onEvent(event: AppEvent, data: Any?, origin: Any?)

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
  fun notifyListeners(event: AppEvent, data: Any? = null, origin: Any? = null) {
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

  private val callbacks = mutableListOf<(AppEvent, Any?) -> Unit>()

  override fun init() {}

  override fun onEvent(event: AppEvent, data: Any?, origin: Any?) {
    callbacks.forEach { callback ->
      callback(event, data)
    }
  }

  fun addCallback(callback: (AppEvent, Any?) -> Unit): (AppEvent, Any?) -> Unit {
    if (!callbacks.contains(callback)) callbacks.add(callback)
    return callback
  }

  fun removeCallback(callback: (AppEvent, Any?) -> Unit) {
    callbacks.remove(callback)
  }
}

/**
 * Custom helper method to define relationship between different
 * event manageables instances. This method also initializes all the
 * managers synchronously.
 *
 * TODO: make initialization calls run asynchronously.
 */
fun setupManagers(vararg managers: EventManageable) {
  managers.forEach { m1 ->
    managers.forEach { m2 ->
      if (m2 != m1) {
        m1.addListener(m2)
      }
    }
  }

  managers.forEach { it.init() }
}