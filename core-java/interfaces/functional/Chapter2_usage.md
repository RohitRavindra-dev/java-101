# Functional Interfaces — Chapter 2 Usage Bonus
**Goal:** Quick reference of common built-in FIs, their single abstract method (SAM), composition helpers, and usage patterns.

> ⚙️ How do chaining methods work internally?  
They are **default methods** on the interfaces that **return a new functional interface** wrapping your original lambda plus the extra behavior. No magic — just composition that creates a tiny delegating lambda.

---

## Function Family

### `Function<T, R>`
- **SAM:** `R apply(T t)`
- **Helpers:**
  - `default <V> Function<T, V> andThen(Function<? super R, ? extends V> after)`
  - `default <V> Function<V, R> compose(Function<? super V, ? extends T> before)`
  - `static <T> Function<T, T> identity()`
- **Examples**
  ```java
  Function<String, Integer> length = String::length;
  Function<Integer, String> label = n -> "len=" + n;
  Function<String, String> pipeline = length.andThen(label);
  pipeline.apply("abcd"); // "len=4"

  Function<String, String> viaCompose = label.compose(length);
  viaCompose.apply("abcd"); // "len=4"
  ```

### `UnaryOperator<T>` (extends Function<T, T>)
- **SAM:** `T apply(T t)`
- **Helpers:** inherits `andThen`, `compose`, `identity`
- **Examples**
  ```java
  UnaryOperator<String> trim = String::trim;
  UnaryOperator<String> upper = String::toUpperCase;
  trim.andThen(upper).apply("  hi "); // "HI"
  ```

### Primitive `*Function` forms
- **`IntFunction<R>`** — `R apply(int value)`  
- **`LongFunction<R>`** — `R apply(long value)`  
- **`DoubleFunction<R>`** — `R apply(double value)`  
- **`ToIntFunction<T>`** — `int applyAsInt(T value)` (and `ToLongFunction`, `ToDoubleFunction`)  
- **`IntToDoubleFunction`**, `IntToLongFunction`, etc.
- **Helpers:** generally **no compose/andThen**; use wrapper `Function` if you need composition.
  ```java
  IntFunction<String> f = n -> "n=" + n;
  f.apply(5); // "n=5"
  ```

---

## Predicate Family

### `Predicate<T>`
- **SAM:** `boolean test(T t)`
- **Helpers:**
  - `default Predicate<T> and(Predicate<? super T> other)`
  - `default Predicate<T> or(Predicate<? super T> other)`
  - `default Predicate<T> negate()`
  - `static <T> Predicate<T> isEqual(Object targetRef)`
- **Examples**
  ```java
  Predicate<String> hasAt = s -> s.contains("@");
  Predicate<String> isGmail = s -> s.endsWith("@gmail.com");
  hasAt.and(isGmail).test("a@gmail.com"); // true
  Predicate<String> notGmail = isGmail.negate();
  ```

### `BiPredicate<T, U>`
- **SAM:** `boolean test(T t, U u)`
- **Helpers:** `and`, `or`, `negate`
- **Example**
  ```java
  BiPredicate<String, String> startsWith =
      (s, pref) -> s.startsWith(pref);
  startsWith.and((s, pref) -> s.length() > pref.length())
            .test("lambda", "lam"); // true
  ```

### Primitive `*Predicate`
- `IntPredicate`, `LongPredicate`, `DoublePredicate`
- **SAM:** `boolean test(int/long/double value)`
- **Helpers:** `and`, `or`, `negate`

---

## Consumer Family

### `Consumer<T>`
- **SAM:** `void accept(T t)`
- **Helpers:**
  - `default Consumer<T> andThen(Consumer<? super T> after)`
- **Examples**
  ```java
  Consumer<String> log = System.out::println;
  Consumer<String> save = s -> repo.save(s);
  log.andThen(save).accept("event");
  ```

