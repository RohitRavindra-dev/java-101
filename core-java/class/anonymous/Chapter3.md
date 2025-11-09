# Anonymous Classes Deep Dive — Chapter 3

## 🧭 Overview

Anonymous classes were powerful for quick one-off implementations before Java 8, but they came with verbosity and design limitations. This chapter explores their weaknesses and explains how **Lambdas** fixed those problems — serving as a natural evolution of the same concept.

---

## 🧩 Section 1: Limitations of Anonymous Classes

### Example 1 — Verbosity

```java
Runnable r1 = new Runnable() {
    public void run() {
        System.out.println("Running task...");
    }
};
```

vs.

```java
Runnable r2 = () -> System.out.println("Running task...");
```

Both achieve the same outcome, but lambdas are far more concise and readable.

---

### Example 2 — Nested Anonymous Classes Reduce Readability

```java
button.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        new Thread(new Runnable() {
            public void run() {
                System.out.println("Processing in background...");
            }
        }).start();
    }
});
```

Deep nesting makes code harder to follow and debug.

---

### Example 3 — Boilerplate and Type Redundancy

```java
Comparator<String> comp = new Comparator<String>() {
    public int compare(String s1, String s2) {
        return s1.compareToIgnoreCase(s2);
    }
};
```

Too much ceremony for a single comparison. Lambdas remove this overhead.

---

### Example 4 — Object Identity Difference

Anonymous classes generate separate `.class` files at compile time:

```java
Runnable r1 = new Runnable() { public void run() {} };
Runnable r2 = new Runnable() { public void run() {} };
System.out.println(r1.getClass() == r2.getClass());
```

**Output:** `false`

Each `new Runnable() {}` is treated as a *unique anonymous class definition* in source code.

---

### Appendix — How Java Numbers and Tracks Anonymous Classes

Each anonymous class gets compiled as a new `.class` file. For example:

```java
class Example {
    Runnable r1 = new Runnable() { public void run() {} }; // -> Example$1.class
    Runnable r2 = new Runnable() { public void run() {} }; // -> Example$2.class
}
```

If both appear in the same file, the compiler numbers them sequentially (`$1`, `$2`, etc.).

#### ✅ Rule of Thumb

| Scenario                                  | Same or Different Class?  | Explanation                                 |
| ----------------------------------------- | ------------------------- | ------------------------------------------- |
| Two distinct anonymous class expressions  | ❌ Different               | Compiler generates `Example$1`, `Example$2` |
| Multiple objects from the same expression | ✅ Same                    | Same `.class` reused                        |
| Same expression in different methods      | ❌ Different               | Each generates its own class                |
| Lambdas                                   | ✅ Same synthetic function | Implemented via `invokedynamic`             |

Internally, the compiler replaces each `new Runnable() {}` expression with a new generated class definition and assigns it a unique number based on its order of appearance.

```java
Runnable r1 = new Runnable() { public void run() {} }; // Example$1
Runnable r2 = new Runnable() { public void run() {} }; // Example$2
```

If you call the *same expression* repeatedly (e.g., from within a method), those instances share the same generated class.

---

### Example 5 — `this` Confusion

In anonymous classes, `this` refers to **the anonymous instance**, not the enclosing one.

```java
class Test {
    void print() {
        Runnable r = new Runnable() {
            public void run() {
                System.out.println(this.getClass().getName());
            }
        };
        r.run();
    }
}
```

Output:

```
Test$1
```

Lambdas fix this — in a lambda, `this` always refers to the **enclosing class instance**.

---

## 🧩 Section 2: What Lambdas Fixed

### ✅ Cleaner Syntax

```java
Runnable r = () -> System.out.println("Task running!");
```

No `new`, no `public void`, no boilerplate.

### ✅ Type Inference

```java
Comparator<String> comp = (a, b) -> a.compareToIgnoreCase(b);
```

No need to repeat types.

### ✅ Functional Interface Support

Lambdas work only with **functional interfaces** — interfaces with one abstract method (e.g., `Runnable`, `Comparator`, `Callable`, `Predicate`).

### ✅ Runtime Optimization

Lambdas use the **`invokedynamic`** bytecode instruction rather than generating new `.class` files. This reduces class loading and improves performance.

### ✅ Scoping Fix

In lambdas, `this` refers to the enclosing object, not the lambda instance:

```java
class Demo {
    void print() {
        Runnable r = () -> System.out.println(this.getClass().getName());
        r.run();
    }
}
```

Output:

```
Demo
```

---

## 🧩 Section 3: When to Use What

| Situation                        | Use Anonymous Class | Use Lambda      |
| -------------------------------- | ------------------- | --------------- |
| Multiple abstract methods        | ✅ Required          | ❌ Not possible  |
| Need custom fields/methods       | ✅ Yes               | ❌ No            |
| Want `this` to mean new instance | ✅ Yes               | ❌ No            |
| Short one-liner function         | ❌ Overkill          | ✅ Ideal         |
| Pre–Java 8                       | ✅ Required          | ❌ Not supported |
| Modern Java (8+)                 | ❌ Legacy            | ✅ Preferred     |

### Example — Sorting

Anonymous class:

```java
List<String> list = Arrays.asList("b", "a", "c");
Collections.sort(list, new Comparator<String>() {
    public int compare(String s1, String s2) {
        return s1.compareTo(s2);
    }
});
```

Lambda:

```java
List<String> list = Arrays.asList("b", "a", "c");
list.sort((s1, s2) -> s1.compareTo(s2));
```

---

## ⚖️ Summary

| Concept                   | Anonymous Class            | Lambda Expression              |
| ------------------------- | -------------------------- | ------------------------------ |
| Syntax                    | Verbose                    | Concise                        |
| Compiled to               | Separate `.class`          | `invokedynamic`                |
| `this` refers to          | Inner instance             | Enclosing instance             |
| Fields/methods            | ✅ Can define               | ❌ Cannot define                |
| Multiple abstract methods | ✅ Works                    | ❌ Not supported                |
| Typical use               | Event listeners, callbacks | Functional interfaces, streams |

---

## 🧩 Mini Quiz — Chapter 3

### 1️⃣ Output

```java
Runnable r = new Runnable() {
    public void run() {
        System.out.println(this.getClass().getName());
    }
};
r.run();
```

**Answer:** Prints something like `Main$1` (class name and order depend on its position in source file).

### 2️⃣ Output

```java
class MyClass {
    void test() {
        Runnable r = () -> System.out.println(this.getClass().getName());
        r.run();
    }
}
new MyClass().test();
```

**Answer:** Prints `MyClass` because lambdas bind `this` to the enclosing instance.

### 3️⃣ Why can’t lambdas replace all anonymous classes?

Because lambdas can only be used with **functional interfaces** (one abstract method) and cannot have their own fields, methods, or constructors.

### 4️⃣ Are lambdas objects or syntax sugar for anonymous classes?

Lambdas are **not anonymous classes** — they compile to lightweight runtime objects created via `invokedynamic`, not `.class` files.

### 5️⃣ When prefer anonymous classes in modern Java?

When you need:

* Multiple abstract methods
* Custom data or helper logic
* Distinct instance identity or `this` reference

---

## ✅ Final Takeaway

Anonymous classes paved the way for functional programming in Java. They remain valuable for non-functional interfaces and short-lived subclassing, but lambdas are the go-to choice for modern, expressive, and performant code.

---

**Next → Lambda Expressions Deep Dive (the natural continuation of this series)**
