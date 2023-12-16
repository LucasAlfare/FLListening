package com.lucasalfare.fllistening

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

/**
 * Suspended function to set up event managers by establishing listener relationships and initializing them concurrently.
 *
 * @param managers Variable number of [EventManageable] instances to set up.
 */
suspend fun setupManagers(vararg managers: EventManageable) {
  // Establish listener relationships among the provided managers
  managers.forEach { m1 ->
    managers.forEach { m2 ->
      // Avoid adding a manager as a listener to itself
      if (m2 != m1) {
        m1.addListener(m2)
      }
    }
  }

  // Initialize managers concurrently using coroutineScope
  coroutineScope {
    managers.forEach { launch { it.initialize() } }
  }
}