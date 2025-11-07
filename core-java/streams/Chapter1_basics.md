# Java Streams API — Chapter 1: The Basics

## 💡 What is a Stream?

A **Stream** is a *sequence of elements* supporting *functional-style operations* to process data declaratively.

Instead of writing loops like:

```java
for (String name : names) {
    if (name.startsWith("A")) {
        result.add(name.toUpperCase());
    }
}
```

You can use a Stream pipeline:
```java
List<String> result = names.stream()
                           .filter(name -> name.startsWith("A"))
                           .map(String::toUpperCase)
                           .toList();
```

Streams don’t store data — they *process* it from a **source**.

---

## ⚙️ Stream Pipeline Structure

A Stream pipeline has **3 stages**:

| Stage | Description | Example |
|--------|--------------|----------|
| **Source** | Data origin (`List`, `Set`, `Array`) | `list.stream()` |
| **Intermediate Operations** | Transformations (lazy) | `filter()`, `map()`, `sorted()` |
| **Terminal Operation** | Triggers execution | `collect()`, `forEach()`, `count()` |

Example:
```java
List<String> result = names.stream()        // Source
                           .filter(n -> n.length() > 3)
                           .map(String::toUpperCase)
                           .sorted()
                           .toList();       // Terminal
```

---

## 🧩 Creating Streams

```java
// From a Collection
Stream<String> s1 = List.of("A", "B", "C").stream();

// From an Array
Stream<Integer> s2 = Arrays.stream(new Integer[]{1, 2, 3});

// From individual values
Stream<String> s3 = Stream.of("X", "Y", "Z");

// Infinite stream (careful!)
Stream<Integer> infinite = Stream.iterate(1, n -> n + 1);
```

---

## 🧪 Example: Squaring Even Numbers

```java
List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6);

List<Integer> squares = numbers.stream()
                               .filter(n -> n % 2 == 0)
                               .map(n -> n * n)
                               .toList();

System.out.println(squares); // [4, 16, 36]
```

---

## ⚠️ Important Notes

- Streams are **consumed once** — they can’t be reused.
- Intermediate operations are **lazy**.
- The **order of operations** matters.
- Streams are *not collections* — they don’t store or modify data.

---

## 🧾 Summary Table

| Operation | Purpose | Example |
|------------|----------|----------|
| `.stream()` | Creates a stream from a collection | `list.stream()` |
| `filter()` | Keeps elements matching condition | `filter(x -> x > 0)` |
| `map()` | Transforms each element | `map(x -> x * 2)` |
| `collect()` | Gathers results | `collect(Collectors.toList())` |
| `forEach()` | Iterates over results | `forEach(System.out::println)` |

---

## 🧩 Mini Quiz

**Q1.**  
What will this print?
```java
List<String> names = List.of("John", "Amy", "Bob");
names.stream()
     .filter(n -> n.length() > 3)
     .map(String::toUpperCase)
     .forEach(System.out::println);
```
🟢 **Answer:** `JOHN`

---

**Q2.**  
What happens if you reuse a stream after a terminal operation?  
🟢 **Answer:** Throws `IllegalStateException`.

---

**Q3.**  
Identify the source, intermediate, and terminal ops:
```java
List<Integer> r = List.of(10, 20, 30, 40)
    .stream()
    .filter(x -> x > 20)
    .map(x -> x / 10)
    .toList();
```
🟢 **Source:** `List.of(...)`  
🟢 **Intermediate:** `filter`, `map`  
🟢 **Terminal:** `toList`

---

✅ **End of Chapter 1**
Next: **Intermediate Operations** — mastering `map`, `filter`, `distinct`, `sorted`, and more.
