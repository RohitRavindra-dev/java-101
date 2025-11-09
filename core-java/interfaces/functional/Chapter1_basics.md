# Functional Interfaces — Chapter 1 Cheatsheet

## 🧩 Overview

A **Functional Interface (FI)** is an interface that contains **exactly one abstract method**. These interfaces form the foundation for **lambda expressions** and **method references** in Java.

---

## 1️⃣ What Makes an Interface Functional

```java
interface Printer {
    void print(String message);
}
```

✅ Functional — one abstract method.

```java
interface Printer {
    void print(String message);
    void clear();
}
```

❌ Not functional — more than one abstract method.

> **Rule:** A functional interface has exactly one abstract method, but can have any number of `default` or `static` methods.

---

## 2️⃣ The `@FunctionalInterface` Annotation

This annotation enforces the single-abstract-method rule.

```java
@FunctionalInterface
interface Printer {
    void print(String message);
}
```

If someone adds another abstract method later:

```java
@FunctionalInterface
interface Printer {
    void print(String message);
    void clear(); // ❌ Error
}
```

Compiler error:

> *Unexpected @FunctionalInterface annotation – multiple non-overriding abstract methods found.*

---

## 3️⃣ Functional vs Normal Interfaces

| Feature             | Normal Interface             | Functional Interface                            |
| ------------------- | ---------------------------- | ----------------------------------------------- |
| Abstract Methods    | Any number                   | Exactly one                                     |
| Typical Use         | Define contract/blueprint    | Represent a single behavior/action              |
| Can use with Lambda | ❌ No                         | ✅ Yes                                           |
| Common Examples     | `Comparable`, `Serializable` | `Runnable`, `Callable`, `Predicate`, `Function` |

> Every **lambda expression** in Java implements a **functional interface** behind the scenes.

---

## 4️⃣ Relation with Anonymous Classes

Example using **anonymous class**:

```java
Runnable r = new Runnable() {
    public void run() {
        System.out.println("Running...");
    }
};
```

Same logic using **lambda expression**:

```java
Runnable r = () -> System.out.println("Running...");
```

Both are valid because `Runnable` has one abstract method — `run()`.

---

## 5️⃣ Step-by-Step Example

| Style                    | Code                                                                                              | Description           |
| ------------------------ | ------------------------------------------------------------------------------------------------- | --------------------- |
| **Class implementation** | `class PrinterImpl implements Printer { public void print(String m) { System.out.println(m); } }` | Traditional approach  |
| **Anonymous class**      | `Printer p = new Printer() { public void print(String m) { System.out.println(m); } };`           | Inline implementation |
| **Lambda expression**    | `Printer p = (m) -> System.out.println(m);`                                                       | Compact & modern      |

All three are equivalent.

---

## 6️⃣ Default and Static Methods

These **don’t count** as abstract methods and are perfectly valid inside functional interfaces.

```java
@FunctionalInterface
interface Calculator {
    int add(int a, int b);

    default void printInfo() {
        System.out.println("This is a calculator!");
    }

    static void staticInfo() {
        System.out.println("Static method inside FI");
    }
}
```

✅ Still a valid functional interface.

---

## 🧠 Summary Table

| Concept                     | Key Idea                                                      |
| --------------------------- | ------------------------------------------------------------- |
| Functional Interface        | Exactly one abstract method                                   |
| `@FunctionalInterface`      | Compile-time safeguard                                        |
| Default / Static Methods    | Allowed                                                       |
| Lambdas & Anonymous Classes | Both use functional interfaces under the hood                 |
| Common Examples             | `Runnable`, `Callable`, `Comparator`, `Predicate`, `Function` |

---

## 💡 Mini Quiz + Answers

**1.** Is this valid?

```java
@FunctionalInterface
interface A {
    void test();
    boolean equals(Object obj);
}
```

✅ **Yes** — `equals()` comes from `Object`, not counted as abstract.

---

**2. Which are valid functional interfaces?**

```java
interface B { void test(); }
interface C { void test(); void run(); }
interface D {
    default void helper() {}
    void execute();
}
```

✅ **B, D** — only one abstract method each.

---

**3. Fill in the blanks:**

```java
interface Worker { void doWork(String task); }
Worker w = (task) -> System.out.println("Doing task: " + task);
```

✅ Correct — valid lambda for a single-abstract-method interface.

---

**4. What happens if you remove `@FunctionalInterface`?**
✅ Nothing changes at runtime. It’s just a **safeguard** — not a requirement.

---

## 🏁 Chapter 1 Key Takeaways

* Functional Interface = one abstract method.
* The `@FunctionalInterface` annotation enforces it but isn’t mandatory.
* Lambdas rely on functional interfaces.
* Default & static methods are allowed.
* Anonymous classes are conceptually similar, but lambdas are more concise and optimized.

---

**Next Step:** → Chapter 2: Built-in Functional Interfaces (`java.util.function`)
