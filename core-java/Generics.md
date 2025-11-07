# 🧠 Java Generics Cheatsheet

---

## 🧩 1️⃣ Basics

| Concept | Example | Meaning |
|----------|----------|---------|
| **Generic Class** | `class Box<T> { T value; }` | Class parameterized by type `T` |
| **Generic Method** | `<T> void print(T item)` | Method parameterized by type `T` |
| **Bounded Type (extends)** | `<T extends Number>` | T can be `Number` or subclass |
| **Bounded Type (super)** | `<T super Integer>` | T can be `Integer` or superclass |
| **Multiple Bounds** | `<T extends Number & Comparable<T>>` | T must satisfy *both* |

---

## 🎯 2️⃣ Why Generics Exist

✅ Type-safety — compiler enforces types  
✅ Eliminates casting  
✅ Code reusability

```java
List<String> list = new ArrayList<>();
String s = list.get(0); // No cast needed
```

---

## 🔍 3️⃣ Type Erasure (Compile-time vs Runtime)

| Phase | Behavior |
|--------|-----------|
| **Compile-time** | Generic types are checked (type-safety enforced) |
| **Runtime** | Type info erased → becomes raw types (`T` → `Object` or upper bound) |

🧠 Example:
```java
class Box<T> { T value; }
```
After erasure → `class Box { Object value; }`

---

## ⚠️ 4️⃣ Erasure Consequences

| Limitation | Why |
|-------------|-----|
| `new T()` ❌ | JVM doesn’t know what `T` is at runtime |
| `T[] arr = new T[5]` ❌ | Array type unknown (use `(T[]) new Object[5]`) |
| `instanceof T` ❌ | Erased at runtime |
| Overloads may clash | `void m(List<String>)` and `void m(List<Integer>)` → same erasure (`List`) |

---

## 🧱 5️⃣ Bridge Methods (due to erasure)

| Problem | Fix |
|----------|-----|
| After erasure, subclass methods might no longer override superclass ones (signatures differ) | Compiler auto-creates a **bridge method** |

🧩 Example:
```java
class Parent<T> {
    T get() { return null; }
}

class Child extends Parent<String> {
    @Override
    String get() { return "Hi"; }
}
```

After erasure:
- `Parent` → `Object get()`
- `Child` → `String get()`
➡️ Compiler adds:

```java
Object get() { return get(); } // Bridge method
```

**Purpose:** preserve polymorphism.

---

## 🧾 6️⃣ Wildcards

| Type | Example | Meaning |
|-------|----------|---------|
| Unbounded | `List<?>` | List of unknown type |
| Upper-bounded | `List<? extends Number>` | Read-only Number or subclass |
| Lower-bounded | `List<? super Integer>` | Can write Integers or subclass |

💡 *PECS rule:* **Producer Extends, Consumer Super**

---

## 🧩 7️⃣ Best Practices

✅ Use generics in interfaces/classes  
✅ Prefer bounded types for constraints  
✅ Avoid raw types (`List` → `List<T>`)  
✅ Don’t mix generic and raw usage  
✅ Return generic type parameters for chaining

---

## 🧠 8️⃣ Quick Recap Map

| Topic | Keyword | Notes |
|--------|----------|-------|
| Generic Class | `<T>` | `class MyClass<T> {}` |
| Generic Method | `<T> T doSomething(T t)` | Defined at method level |
| Type Bound | `<T extends SomeClass>` | Restrict type |
| Wildcards | `<?>`, `<? extends>`, `<? super>` | Flexible arguments |
| Erasure | compile-time only types | runtime → raw types |
| Bridge Method | synthetic method | fixes erasure conflicts |
