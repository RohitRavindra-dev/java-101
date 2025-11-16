# Chapter 4 — Best Practices, Gotchas & Real Backend Usage (Complete Cheat Sheet)

This cheat sheet contains the entire Chapter 4 content in a clean, complete, standalone format.

---

# 1. When to Use Each Inner Class Type

## Member Inner Class — Use When:
- The inner class **depends on an outer instance**
- It models a component/part of the outer class
- You need direct access to outer fields (even private)

Examples:  
`Tree.Node`, `Order.Item`, `Graph.Edge`.

Avoid when:
- No instance coupling is needed  
- You risk leaking the outer object by storing the inner instance in long-lived structures  

---

## Static Nested Class — Use When:
- You need grouping without requiring an instance
- You’re designing a **builder pattern**
- You want a clean, namespaced API

Examples:  
`Map.Entry`, `ResponseEntity.BodyBuilder`, `HttpStatus.Series`.

Avoid when:
- You actually need access to instance members  

---

## Local Class — Use When:
- Logic exists only inside a method
- You want a small helper class with state

Avoid when:
- Logic grows too large  
- You need reuse outside the method  

---

## Anonymous Class — Use When:
- You need to override multiple methods quickly
- You need your own fields
- You need a new `this` binding

Avoid when:
- You can use a lambda  
- Logic becomes complex  

---

## Lambda — Use When:
- Implementing a **functional interface**
- You want modern, clean syntax
- You want the most lightweight approach

Avoid when:
- You need fields  
- You need separate `this`  
- You must override multiple methods  

---

# 2. Real Backend Patterns Using Inner Classes

## Pattern 1 — Builder Pattern (Static Nested)
Most common enterprise usage:
```java
User u = new User.Builder().name("A").age(22).build();
```

---

## Pattern 2 — Fluent APIs / DSLs
Spring uses static nested builder types:
```java
ResponseEntity.status(200).body("OK");
```

---

## Pattern 3 — Strategy / Functional Logic
Modern Java uses lambdas:
```java
filter(users, u -> u.isActive());
```

---

## Pattern 4 — Event Listeners in Spring
Anonymous classes still appear:
```java
context.addApplicationListener(new ApplicationListener<>() {
    public void onApplicationEvent(E e) { ... }
});
```

---

## Pattern 5 — Internal Helpers
Private static nested classes encapsulate logic:
```java
private static class Validator { ... }
```

---

## Pattern 6 — Encapsulating State Machines
Member inner classes are useful when the state object needs the outer instance.

---

# 3. Gotchas & Pitfalls

## ⚠️ Gotcha 1 — Member Inner Class → Hidden Outer Reference
Compiler injects:
```
Outer this$0;
```
If stored in:
- caches  
- thread pools  
- static fields  

→ Outer instance never gets GC’d → **memory leak** risk.

---

## ⚠️ Gotcha 2 — Overuse of Inner Classes → Hard-to-read Code
Avoid deep nesting or oversized inner classes.

---

## ⚠️ Gotcha 3 — Lambdas & Local Classes Need Effectively Final Variables
Reason: They capture **values**, not variables.  
Modifying a captured variable breaks this rule.

---

## ⚠️ Gotcha 4 — Anonymous Classes Shadow Fields
```java
int x=10;
new Runnable() {
    int x=20;
    public void run(){ System.out.println(this.x);} // prints 20
};
```

---

## ⚠️ Gotcha 5 — Choosing the Wrong Abstraction
- Use lambdas for simple behaviors  
- Use anonymous classes for multi-method overrides  
- Use static nested classes for grouping & builders  
- Use member inner classes only when instance-level logic is required  

---

# 4. How Senior Engineers Decide

### ✔ Rule 1  
If the nested class needs instance data → **member inner class**.

### ✔ Rule 2  
If not → **static nested class** (most common & recommended).

### ✔ Rule 3  
Prefer **lambdas** to anonymous classes whenever possible.

### ✔ Rule 4  
Use **local classes** sparingly.

### ✔ Rule 5  
If complexity grows → extract a top-level class.

---

# 5. Mini Quiz — Questions & Answers

## Q1. Which type risks memory leaks if stored in static structures?
**Answer:** Member inner classes (due to hidden outer reference).

---

## Q2. Best choice for fluent APIs in Java?
**Answer:** Static nested classes.

---

## Q3. Prefer anonymous class or lambda? Why?
**Answer:** Lambda — cleaner, more modern, more performant, aligns with functional design.

---

## Q4. Common restriction for lambdas & local classes?
**Answer:** Captured variables must be final or effectively final.

---

## Q5. In `SomeClass.Inner.Builder`, what type is `Builder`?
**Answer:** Static nested class (most likely).

---

# End of Chapter 4
