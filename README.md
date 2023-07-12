# FLListener

This is my custom implementation for observer/listener/event driven project patterns using pure Kotlin and its Coroutines.

This aims to help me decouple code making then more separeted from thenselves by making different parts of a project interacting only by events notifications.

# The implementation

In this project we have a central piece of code called `EventManageable`. This is an abstract type and make all its actual implementations reffers to code capable of either receive events notifications from others either send event notifications to others.

In this way, should be estabilished a minimal connection between those event manageables and this is made by adding the references of each other in a local list. This doesn't create more data, just creates pointers in respective lists to target the other living manageables of a project.

Before we start explaning an example, is also important to know that this project includes a machanism to automatically initialzes the manager pieces asynchronously. This means, for example, if a `manager1` initizlization depends on something that is processed by another `manager2` and, by its time, this other manager perform some havy work in their initializations, the assynchronous initialization should make the `manager1` "waits" for `manager2` to finish, giving time to `manager2` fire some notification that `manager1` is waiting for.

Having the general explanation done, let's consider more detailed with this example:

- A project contains two managers: `Manager1` and `Manager2`;
- `Manager1` needs to initialize its stuff but, for this, for some reason, it needs of something that is processed inside `Manager2` initialization;
- `Manager2` is a really slow code, that perform really heavy undefined loops, but finishable;
- When `Manager2` finnally finishes it performs
  ```kotlin
    this.notifyListeners(
      event = MyAppEvents.TestEvent.appEvent,
      data  = "this is the resulting value processed here on Manager2."
    )
    ```
- As `Manager1` also still alive and running, when it receives that event notification sended from `Manager2` it can, finnally finish its own initialization using the received value attached to the event.

As you can see, this is a rough example, but it works to show how the project handles the asynchronous blocks.

By the end, is important to know that the projects consider that events are an instance of the data class `AppEvent`. This was made to abstract the event types in order to use another structures, such as `enums`. Without this, normally should be considered that events could be `String`, but was thought that this must contains a lot of duplicated fields. For example:

```kotlin
const val MY_EVENT_IDENTIFIER_1 = "MY_EVENT_IDENTIFIER_1"
```

Clearly there are another good ways to deal with it, but, for now, we are suggesting to use something like this:

```kotlin
enum class MyEvent(val appEvent: AppEvent) {
  ThisIsOneOfMyEvents(eventFactory("ThisIsOneOfMyEvents"))
}
```

As you can see, this approach still dupicating the labls, but we should follow this direction on next updates.
