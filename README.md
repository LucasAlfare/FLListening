# FLListening

FLListening is a Kotlin project that offers a versatile base class for managing events and listeners, facilitating seamless event handling and communication across various components of your application.

# Download
You can use this library with [Jitpack](https://jitpack.io/). Add the following to your `build.gradle`:
```groovy
repositories {
  // ...
  maven { url 'https://jitpack.io' }
}

dependencies {
  implementation 'com.github.LucasAlfare:FLListening:v2.0'
}
```

Or if you are using `kotlin-DSL` in an `build.gradle.kts` file:
```kotlin
repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.LucasAlfare:FLListening:v2.0")
}
```

You can also find how to get the library if you are using another dependency manager system, such as `Maven` in this [Jitpack page](https://jitpack.io/#LucasAlfare/FLListening/v2.0).

## EventManageable

`EventManageable` is an abstract class designed to be extended for managing events and listeners. It provides a foundation for implementing event-driven architectures. Key features include:

- **Initialization:** Use the `initialize` method to set up the initial state of your event manager.
- **Event Notification:** Notify all registered listeners about the occurrence of an event using the `notifyListeners` method.
- **Event Handling:** Implement the `onEvent` method in your subclass to define custom logic for handling events.
- **Listener Management:** Add and remove listeners dynamically to control the flow of information.

## Usage

### Setting up Event Managers

To simplify the setup process for multiple `EventManageable` instances, you can use the `setupManagers` function. This function establishes listener relationships among the provided managers and initializes them concurrently using coroutines.

Example:

```kotlin
val manager1 = SomeEventManageableSubclass() // create your EventManageable instance
val manager2 = AnotherEventManageableSubclass()// create another EventManageable instance

// Set up managers with listener relationships
setupManagers(manager1, manager2)
```

### Creating Custom Event Managers

Create your custom event managers by extending the `EventManageable` class and implementing the required methods:

```kotlin
class CustomManager : EventManageable() {
    override suspend fun initialize() {
        // Initialization logic here
    }

    override fun onEvent(event: Any, data: Any?) {
        // Custom event handling logic here
    }
}
```

## Contribution

Feel free to contribute to the project by opening issues or submitting pull requests.

## License

This project is licensed under the [MIT License](LICENSE).