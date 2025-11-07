# Java Streams API — Chapter 3: Terminal Operations

## 🎯 Goal
Understand and master **terminal operations** — the final stage of a Stream pipeline that produces output or side effects.

---

## ⚙️ What Are Terminal Operations?

A **terminal operation**:
- Consumes the stream (cannot be reused).
- Triggers all intermediate steps.
- Returns a result (collection, value, or void).

---

## 🔑 Common Terminal Operations

| Operation | Returns | Common Use |
|------------|----------|------------|
| `forEach()` | void | Perform an action per element |
| `collect()` | Collection or custom result | Gather results |
| `count()` | long | Count elements |
| `min()` / `max()` | Optional<T> | Find smallest/largest |
| `findFirst()` / `findAny()` | Optional<T> | Retrieve an element |
| `reduce()` | T or Optional<T> | Combine into one value |
| `anyMatch()` / `allMatch()` / `noneMatch()` | boolean | Test conditions |

---

## 💡 1. `forEach()`

```java
List<String> names = List.of("Alice", "Bob", "Charlie");
names.stream()
     .map(String::toUpperCase)
     .forEach(System.out::println);
```

📌 Used for side effects (printing, logging).

---

## 💡 2. `collect()`

Gather elements into a collection or custom container.

```java
List<String> upper = List.of("a", "b", "c").stream()
    .map(String::toUpperCase)
    .collect(Collectors.toList());
```

```java
String joined = Stream.of("Java", "Streams", "API")
    .collect(Collectors.joining(" "));
System.out.println(joined); // "Java Streams API"
```

---

## 💡 3. `count()`, `min()`, `max()`

```java
long count = Stream.of(1, 2, 3, 4, 5)
    .filter(n -> n % 2 == 0)
    .count(); // 2
```

```java
List<Integer> nums = List.of(5, 1, 9, 2);
int min = nums.stream().min(Integer::compareTo).get();
int max = nums.stream().max(Integer::compareTo).get();
System.out.println(min + " " + max); // 1 9
```

---

## 💡 4. `findFirst()` / `findAny()`

```java
List<String> names = List.of("Alice", "Bob", "Charlie");
Optional<String> first = names.stream().findFirst();
Optional<String> any = names.parallelStream().findAny();
```

`findFirst()` → deterministic (first element).  
`findAny()` → nondeterministic (for parallel streams).

---

## 💡 5. `reduce()`

Combines elements into a single result.

```java
int sum = Stream.of(1, 2, 3, 4)
    .reduce(0, (a, b) -> a + b); // 10
```

```java
String sentence = Stream.of("Java", "is", "awesome")
    .reduce("", (a, b) -> a + " " + b)
    .trim();
System.out.println(sentence); // "Java is awesome"
```

---

## 💡 6. Matching Operations

```java
List<Integer> numbers = List.of(2, 4, 6, 8);
boolean allEven = numbers.stream().allMatch(n -> n % 2 == 0);
boolean anyEven = numbers.stream().anyMatch(n -> n % 2 == 0);
boolean noneNegative = numbers.stream().noneMatch(n -> n < 0);
```

---

## ⚙️ Execution Order Example

```java
List<Integer> result = Stream.of(1, 2, 3, 4, 5, 6)
    .filter(n -> {
        System.out.println("Filtering " + n);
        return n % 2 == 0;
    })
    .map(n -> {
        System.out.println("Mapping " + n);
        return n * 10;
    })
    .toList();
```

Output:
```
Filtering 1
Filtering 2
Mapping 2
Filtering 3
Filtering 4
Mapping 4
Filtering 5
Filtering 6
Mapping 6
[20, 40, 60]
```

---

## 🧩 Mini Quiz (with Answers)

**Q1.** Difference between `collect()` and `reduce()`?  
✅ `collect()` → gathers elements into a container (List, Map, Set).  
✅ `reduce()` → folds all elements into a single scalar value.

**Q2.** What happens if two terminal ops are called on one stream?  
✅ `IllegalStateException` — stream already consumed.

**Q3.** Output?
```java
List<String> names = List.of("a", "b", "c", "d");
names.stream().limit(2).peek(System.out::print).count();
```
✅ Prints `ab` (since only first 2 elements pass through).

---

✅ **End of Chapter 3**
Next: **Collectors Deep Dive** — advanced `groupingBy`, `partitioningBy`, and real-world backend use cases.
