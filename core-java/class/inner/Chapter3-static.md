# Chapter 3 — Static Nested Classes (Complete Cheat Sheet)

This chapter covers **everything** required to fully understand and master static nested classes:
- Concepts  
- Access rules  
- All examples  
- Full Spring Boot explanation  
- Memory & performance reasoning  
- Quiz Q&A with explanations  

---

# 1. What Is a Static Nested Class?

A **static nested class** is a class defined *inside* another class using the `static` keyword.

```java
class Outer {
    static class Helper {
        void work() {
            System.out.println("Working...");
        }
    }
}
```

Key point:
> It belongs to the **outer class**, not to an *instance* of the outer class.

It behaves almost like a top-level class but is grouped under another class for organization.

---

# 2. Access Rules

### ✔ Does NOT need an outer instance
```java
Outer.Helper h = new Outer.Helper();
```

### ✔ Can only access static members of the outer class
```java
class A {
    static int s = 5;
    int x = 10;

    static class B {
        void test() {
            System.out.println(s);    // ✔ allowed
            // System.out.println(x); // ❌ compile error
        }
    }
}
```

### ✔ Can have any visibility (public/private/protected/package)

---

# 3. Why Static Nested Classes Are Useful

### ✔ 1. Group related classes without clutter
Example:
```java
Map.Entry<K, V>
```

### ✔ 2. Perfect for builder patterns  

### ✔ 3. Great for hiding implementation details  

### ✔ 4. Allows clean “namespacing” inside outer classes  

---

# 4. Builder Pattern Example

```java
class User {
    private final String name;
    private final int age;

    private User(Builder b) {
        this.name = b.name;
        this.age = b.age;
    }

    static class Builder {
        private String name;
        private int age;

        Builder name(String name) {
            this.name = name;
            return this;
        }

        Builder age(int age) {
            this.age = age;
            return this;
        }

        User build() {
            return new User(this);
        }
    }
}
```

Usage:
```java
User u = new User.Builder()
                .name("Rohit")
                .age(26)
                .build();
```

---

# 5. How Spring Boot Uses Static Nested Classes

## ResponseEntity.BodyBuilder

```java
return ResponseEntity
        .status(200)
        .body("OK");
```

Spring achieves this using static nested types:

```java
public static interface BodyBuilder { ... }
public static class HeadersBuilder implements BodyBuilder { ... }
```

`status()` returns a `HeadersBuilder`, enabling the fluent API.

---

## HttpStatus.Series

```java
public static enum Series { INFORMATIONAL, SUCCESSFUL, REDIRECTION, ... }
```

---

## PageRequest.SortDirection

```java
Sort.Direction.ASC
Sort.Direction.DESC
```

---

# 6. Memory & Performance Advantages

## ✔ No hidden outer reference → no accidental memory leaks  
Member inner classes store a hidden field:

```
Outer this$0;
```

Static nested classes DO NOT, so they never keep outer objects alive unintentionally.

---

## ✔ Better performance
- Smaller object size  
- No capture of outer class → faster instantiation  
- Fewer pointer traversals → fewer CPU cache misses  
- Less GC pressure → cleaner backend performance  

---

## ✔ API design benefits
- Better namespacing  
- Cleaner, modular design  
- Fluent APIs become easy (ResponseEntity, builders)

---

# 7. When NOT to Use Static Nested Classes

- When you need access to instance fields (use member inner class instead)  
- When the nested class has no logical relationship with outer  
- When over-nesting makes your API confusing  

---

# 8. Compilation Name

Static nested classes compile into:
```
Outer$Nested.class
```

---

# MINI SUMMARY

- Belong to the **class**, not the instance  
- Cannot access instance members  
- Memory safe, performant  
- Heavily used in Spring Boot  
- Best choice for builder patterns  
- Clean grouping mechanism  

---

# MINI QUIZ — Questions & Answers

## Q1.
```java
class A {
    static int s = 5;
    int x = 10;

    static class B {
        void test() {
            System.out.println(s);
            // System.out.println(x);
        }
    }
}
```
**Answer:**  
Prints `5`.  
Uncommenting `x` causes compile error.

---

## Q2. Why is this allowed?

```java
ResponseEntity.status(200).body("OK");
```

**Answer:**  
Because `ResponseEntity` contains static nested types (`BodyBuilder`, `HeadersBuilder`).  
`status()` returns a nested builder that provides `body()`.

---

## Q3. Which is better for Builder pattern?

**Answer:**  
Static nested class — builders should not depend on outer instance state and static nested classes are lighter.

---

## Q4.
```java
class A {
    int x = 10;

    static class B {
        void show() { System.out.println(x); }
    }
}
```

**Answer:**  
❌ Will not compile — static nested class cannot access instance fields.

---

# End of Chapter 3