### `BiConsumer<T, U>`
- **SAM:** `void accept(T t, U u)`
- **Helpers:** `andThen(BiConsumer<? super T, ? super U> after)`
- **Examples**
  ```java
  BiConsumer<String, Integer> print = (n, a) -> System.out.println(n + ":" + a);
  BiConsumer<String, Integer> cache = (n, a) -> map.put(n, a);
  print.andThen(cache).accept("Alice", 30);
  ```

### Primitive consumers
- `IntConsumer`, `LongConsumer`, `DoubleConsumer`
- **SAM:** `void accept(int/long/double value)`
- **Helpers:** `andThen`

---

## Supplier Family

### `Supplier<T>`
- **SAM:** `T get()`
- **Helpers:** none (no chaining). Compose manually by wrapping:
  ```java
  Supplier<String> token = () -> UUID.randomUUID().toString();
  Supplier<String> logged = () -> { 
      String t = token.get();
      System.out.println("gen " + t);
      return t;
  };
  ```

### Primitive suppliers
- `BooleanSupplier` — `boolean getAsBoolean()`  
- `IntSupplier`, `LongSupplier`, `DoubleSupplier` — `getAsInt()` etc.
- **Helpers:** none

---

## Binary/Operator Helpers

### `BinaryOperator<T>` (extends BiFunction<T,T,T>)
- **SAM:** `T apply(T t, T u)`
- **Helpers:**
  - `static <T> BinaryOperator<T> minBy(Comparator<? super T> comparator)`
  - `static <T> BinaryOperator<T> maxBy(Comparator<? super T> comparator)`
- **Examples**
  ```java
  BinaryOperator<Integer> add = Integer::sum;
  BinaryOperator<String> shorter = BinaryOperator.minBy(Comparator.comparingInt(String::length));
  shorter.apply("lambda", "fi"); // "fi"
  ```

### `BiFunction<T, U, R>`
- **SAM:** `R apply(T t, U u)`
- **Helpers:** `default <V> BiFunction<T, U, V> andThen(Function<? super R, ? extends V> after)`
- **Example**
  ```java
  BiFunction<Integer, Integer, Integer> sum = Integer::sum;
  Function<Integer, String> asText = n -> "total=" + n;
  sum.andThen(asText).apply(3, 4); // "total=7"
  ```

### Primitive operators
- `IntUnaryOperator`, `LongUnaryOperator`, `DoubleUnaryOperator`
- `IntBinaryOperator`, `LongBinaryOperator`, `DoubleBinaryOperator`
- **SAMs:** `applyAsInt/Long/Double(...)`
- **Helpers:** unary has `andThen`, `compose`; binary has none (but can be combined using higher-order functions).

---

## Are helpers the same across all FIs?
- **No.** Families differ:
  - **Function/UnaryOperator**: `compose`, `andThen`, `identity`
  - **Predicate**: `and`, `or`, `negate`, `isEqual`
  - **Consumer/BiConsumer**: `andThen`
  - **BiFunction**: `andThen`
  - **Supplier**: none
  - **BinaryOperator**: static `minBy`, `maxBy`
  - **Primitive variants**: limited (often lack compose/andThen; use wrappers)

---

## Practical chaining patterns

```java
// Validate → Transform → Act
Predicate<User> valid = u -> u.email() != null && u.email().contains("@");
Function<User, String> toEmail = User::email;
Consumer<String> send = mailer::send;

users.stream()
     .filter(valid)    // Predicate
     .map(toEmail)     // Function
     .forEach(send);   // Consumer
```

```java
// Combine two inputs then format
BiFunction<BigDecimal, BigDecimal, BigDecimal> add = BigDecimal::add;
Function<BigDecimal, String> fmt = n -> n.setScale(2, RoundingMode.HALF_UP).toPlainString();
String out = add.andThen(fmt).apply(new BigDecimal("10.2"), new BigDecimal("3.45"));
// "13.65"
```

---

## Mental model
- Each helper returns a **new** FI that, when invoked, calls your lambdas in the specified order (e.g., `andThen` runs first lambda, then second with the result). This keeps your code declarative and composable without mutation.