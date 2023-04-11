package com.lucasalfare.fllistener


enum class MyEvent(val appEvent: AppEvent) {
  TestEvent(eventFactory("TestEvent"))
}


class Manager1 : EventManageable() {

  private var neededValueToInitializeThisManager = ""

  override fun onNotInitiated() {
    if (neededValueToInitializeThisManager != "") {
      this.initiated = true
    }
  }

  override fun onInitiated() {
    println("Manager 1 received its needed value This is the value: [$neededValueToInitializeThisManager]. Finishing...")
  }

  override fun onEvent(event: AppEvent, data: Any?, origin: Any?) {
    if (event == MyEvent.TestEvent.appEvent) {
      neededValueToInitializeThisManager = data as String
    }
  }
}

class Manager2 : EventManageable() {

  private var elapsed = 0L
  private val start = System.currentTimeMillis()

  override fun onNotInitiated() {
    if (elapsed < 5000) {
      elapsed = System.currentTimeMillis() - start
    } else {
      this.initiated = true
    }
  }

  override fun onInitiated() {
    this.notifyListeners(MyEvent.TestEvent.appEvent, "Kkkkkkkk !!!")
  }

  override fun onEvent(event: AppEvent, data: Any?, origin: Any?) {

  }
}

class Manager3 : EventManageable() {

  private var elapsed = 0L
  private val start = System.currentTimeMillis()

  override fun onNotInitiated() {
    if (elapsed < 200) {
      elapsed = System.currentTimeMillis() - start
    } else {
      this.initiated = true
    }
  }

  override fun onInitiated() {
    println("${this.javaClass.name} finished after $elapsed milliseconds.")
  }

  override fun onEvent(event: AppEvent, data: Any?, origin: Any?) {

  }
}

suspend fun main() {
  setupManagers(
    Manager1(),
    Manager2(),
    Manager3()
  )
}