# Java Streams API — Chapter 5: Primitive Streams & Optional

## 🎯 Goal
Learn to use **primitive streams** (`IntStream`, `LongStream`, `DoubleStream`) efficiently and safely handle missing values using `Optional`.

---

## 💡 Why Primitive Streams Exist

Normal `Stream<T>` works with objects — e.g., `Stream<Integer>`.  
That causes **boxing/unboxing overhead**, which is inefficient for numeric data.

Primitive streams solve that:
- `IntStream` → for `int`
- `LongStream` → for `long`
- `DoubleStream` → for `double`

They store raw primitives and add numeric-specific methods like `sum()`, `average()`, and `range()`.

---

## ⚙️ 1️⃣ Creating Primitive Streams

### From Ranges
```java
IntStream range = IntStream.range(1, 5);      // 1, 2, 3, 4
IntStream rangeClosed = IntStream.rangeClosed(1, 5); // 1, 2, 3, 4, 5
```

### From Arrays
```java
int[] nums = {1, 2, 3, 4};
IntStream stream = Arrays.stream(nums);
```

### From Values
```java
IntStream values = IntStream.of(10, 20, 30);
DoubleStream doubles = DoubleStream.of(1.5, 2.5, 3.5);
```

### From Random Numbers
```java
IntStream randoms = new Random().ints(5, 1, 100); // 5 random ints between 1–99
randoms.forEach(System.out::println);
```

---

## ⚙️ 2️⃣ Common Primitive Stream Operations

| Operation | Purpose | Example |
|------------|----------|----------|
| `sum()` | Total of elements | `IntStream.of(1,2,3).sum()` → 6 |
| `average()` | Average as `OptionalDouble` | `IntStream.of(1,2,3).average().getAsDouble()` |
| `min()` / `max()` | Returns `OptionalInt` | `IntStream.of(2,5,1).max()` |
| `range()` / `rangeClosed()` | Sequential numbers | `IntStream.range(1,4)` → 1,2,3 |
| `boxed()` | Convert primitive → Stream<T> | `IntStream.of(1,2,3).boxed()` |
| `mapToInt()` / `mapToLong()` / `mapToDouble()` | Convert object → primitive | see below |

---

## 💡 3️⃣ Converting Between Object & Primitive Streams

### Object → Primitive
```java
List<String> numbers = List.of("1", "2", "3");
int sum = numbers.stream()
                 .mapToInt(Integer::parseInt)
                 .sum();
System.out.println(sum); // 6
```

### Primitive → Object
```java
List<Integer> list = IntStream.range(1, 5)
                              .boxed()
                              .toList();
System.out.println(list); // [1, 2, 3, 4]
```

### Map to Another Primitive
```java
IntStream.of(1, 2, 3)
         .mapToDouble(x -> x * 0.5)
         .forEach(System.out::println); // 0.5, 1.0, 1.5
```

---

## ⚙️ 4️⃣ Specialized Stream Methods

```java
IntStream.of(1, 2, 3, 4, 5).summaryStatistics();
```
Output:
```
IntSummaryStatistics{count=5, sum=15, min=1, average=3.0, max=5}
```

```java
DoubleStream.of(2.5, 3.5, 4.5)
    .map(x -> x * 2)
    .average()
    .ifPresent(System.out::println); // 7.0
```

---

# ☕ Optional Deep Dive

## 💡 What Is Optional?
`Optional<T>` represents a **value that might or might not be present**, replacing null checks safely.

```java
Optional<String> name = getOptionalName();
name.ifPresent(n -> System.out.println(n.toUpperCase()));
```

---

## ⚙️ 1️⃣ Creating Optionals

```java
Optional<String> nonEmpty = Optional.of("Hello");
Optional<String> empty = Optional.empty();
Optional<String> nullable = Optional.ofNullable(possibleNullValue);
```

---

## ⚙️ 2️⃣ Accessing Values

```java
Optional<String> name = Optional.of("Alice");

System.out.println(name.get());             // Alice
System.out.println(name.orElse("Unknown")); // Alice
System.out.println(name.orElseGet(() -> "Default")); // Alice
```

If you call `.get()` on an empty Optional → `NoSuchElementException`.

---

## ⚙️ 3️⃣ Functional Methods

| Method | Description | Example |
|---------|--------------|----------|
| `isPresent()` / `isEmpty()` | Check if value exists | `opt.isPresent()` |
| `ifPresent()` | Run action if exists | `opt.ifPresent(System.out::println)` |
| `map()` | Transform value | `opt.map(String::toUpperCase)` |
| `filter()` | Keep only if passes test | `opt.filter(v -> v.length() > 3)` |
| `orElse()` / `orElseGet()` / `orElseThrow()` | Fallback or exception | `opt.orElse("default")` |

---

## 💡 Example — Safe Null Handling

```java
Optional<String> maybeName = Optional.ofNullable(null);

String upper = maybeName
    .map(String::toUpperCase)
    .orElse("DEFAULT");

System.out.println(upper); // DEFAULT
```

---

## 💡 Example — With Streams

```java
List<Integer> numbers = List.of(1, 2, 3, 4);

OptionalDouble avg = numbers.stream()
                            .mapToInt(Integer::intValue)
                            .average();

avg.ifPresent(System.out::println);
```

---

# 🧾 Chapter 4 Summary

| Concept | Description | Example |
|----------|--------------|----------|
| `IntStream`, `LongStream`, `DoubleStream` | Primitive-specialized streams | `IntStream.of(1,2,3)` |
| `range()` / `rangeClosed()` | Generate sequential ints | `IntStream.range(1, 5)` |
| `boxed()` | Convert to Stream<T> | `IntStream.range(1,4).boxed()` |
| `mapToInt`, `mapToDouble` | Convert objects → primitives | `.mapToInt(Integer::parseInt)` |
| `sum()`, `average()`, `summaryStatistics()` | Built-in numeric aggregation | — |
| `Optional` | Container for maybe-present values | `Optional.ofNullable(x)` |
| `map`, `filter`, `orElse` | Transform or fallback safely | `opt.map(...).orElse(...)` |

---

## 🧩 Mini Quiz (with Answers)

**Q1.** What’s the difference between `Stream<Integer>` and `IntStream`?  
✅ `IntStream` skips boxing/unboxing overhead and stores primitives directly, making it faster and memory-efficient.

**Q2.** What does `IntStream.rangeClosed(1,3).boxed().toList()` produce?  
✅ `List<Integer> = [1, 2, 3]`  
Also, `IntStream.rangeClosed(1,3).toList()` (Java 16+) returns the same type, auto-boxed internally.

**Q3.**
```java
Optional<String> s = Optional.ofNullable(null);
System.out.println(s.orElse("fallback"));
```
✅ Output: `"fallback"`

---

✅ **End of Chapter 5**
Next: **Parallel Streams & Advanced Topics** — combining, short-circuiting, and parallel execution.
