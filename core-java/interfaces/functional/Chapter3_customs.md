# Functional Interfaces — Chapter 3 Cheatsheet

## ⚙️ Topic: Custom Functional Interfaces + Real-world Usages

---

### 🧱 1️⃣ Creating Your Own Functional Interface

A **custom functional interface** has exactly one abstract method.

```java
@FunctionalInterface
interface Calculator {
    int operate(int a, int b);
}

Calculator add = (a, b) -> a + b;
Calculator multiply = (a, b) -> a * b;

System.out.println(add.operate(3, 5));      // 8
System.out.println(multiply.operate(3, 5)); // 15
```

✅ Behaves like built-in ones (`Function`, `BiFunction`, etc.), but allows domain-specific naming.

---

### 🧩 2️⃣ Mixing Custom FIs with Built-ins

You can mix your own FIs with built-ins to create flexible APIs.

```java
void runOperation(Calculator calc) {
    System.out.println(calc.operate(10, 5));
}

runOperation((a, b) -> a - b); // prints 5
```

You can even combine a custom FI with a built-in one:

```java
Function<Integer, Integer> doubler = x -> x * 2;
Calculator adder = (a, b) -> doubler.apply(a) + doubler.apply(b);
System.out.println(adder.operate(2, 3)); // 10
```

---

### 🔁 3️⃣ Combining Functional Interfaces

You can design composable interfaces like this:

```java
@FunctionalInterface
interface Validator<T> {
    boolean validate(T input);
}

Validator<String> nonEmpty = s -> !s.isEmpty();
Validator<String> containsAt = s -> s.contains("@");

Validator<String> validEmail = s -> nonEmpty.validate(s) && containsAt.validate(s);
System.out.println(validEmail.validate("rohit@gmail.com")); // true
```

To make it chainable (like `Predicate`):

```java
@FunctionalInterface
interface Validator<T> {
    boolean validate(T input);

    default Validator<T> and(Validator<T> other) {
        return t -> this.validate(t) && other.validate(t);
    }
}

Validator<String> valid = nonEmpty.and(containsAt);
System.out.println(valid.validate("abc@gmail.com")); // true
```

---

### 💼 4️⃣ Real-world Use: Functional Interfaces in Spring Boot

#### ✅ Example 1: Generic Retry Handler

```java
@FunctionalInterface
interface RetryableTask {
    void execute() throws Exception;
}

public void retry(RetryableTask task, int attempts) {
    for (int i = 0; i < attempts; i++) {
        try {
            task.execute();
            return;
        } catch (Exception e) {
            System.out.println("Attempt " + (i + 1) + " failed: " + e.getMessage());
        }
    }
}

retry(() -> myService.callExternalAPI(), 3);
```

Perfect for implementing retry or fallback logic.

---

#### ✅ Example 2: Functional Configuration in Spring Boot

```java
@Bean
public Function<String, String> greeter() {
    return name -> "Hello, " + name + "!";
}

@Autowired
Function<String, String> greeter;

System.out.println(greeter.apply("Rohit"));
```

This shows how you can inject a **lambda-defined bean** into your Spring context.

---

#### ✅ Example 3: Request Filters or Middleware Logic

```java
@FunctionalInterface
interface RequestFilter {
    boolean allow(String token);
}

RequestFilter jwtFilter = token -> token != null && token.startsWith("Bearer ");

if (jwtFilter.allow("Bearer xyz123")) {
    System.out.println("Access granted");
}
```

Useful for request validation or authentication checks.

---

### 🧠 5️⃣ Functional Interfaces + Streams (Custom + Built-in)

Custom interfaces can also integrate seamlessly with streams:

```java
@FunctionalInterface
interface Transformer<T, R> {
    R transform(T input);
}

List<String> names = List.of("rohit", "arjun", "sanya");
Transformer<String, String> capitalize = s -> s.substring(0,1).toUpperCase() + s.substring(1);

List<String> result = names.stream()
    .map(capitalize::transform)
    .toList();

System.out.println(result); // [Rohit, Arjun, Sanya]
```

---

### 🧰 6️⃣ Pro Tips for Real Projects

| Tip                      | Reason                                                        |
| ------------------------ | ------------------------------------------------------------- |
| ✅ Prefer built-in FIs    | They are optimized, standardized, and well understood         |
| 💡 Custom FIs            | Use only when semantics matter (`Validator`, `RetryableTask`) |
| ⚡ Keep lambdas pure      | Avoid side-effects for testability                            |
| 🧩 Mix custom + built-in | Composition-friendly code                                     |
| 🧠 Keep scope small      | Each FI should represent a single action                      |

---

### 🎯 Mini Quiz (with Answers)

**1. Define a `Logger` functional interface and use it:**

```java
@FunctionalInterface
interface Logger {
    void log(String msg);
}

Logger logger = msg -> System.out.println("LOG: " + msg);
logger.log("Server started");
```

**2. Add a default method:**

```java
@FunctionalInterface
interface Logger {
    void log(String msg);

    default void logError(String msg) {
        System.out.println("ERROR: " + msg);
    }
}

Logger logger = msg -> System.out.println("LOG: " + msg);
logger.log("Task complete");
logger.logError("Failed to connect to DB");
```

**3. Math Operation example:**

```java
@FunctionalInterface
interface MathOperation {
    int operate(int a, int b);
}

public void calculate(int a, int b, MathOperation op) {
    System.out.println("Result is: " + op.operate(a, b));
}

MathOperation addition = (a, b) -> a + b;
MathOperation multiplication = (a, b) -> a * b;

calculate(1, 2, addition);       // Result is: 3
calculate(2, 3, multiplication); // Result is: 6
```

**4. When to create your own FI?**

> Prefer built-ins for simplicity and optimization. Only create custom FIs when existing ones don’t fit semantically or you need domain-specific clarity (e.g., `RetryableTask`, `Validator`, `Transformer`).

---

### 🧭 Key Takeaways

* Custom FIs = clarity for domain logic.
* Default methods extend functionality safely.
* Built-ins and customs mix seamlessly.
* Used heavily in Spring Boot, Streams, and asynchronous logic.

---

**Next Deep Dive →** Lambdas: Syntax, Scope, Capturing, and Optimization.
