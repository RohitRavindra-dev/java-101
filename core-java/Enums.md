# 🧩 Java Enum Deep Dive Cheatsheet

Comprehensive, example-rich reference covering everything from enum basics to advanced backend integration and patterns.

---

## 🧱 Chapter 1: Enum Basics

### 💡 What is an Enum?
- Enum = special Java type used for defining a **fixed set of constants**.
- Internally, every enum extends `java.lang.Enum<T>`.
- Each constant is a **singleton instance**.
- Enums are **type-safe** and can be used in switch statements.

```java
enum Direction {
    NORTH, SOUTH, EAST, WEST
}
```

### ✅ Key Points
- Each constant (`NORTH`, `SOUTH`, etc.) is an **object** of type `Direction`.
- Compare enums using `==` (not `.equals()`).
- Enum constants are created **once** when the enum is loaded.
- Enums can’t be extended but **can implement interfaces.**

### ⚙️ Example: Basic Usage
```java
enum TrafficLight { RED, YELLOW, GREEN; }

public class Example {
    public static void main(String[] args) {
        TrafficLight signal = TrafficLight.RED;

        switch (signal) {
            case RED -> System.out.println("Stop!");
            case YELLOW -> System.out.println("Get ready!");
            case GREEN -> System.out.println("Go!");
        }
    }
}
```

### ⚠️ Enums vs static final constants
| Aspect | static final | enum |
|---------|--------------|------|
| Type safety | ❌ none | ✅ enforced |
| Extendable | ❌ no | ✅ with logic |
| Comparison | value-based | instance-based |
| Compile-time validation | ❌ none | ✅ yes |

Example:
```java
class DirectionOld {
    public static final int NORTH = 0;
}

public void move(int direction) {
    if (direction == DirectionOld.NORTH) {}
}
// move(99); ✅ compiles, ❌ meaningless
```

Using enum:
```java
enum Direction { NORTH, SOUTH, EAST, WEST }
void move(Direction d) {}
// move(99); ❌ compile error
```

---

## ⚙️ Chapter 2: Enum Fields, Constructors, and Methods

Enums are **full-fledged classes** — they can have fields, constructors, and methods.

```java
enum Status {
    SUCCESS(200), ERROR(500);

    private final int code;
    Status(int code) { this.code = code; }
    public int getCode() { return code; }
}
```

### 💡 Notes
- Constructors are **private** or package-private by default.
- Each constant calls the constructor once.
- You can add methods — both static and instance.

### ⚙️ Enum with Custom Behavior per Constant
```java
enum Operation {
    ADD { public double apply(double x, double y) { return x + y; } },
    SUBTRACT { public double apply(double x, double y) { return x - y; } },
    MULTIPLY { public double apply(double x, double y) { return x * y; } },
    DIVIDE { public double apply(double x, double y) { return x / y; } };

    public abstract double apply(double x, double y);
}
```

### ⚙️ Enum with Shared Logic
```java
enum HttpStatus {
    OK(200), BAD_REQUEST(400), SERVER_ERROR(500);

    private final int code;
    HttpStatus(int code) { this.code = code; }

    public boolean isError() { return code >= 400; }
}
```

### ✅ Notes
- Abstract methods in enums force every constant to implement them.
- Great for replacing `if-else` or `switch` logic.
- Constants can override methods selectively.

---

## 🧩 Chapter 3: Built-in Enum API

### 🔹 `values()`
Returns all constants:
```java
for (Direction d : Direction.values()) System.out.println(d);
```

### 🔹 `ordinal()`
Returns position (0-based index). Avoid using it in logic.

### 🔹 `name()`
Returns exact constant name.

### 🔹 `valueOf(String)`
Converts string to enum (throws `IllegalArgumentException` if invalid).

### ✅ Safe Parser Pattern
```java
public static Direction fromString(String s) {
    for (Direction d : values())
        if (d.name().equalsIgnoreCase(s)) return d;
    return null;
}
```

### 🔹 Stream Example
```java
Arrays.stream(Direction.values())
      .filter(d -> d.name().startsWith("S"))
      .forEach(System.out::println);
```

### 🔹 Safe Lookup with Optional
```java
Optional<Direction> safeDir = Arrays.stream(Direction.values())
                                    .filter(d -> d.name().equalsIgnoreCase(input))
                                    .findFirst();
```

---

## 🧠 Chapter 4: Strategy Pattern & Real Scenarios

Enums can directly hold business logic.

### ⚙️ Example: Cleaner Alternative to Switch
```java
enum OrderStatus {
    PENDING { public String message() { return "Order placed."; } },
    SHIPPED { public String message() { return "Dispatched."; } },
    DELIVERED { public String message() { return "Delivered."; } };

    public abstract String message();
}
```

### ⚙️ Example: Partial Overrides
```java
enum HttpStatus {
    OK(200), BAD_REQUEST(400), SERVER_ERROR(500) {
        @Override public boolean isError() { return true; }
    };

    private final int code;
    HttpStatus(int code) { this.code = code; }
    public boolean isError() { return code >= 400; }
}
```

