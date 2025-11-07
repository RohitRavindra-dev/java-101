# Java Streams API — Chapter 7: Stream Best Practices

## 🎯 Goal
Master **when and how to use Streams effectively** — avoid pitfalls, write clean and readable pipelines, and understand where Streams truly shine.

---

## ⚙️ 1️⃣ Common Pitfalls & Anti-Patterns

### ❌ Pitfall #1 — Reusing a Consumed Stream

```java
Stream<String> s = Stream.of("A", "B", "C");
s.forEach(System.out::println);
s.count(); // ❌ IllegalStateException: stream has already been operated upon or closed
```

✅ **Fix:**
```java
Stream.of("A", "B", "C").count(); // Create a new stream each time
```

---

### ❌ Pitfall #2 — Modifying External State

```java
List<String> names = List.of("Alice", "Bob");
List<String> upper = new ArrayList<>();

names.stream()
     .map(String::toUpperCase)
     .forEach(upper::add); // ❌ Bad: mutates external list
```

✅ **Better:**
```java
List<String> upper = names.stream()
                          .map(String::toUpperCase)
                          .toList();
```

---

### ❌ Pitfall #3 — Null Values in Streams

```java
List<String> names = Arrays.asList("Alice", null, "Bob");
names.stream()
     .map(String::toUpperCase) // ❌ NPE
     .toList();
```

✅ **Fix:**
```java
List<String> safe = names.stream()
                         .filter(Objects::nonNull)
                         .map(String::toUpperCase)
                         .toList();
```

---

### ❌ Pitfall #4 — Complex Logic Inside Lambdas

```java
users.stream()
     .filter(u -> {
         if (u.isActive()) {
             sendEmail(u);
             return u.getAge() > 18 && u.getRole().equals("ADMIN");
         }
         return false;
     })
     .forEach(System.out::println); // ❌ unreadable and side-effect heavy
```

✅ **Better:**
```java
users.stream()
     .filter(User::isActive)
     .filter(u -> u.getAge() > 18 && u.getRole().equals("ADMIN"))
     .forEach(System.out::println);
```

💡 Tip: Each filter should represent *one simple rule.*

---

### ❌ Pitfall #5 — Using `forEach` to Mutate Data

```java
List<Integer> nums = new ArrayList<>(List.of(1, 2, 3));
nums.stream().forEach(n -> nums.add(n + 1)); // ❌ ConcurrentModificationException
```

✅ **Better:**
```java
List<Integer> doubled = nums.stream()
                            .map(n -> n * 2)
                            .toList();
```

---

### ❌ Pitfall #6 — Overusing Streams

Sometimes **for-loops** are better for clarity, especially when logic is sequential or exception-heavy.

---

## ⚙️ 2️⃣ When *Not* to Use Streams

| 🚫 Situation | Why | Better Alternative |
|---------------|------|-------------------|
| Simple for-loops | Streams add noise | Regular loop |
| Deep branching logic | Hard to read/debug | Break into methods |
| Mutable aggregation | Streams are immutable | Classic loops |
| Checked exceptions | Streams complicate error handling | Try-catch in loops |

---

### Example — Loops Are Sometimes Better

```java
for (User u : users) {
    try {
        process(u);
    } catch (IOException e) {
        logger.error("Error processing user " + u.getId(), e);
    }
}
```

✅ Easier to debug and log than streams with try-catch wrappers.

---

## 🧩 Mini Quiz (with Examples & Answers)

**Q1.** What happens if you reuse a stream after a terminal operation?  
✅ The stream is closed; attempting reuse throws `IllegalStateException`.

Example:
```java
Stream<String> s = Stream.of("A", "B");
s.forEach(System.out::println);
s.count(); // ❌ Runtime error
```

---

**Q2.** Why avoid modifying external variables inside streams?  
✅ It breaks functional purity and may cause race conditions in parallel streams.

Example:
```java
List<Integer> list = new ArrayList<>();
IntStream.range(1, 5).forEach(list::add); // ❌ Not thread-safe
```

---

**Q3.** Which is better for checked exceptions — streams or loops?  
✅ Loops, because they’re easier to read and handle exceptions gracefully.

Example:
```java
for (User u : users) {
    try {
        process(u);
    } catch (IOException e) {
        log.error("Error", e);
    }
}
```

---

## 🧠 Bonus: `.map()` in Stream vs Optional

Both `Stream<T>` and `Optional<T>` support `.map(Function<T, R>)` because both are **containers** — one holds many elements, one holds zero or one.

| Concept | Optional | Stream |
|----------|-----------|--------|
| Container for | 0 or 1 value | 0 or many values |
| `map()` result | `Optional<R>` | `Stream<R>` |
| Purpose | Apply transformation if present | Apply transformation to all elements |

Example:
```java
Optional<String> nameOpt = Optional.of("Alice");
nameOpt.map(String::toUpperCase); // Optional<"ALICE">

Stream<String> names = Stream.of("Alice", "Bob");
names.map(String::toUpperCase); // Stream<"ALICE", "BOB">
```

Both share the same *functional pattern*:  
> “Apply this function if a value exists.”

---

✅ **End of Chapter 7**
Next: **Chapter 8 — Streams in Spring Boot (Real-World Use)**.
