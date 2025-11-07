# Java Streams API — Chapter 6: Advanced & Parallel Streams

## 🎯 Goal
Learn how to use **advanced stream patterns**, including parallel processing, stream flattening (`flatMap`), infinite streams, and performance optimization.

---

## ⚙️ 1️⃣ Parallel vs Sequential Streams

By default, streams are **sequential** — elements are processed one by one on a single thread.

```java
list.stream()
    .filter(...)
    .map(...)
    .forEach(...);
```

### 💡 Making it Parallel
You can make a stream parallel using `.parallel()` or `.parallelStream()`.

```java
list.parallelStream()
    .filter(...)
    .map(...)
    .forEach(...);
```

or

```java
list.stream()
    .parallel()
    .forEach(...);
```

Each element is processed on multiple threads from the ForkJoinPool (usually one per CPU core).

---

## ⚙️ Example — Sequential vs Parallel

```java
List<Integer> nums = IntStream.rangeClosed(1, 10).boxed().toList();

nums.stream()
    .forEach(n -> System.out.println(Thread.currentThread().getName() + " -> " + n));

nums.parallelStream()
    .forEach(n -> System.out.println(Thread.currentThread().getName() + " -> " + n));
```

In parallel mode, multiple threads process different elements simultaneously.

---

## ⚠️ When NOT to Use Parallel Streams

| ❌ Avoid When | Why |
|---------------|------|
| Order matters | Parallel may reorder elements |
| Shared mutable state | Risk of race conditions |
| Small datasets | Thread overhead > processing gain |
| I/O-bound tasks | Threads wait on I/O — no benefit |
| Streams in web/db calls | Thread contention and unpredictable latency |

✅ Use for **large**, **CPU-heavy**, **stateless** operations.

---

### 💡 Example — CPU-Intensive Case

```java
long count = LongStream.range(1, 100_000_000)
    .parallel()
    .filter(n -> n % 2 == 0)
    .count();
```

Parallel execution can drastically reduce runtime here.

---

## ⚙️ 2️⃣ Combining Streams

Use `Stream.concat()` to merge multiple streams.

```java
Stream<String> s1 = Stream.of("A", "B");
Stream<String> s2 = Stream.of("C", "D");

Stream<String> combined = Stream.concat(s1, s2);
combined.forEach(System.out::print); // ABCD
```

⚠️ Once combined, the input streams are **consumed** and cannot be reused.

---

## ⚙️ 3️⃣ Flattening Nested Streams → `flatMap()`

### 💡 What Does `flatMap()` Expect?

`flatMap()` expects a **function that takes one element** and returns a **Stream** of new elements.  
It then flattens all those small streams into one big stream.

---

### 🧩 Example 1 — Using Lambda

```java
record Department(String name, List<String> employees) {}

List<Department> depts = List.of(
    new Department("IT", List.of("Alice", "Bob")),
    new Department("HR", List.of("Charlie"))
);

List<String> allEmployees = depts.stream()
    .flatMap(d -> d.employees().stream())
    .toList();

System.out.println(allEmployees); // [Alice, Bob, Charlie]
```

🧠 Breakdown:
1. Each `Department` passes through the lambda.
2. Lambda returns a `Stream<String>` (the employee list).
3. `flatMap()` merges (flattens) all `Stream<String>` into one.

So:
```
Stream<Department> → flatMap(d -> d.employees().stream()) → Stream<String>
```

---

### 🧩 Example 2 — Using Method Reference

```java
List<List<String>> data = List.of(
    List.of("A", "B"),
    List.of("C", "D")
);

List<String> flat = data.stream()
                        .flatMap(List::stream)
                        .toList();

System.out.println(flat); // [A, B, C, D]
```

Here, `List::stream` is equivalent to `list -> list.stream()`.

So:
```
Stream<List<String>> → flatMap(List::stream) → Stream<String>
```

---

### 💬 Analogy: `map()` vs `flatMap()`

| Operation | Input | Output |
|------------|--------|--------|
| `map()` | One element → One transformed element | Keeps nesting |
| `flatMap()` | One element → Stream of elements | Flattens nesting |

```java
.map(list -> list.stream())      // Stream<Stream<String>>
.flatMap(list -> list.stream())  // Stream<String>
```

That’s why it’s called **flat** + **map**.

---

## ⚙️ 4️⃣ Infinite Streams

Streams can generate infinite sequences. Use `limit()` to keep them finite.

```java
Stream<Integer> infinite = Stream.iterate(1, n -> n + 1);
List<Integer> firstTen = infinite.limit(10).toList();
System.out.println(firstTen); // [1,2,3,4,5,6,7,8,9,10]
```

### Another Variant: `generate()`
```java
Stream<Double> randoms = Stream.generate(Math::random).limit(5);
randoms.forEach(System.out::println);
```

---

## ⚙️ 5️⃣ Short-Circuiting Operations

These terminal operations stop processing once a condition is met.

| Operation | Description |
|------------|--------------|
| `findFirst()` | Returns first match and stops |
| `findAny()` | Returns any match (useful in parallel) |
| `anyMatch()` | Stops when condition is true |
| `allMatch()` / `noneMatch()` | Stop when falsified |

Example:
```java
boolean found = Stream.of("alpha", "beta", "gamma")
                      .peek(System.out::println)
                      .anyMatch(s -> s.startsWith("b"));
System.out.println(found); // true, stops after "beta"
```

---

## ⚙️ 6️⃣ Performance Tips & Best Practices

✅ **Use parallel streams when:**
- Data size is large (10k+)
- Operations are CPU-bound
- No shared mutable state
- Order doesn’t matter

✅ **Avoid parallel when:**
- You’re using synchronized or mutable structures
- Stream elements depend on each other
- Tasks involve I/O or DB calls

✅ **Combine smartly:**
Prefer `flatMap()` and `limit()` instead of building large intermediate lists.

---

## 🧾 Chapter 5 Summary

| Concept | Purpose | Example |
|----------|----------|----------|
| `parallelStream()` | Run stream concurrently | `list.parallelStream()` |
| `flatMap()` | Flatten nested data | `lists.stream().flatMap(List::stream)` |
| `concat()` | Merge streams | `Stream.concat(a, b)` |
| `iterate()` / `generate()` | Create infinite streams | `Stream.iterate(1, n->n+1)` |
| Short-circuit ops | Stop early | `anyMatch()`, `findFirst()` |

---

## 🧩 Mini Quiz (with Answers & Explanations)

**Q1. What’s the key difference between `map()` and `flatMap()`?**  
✅ `map()` transforms each element → one new element.  
✅ `flatMap()` transforms each element → a **Stream** of elements, then flattens those streams into one continuous stream.

**Example:**  
```java
List<List<String>> data = List.of(List.of("A", "B"), List.of("C"));
data.stream().map(List::stream);     // Stream<Stream<String>>
data.stream().flatMap(List::stream); // Stream<String>
```

---

**Q2. When does parallel stream reduce performance?**  
✅ When the dataset is small, or operations are simple/lightweight.  
✅ Because the overhead of splitting work and managing threads can exceed the time saved.  
Also harmful when there’s shared mutable state or order dependency.

💡 **Rule of thumb:** Use for large (10k+) CPU-bound datasets.

---

**Q3. What will this print?**
```java
Stream.generate(() -> "Hi")
      .limit(3)
      .forEach(System.out::print);
```
✅ Output: `HiHiHi` (same line)  
If using `println`, it would print each on a new line.

---

✅ **End of Chapter 6**
Next: **Chapter 7 — Stream Best Practices** (common pitfalls, debugging, and using streams effectively in Spring Boot).