### ⚙️ Enum with Interface
```java
interface Command { void execute(); }

enum FileCommand implements Command {
    CREATE { public void execute() { System.out.println("File created"); } },
    DELETE { public void execute() { System.out.println("File deleted"); } };
}
```

### ✅ Benefits
- Compiler enforces coverage.
- Easier to extend.
- Cleaner, encapsulated design.

---

## 🌐 Chapter 5: Spring Boot Integration

### 🔹 Default Behavior
- JSON serialization uses `name()`.
- Deserialization converts back via `valueOf()`.

```java
enum Role { ADMIN, USER }
record User(String name, Role role) {}
```

```json
{"name": "Rohit", "role": "ADMIN"}
```

### 🔹 Custom JSON Value
```java
enum Status {
    SUCCESS("Success"), ERROR("Error");

    private final String label;
    Status(String label) { this.label = label; }

    @JsonValue
    public String getLabel() { return label; }
}
```

### 🔹 Custom JSON Parsing
```java
@JsonCreator
public static Role fromString(String key) {
    return key == null ? null : Role.valueOf(key.toUpperCase());
}
```

### 🔹 Enums in JPA
```java
@Enumerated(EnumType.STRING)
private Role role; // stores name in DB
```

Never use ORDINAL in production.

### 🔹 Custom Converter
```java
@Converter(autoApply = true)
public class PaymentStatusConverter implements AttributeConverter<PaymentStatus, String> {
    public String convertToDatabaseColumn(PaymentStatus s) { return s.getCode(); }
    public PaymentStatus convertToEntityAttribute(String db) {
        return PaymentStatus.fromCode(db);
    }
}
```

### 🔹 Binding from Path/Params
```java
@GetMapping("/users/{role}")
public String getByRole(@PathVariable Role role) { return role.name(); }
```

---

## ⚡ Chapter 6: Advanced Patterns

### 🔹 EnumSet
- Backed by bit vector, super fast.
- Only works with enums.

```java
enum Permission { READ, WRITE, DELETE }
EnumSet<Permission> set = EnumSet.of(Permission.READ, Permission.WRITE);
```

### 🔹 EnumMap
- Map optimized for enum keys.

```java
enum Currency { USD, INR }
EnumMap<Currency, Double> rates = new EnumMap<>(Currency.class);
rates.put(Currency.USD, 1.0);
```

### 🔹 Reverse Lookup
```java
enum PaymentStatus {
    SUCCESS("S"), FAILED("F"), PENDING("P");
    private final String code;

    private static final Map<String, PaymentStatus> BY_CODE = new HashMap<>();
    static { for (PaymentStatus s : values()) BY_CODE.put(s.code, s); }

    public static PaymentStatus fromCode(String code) { return BY_CODE.get(code); }
}
```

### 🔹 Enum Singleton
```java
public enum AppConfig {
    INSTANCE;

    private final Properties props;
    AppConfig() {
        props = new Properties();
        props.setProperty("env", "prod");
    }

    public String get(String key) { return props.getProperty(key); }
}
```

#### ✅ Benefits
- Reflection-proof
- Serialization-safe
- Thread-safe
- JVM ensures single instance

```java
AppConfig.INSTANCE.get("env");
```

### 🔹 Multiple Enum Instances Example
```java
public enum Example {
    INSTANCE_ONE, INSTANCE_TWO;

    private final Map<String, String> cache = new ConcurrentHashMap<>();
}
// Each constant has its own independent cache instance.
```

### 🔹 Class-based vs Enum-based Singleton
| Aspect | Class Singleton | Enum Singleton |
|---------|-----------------|----------------|
| Thread safety | Needs sync | JVM handles |
| Reflection safe | ❌ | ✅ |
| Serialization safe | ❌ Needs readResolve | ✅ Auto |
| Simplicity | Boilerplate | One-liner |

---

## 🚀 Quick Reference Table

| Method / Annotation | Purpose | Notes |
|---------------------|----------|-------|
| `values()` | Returns all constants | Generated automatically |
| `name()` | Constant name | Same as declared |
| `ordinal()` | Index of constant | Avoid in logic |
| `valueOf(String)` | Parse String → Enum | Throws if invalid |
| `@JsonValue` | Custom JSON output | Used by Jackson |
| `@JsonCreator` | Custom JSON parsing | Used by Jackson |
| `@Enumerated(EnumType.STRING)` | Store enum name in DB | Safe for persistence |
| `EnumSet` | Optimized Set for enums | Backed by bit vector |
| `EnumMap` | Optimized Map for enums | Array-index lookup |
| `AttributeConverter` | Custom DB mapping | Converts enum <-> DB code |

---

## 🧠 Key Takeaways

✅ Enums are classes with predefined instances.
✅ Use for type-safe constants, business logic, and design.
✅ Prefer `EnumType.STRING` in JPA.
✅ Combine `@JsonValue` + `@JsonCreator` for JSON control.
✅ Use `EnumSet` and `EnumMap` for high performance.
✅ Enum Singleton is the cleanest, safest singleton in Java.
✅ Instance fields → each constant gets its own copy.
✅ Static fields → shared across all constants.

---

**End of Cheatsheet — Java Enum Deep Dive (Complete Reference)**
