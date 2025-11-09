# Functional Interfaces — Chapter 2
**Topic:** Built-in Functional Interfaces (`java.util.function`)

---

## Why use built-ins?
To avoid creating ad-hoc single-method interfaces for common patterns (transform, consume, test, supply), Java ships a set of ready FIs that play nicely with Streams, Optionals, and APIs.

---

## Core Four

### 1) `Function<T, R>` — transform
- **SAM:** `R apply(T t)`
- **Examples**
  ```java
  Function<String, Integer> toLen = s -> s.length();
  int len = toLen.apply("Walmart"); // 7

  Function<Integer, Integer> doubler = n -> n * 2;
  Function<Integer, Integer> square = n -> n * n;
  doubler.andThen(square).apply(3); // (3*2)^2 = 36
  doubler.compose(square).apply(3); // (3^2)*2 = 18
  ```

### 2) `Consumer<T>` — act, no return
- **SAM:** `void accept(T t)`
- **Examples**
  ```java
  Consumer<String> printer = msg -> System.out.println("Hello " + msg);
  printer.accept("Rohit");

  Consumer<String> upper = s -> System.out.println(s.toUpperCase());
  Consumer<String> lower = s -> System.out.println(s.toLowerCase());
  upper.andThen(lower).accept("Java");
  ```

### 3) `Supplier<T>` — provide value, no input
- **SAM:** `T get()`
- **Examples**
  ```java
  Supplier<Double> random = Math::random;
  Double x = random.get();

  Supplier<String> token = () -> UUID.randomUUID().toString();
  ```

### 4) `Predicate<T>` — boolean test
- **SAM:** `boolean test(T t)`
- **Examples**
  ```java
  Predicate<Integer> isEven = n -> n % 2 == 0;
  isEven.test(4); // true

  Predicate<Integer> pos = n -> n > 0;
  Predicate<Integer> small = n -> n < 10;
  pos.and(small).test(5);  // true
  pos.and(small).test(50); // false
  ```

---

## “Bi-” versions (two inputs)
- **`BiFunction<T, U, R>`** — `R apply(T t, U u)`
  ```java
  BiFunction<Integer, Integer, Integer> sum = (a, b) -> a + b;
  sum.apply(3, 5); // 8
  ```
- **`BiConsumer<T, U>`** — `void accept(T t, U u)`
  ```java
  BiConsumer<String, Integer> print = (name, age) ->
      System.out.println(name + " is " + age);
  print.accept("Rohit", 25);
  ```
- **`BiPredicate<T, U>`** — `boolean test(T t, U u)`
  ```java
  BiPredicate<String, String> startsWith = (s, pref) -> s.startsWith(pref);
  startsWith.test("lambda", "lam"); // true
  ```

---

## Variants you’ll actually use
- **`UnaryOperator<T>`** = `Function<T, T>`
  ```java
  UnaryOperator<Integer> square = n -> n * n;
  square.apply(5); // 25
  ```
- **`BinaryOperator<T>`** = `BiFunction<T, T, T>`
  ```java
  BinaryOperator<Integer> adder = (a, b) -> a + b;
  adder.apply(4, 6); // 10
  ```
- **Primitive specializations** (avoid boxing):
  - `IntPredicate`, `LongPredicate`, `DoublePredicate`
  - `IntUnaryOperator`, `LongUnaryOperator`, `DoubleUnaryOperator`
  - `IntBinaryOperator`, `LongBinaryOperator`, `DoubleBinaryOperator`
  - `IntFunction<R>`, `LongFunction<R>`, `DoubleFunction<R>`
  - `ToIntFunction<T>`, `ToLongFunction<T>`, `ToDoubleFunction<T>`
  - `IntToLongFunction`, `IntToDoubleFunction`, etc.

**Requested example (`IntFunction`)**
```java
IntFunction<String> converter = num -> "Number: " + num;
converter.apply(10); // "Number: 10"
```

---

## Real-world snippets

### Streams
```java
List<Integer> nums = List.of(1, 2, 3, 4, 5, 6);
nums.stream()
    .filter(n -> n % 2 == 0)  // Predicate
    .map(n -> n * n)          // Function
    .forEach(System.out::println); // Consumer
```

### Converting List<String> → List<Integer>
```java
List<String> raw = List.of("1", "2", "3");
List<Integer> ints = raw.stream()
    .map(Integer::parseInt) // Function<String, Integer>
    .toList();
```

### (Spring Boot) Bean via Supplier
```java
@Bean
public Supplier<Connection> connectionSupplier() {
    return () -> DriverManager.getConnection(url, user, pass);
}
```

---

## Mini Quiz (with solutions)

**1. Fill the blank**
```java
Function<Integer, Integer> doubler = (val) -> val * 2;
System.out.println(doubler.apply(5)); // 10
```

**2. Difference between `Function<T,R>` and `UnaryOperator<T>`**
- `UnaryOperator<T>` is a `Function<T, T>` (input and output same type). Prefer it to signal intent.

**3. Choose the FI**
- a) “Check if string length > 5” → `Predicate<String>`  
- b) “Print all elements in a list” → `Consumer<String>`  
- c) “Generate random number” → `Supplier<Integer>` or `Supplier<Double>`  
- d) “Add two integers” → `BinaryOperator<Integer>` (or primitive `IntBinaryOperator`)

**4. Output?**
```java
Predicate<String> startsWithA = s -> s.startsWith("A");
Predicate<String> endsWithZ = s -> s.endsWith("Z");
System.out.println(startsWithA.or(endsWithZ).test("ABZ")); // true
```

---

## Key takeaways
- Know the core 4 + Bi- + Operator variants.
- Prefer primitive specializations in perf-sensitive paths.
- Composition methods (`andThen`, `compose`, `and`, `or`, `negate`) create small wrapper lambdas — great for pipeline readability.