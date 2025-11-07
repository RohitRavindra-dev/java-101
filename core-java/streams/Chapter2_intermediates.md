# Java Streams API — Chapter 2: Intermediate Operations

## 🎯 Goal
To master the **intermediate (transformation) operations** that modify or filter data inside a Stream pipeline before the terminal step.

---

## 🧩 What Are Intermediate Operations?

They **transform** or **filter** data, returning a *new Stream* each time — not actual data.

> 🟡 Intermediate operations are **lazy** — they don’t execute until a terminal operation (like `collect()` or `forEach()`) is called.

---

## ⚙️ Common Intermediate Operations

| Operation | Purpose | Example |
|------------|----------|----------|
| `filter()` | Keep elements that satisfy a condition | `filter(x -> x > 10)` |
| `map()` | Transform each element | `map(String::length)` |
| `distinct()` | Remove duplicates | `distinct()` |
| `sorted()` | Sort elements | `sorted()` or `sorted(Comparator.reverseOrder())` |
| `limit()` | Take first N elements | `limit(5)` |
| `skip()` | Skip first N elements | `skip(3)` |
| `peek()` | Debug or inspect elements mid-pipeline | `peek(System.out::println)` |

---

## 💡 1. `filter()`

Filters elements based on a condition.

```java
List<String> names = List.of("Alice", "Bob", "Annie", "Alex", "Charlie");

List<String> aNames = names.stream()
                           .filter(n -> n.startsWith("A"))
                           .toList();

System.out.println(aNames); // [Alice, Annie, Alex]
```

---

## 💡 2. `map()`

Transforms each element into another form.

```java
List<String> names = List.of("Alice", "Bob", "Charlie");

List<Integer> lengths = names.stream()
                             .map(String::length)
                             .toList();

System.out.println(lengths); // [5, 3, 7]
```

Change element type example:
```java
List<Integer> numbers = List.of(1, 2, 3);
List<String> strNums = numbers.stream()
                              .map(String::valueOf)
                              .toList();
```

---

## 💡 3. `distinct()`

Removes duplicates (based on `.equals()` comparison).

```java
List<Integer> nums = List.of(1, 2, 2, 3, 3, 3);
System.out.println(nums.stream().distinct().toList()); // [1, 2, 3]
```

---

## 💡 4. `sorted()`

Sorts elements either by natural order or using a custom comparator.

```java
List<String> names = List.of("Charlie", "Alice", "Bob");
System.out.println(names.stream().sorted().toList()); // [Alice, Bob, Charlie]

// Reverse order
System.out.println(names.stream()
                        .sorted(Comparator.reverseOrder())
                        .toList()); // [Charlie, Bob, Alice]
```

---

## 💡 5. `limit()` and `skip()`

```java
List<Integer> numbers = List.of(10, 20, 30, 40, 50, 60);

System.out.println(numbers.stream().limit(3).toList()); // [10, 20, 30]
System.out.println(numbers.stream().skip(3).toList());  // [40, 50, 60]
System.out.println(numbers.stream().skip(2).limit(3).toList()); // [30, 40, 50]
```

---

## 💡 6. `peek()` — Debugging Streams

Use `peek()` to view or log data flowing through a pipeline.

```java
List<Integer> nums = List.of(1, 2, 3, 4);

List<Integer> doubled = nums.stream()
                            .peek(n -> System.out.println("Before map: " + n))
                            .map(n -> n * 2)
                            .peek(n -> System.out.println("After map: " + n))
                            .toList();

System.out.println(doubled);
```

> ⚠️ `peek()` is for **side-effects only** (e.g., logging). It should **not** change data.

---

## ⚙️ Lazy Evaluation Demonstration

Intermediate ops do nothing until a terminal operation is invoked.

```java
Stream<Integer> s = Stream.of(1, 2, 3, 4, 5)
    .filter(n -> {
        System.out.println("Filtering " + n);
        return n % 2 == 0;
    });

System.out.println("Stream created, but not executed yet.");
s.toList(); // Execution happens here!
```

Output:
```
Stream created, but not executed yet.
Filtering 1
Filtering 2
Filtering 3
Filtering 4
Filtering 5
```

---

## ⚡ Chaining Operations — Real Example

```java
List<String> employees = List.of("Alice", "Bob", "Alex", "Charlie", "Andrew");

List<String> result = employees.stream()
                               .filter(e -> e.startsWith("A"))
                               .map(String::toUpperCase)
                               .sorted()
                               .toList();

System.out.println(result); // [ALEX, ALICE, ANDREW]
```

---

## 🧾 Chapter 2 Summary

| Concept | Description | Example |
|----------|--------------|----------|
| `filter()` | Keep only matching elements | `filter(x -> x > 10)` |
| `map()` | Transform elements | `map(String::toUpperCase)` |
| `distinct()` | Remove duplicates | `distinct()` |
| `sorted()` | Sort elements | `sorted(Comparator.reverseOrder())` |
| `limit()` | Restrict number of elements | `limit(5)` |
| `skip()` | Skip first N elements | `skip(3)` |
| `peek()` | Inspect pipeline for debugging | `peek(System.out::println)` |
| **Lazy evaluation** | Runs only when terminal op is called | — |

---

## 🧩 Mini Quiz (with Answers)

**Q1.**
```java
List<String> names = List.of("Amy", "Bob", "Alex", "Charlie");
names.stream()
     .filter(n -> n.length() > 3)
     .map(String::toUpperCase)
     .sorted()
     .forEach(System.out::println);
```
✅ **Answer:** `ALEX`, `CHARLIE`

---

**Q2.** What’s the difference between `map()` and `peek()`?  
✅ `map()` transforms and returns a new stream with modified data.  
✅ `peek()` inspects elements (side-effect) but doesn’t modify or consume them.

---

**Q3.** What happens if no terminal operation is called?  
✅ Nothing executes — the stream stays in a **lazy** state until triggered.

---

✅ **End of Chapter 2**
Next: **Terminal Operations** — `collect`, `reduce`, `count`, `min`, `max`, and how to summarize data efficiently.
