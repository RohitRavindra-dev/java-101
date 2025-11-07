
# ✅ Java `CompletableFuture` Cheat Sheet

## ✅ `CompletableFuture` vs `Future`

| Feature              | `Future`                 | `CompletableFuture`                           |
|----------------------|--------------------------|-----------------------------------------------|
| Introduced In        | Java 5                   | Java 8                                        |
| Blocking `.get()`    | Yes                      | Yes (but also non-blocking alternatives)      |
| Async Composition    | No                       | Yes (`then*` methods for chaining)            |
| Exception Handling   | No                       | Yes (`handle`, `exceptionally`)               |
| Manual Completion    | No                       | Yes (`complete`, `completeExceptionally`)     |
| Multiple Completion  | No                       | Yes (via callbacks, manual, or async stages)  |

## ✅ Creating `CompletableFuture`

```java
// Async execution
CompletableFuture.supplyAsync(() -> "result");

// Manual completion
CompletableFuture<String> cf = new CompletableFuture<>();
cf.complete("manual result");

// Already completed
CompletableFuture.completedFuture("immediate value");
```

## ✅ Core Methods & When to Use

### `thenApply(fn)`
- Transforms the result.
- Use for mapping: `T -> U`.

```java
cf.thenApply(result -> result.toUpperCase());
```

### `thenAccept(consumer)`
- Consumes the result.
- Use for side-effects, no return value needed.

```java
cf.thenAccept(System.out::println);
```

### `thenRun(runnable)`
- Ignores result, just runs.
- Use when you want to trigger something after completion, but don’t care about the result.

```java
cf.thenRun(() -> System.out.println("Done"));
```

### `thenCompose(fn)`
- Use when your function returns another `CompletableFuture<T>`.
- Flattens: `CompletableFuture<CompletableFuture<T>>` → `CompletableFuture<T>`.
- Use for chaining async calls.

```java
cf.thenCompose(id -> getUserDetails(id));
```

### `thenCombine(otherFuture, combiner)`
- Runs both futures in parallel, combines their results.

```java
cf1.thenCombine(cf2, (a, b) -> a + b);
```

### `allOf(cf1, cf2, ...)`
- Waits for all futures to complete.

```java
CompletableFuture.allOf(cf1, cf2).thenRun(() -> {
    var result1 = cf1.join();
    var result2 = cf2.join();
});
```

### `anyOf(cf1, cf2, ...)`
- Returns the result of first completed future.

```java
CompletableFuture.anyOf(cf1, cf2).thenAccept(System.out::println);
```

### `exceptionally(fn)`
- Handles exception and returns fallback.

```java
cf.exceptionally(ex -> "fallback");
```

### `handle((result, exception) -> ...)`
- Runs always, gives both result and exception.

```java
cf.handle((res, ex) -> ex != null ? "Recovered" : res);
```

### `whenComplete((result, exception) -> void)`
- Observes result or error.

```java
cf.whenComplete((res, ex) -> log.info("Completed"));
```

### `orTimeout(timeout, unit)`
- Auto-fails if task exceeds timeout.

```java
cf.orTimeout(2, TimeUnit.SECONDS);
```

### `completeOnTimeout(value, timeout, unit)`
- Completes with a fallback value on timeout.

```java
cf.completeOnTimeout("default", 2, TimeUnit.SECONDS);
```

### `join()`
- Like `get()`, but throws `CompletionException` (unchecked).

```java
String value = cf.join();
```

## ✅ Threading: Async Variants

| Method                   | Executes On                     | Use When                             |
|--------------------------|----------------------------------|--------------------------------------|
| `thenApply`              | Same thread as previous stage   | Lightweight, non-blocking work       |
| `thenApplyAsync`         | ForkJoinPool (default)          | Offload to another thread            |
| `thenApplyAsync(..., e)` | Custom executor                 | For IO-bound work, separation of concerns |

## ✅ Manual Completion Use Cases

```java
CompletableFuture<String> cf = new CompletableFuture<>();
callback.onResponse(data -> cf.complete(data));
```

**Use when:**
- Wrapping callback-based APIs
- Integrating with WebSocket/event-based systems
- Writing test doubles or mocks
- Implementing fail-fast logic with `completeExceptionally`

## ✅ Clean Async Pipeline Example

```java
public CompletableFuture<String> placeOrder(String itemId) {
    return itemService.get(itemId)
        .thenCompose(item -> {
            if (!item.isInStock()) {
                return CompletableFuture.failedFuture(new IllegalStateException("Out of stock"));
            }
            return orderService.place(item);
        })
        .thenApply(Order::confirmation);
}
```
