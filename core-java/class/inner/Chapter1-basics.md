# Chapter 1 — Inner Classes Overview (Complete Cheat Sheet)

## 1. What Inner Classes Are
Inner classes = classes defined **inside another class**.

They exist to:
- group related logic  
- hide implementation details  
- avoid polluting the package with too many small helper files  
- create cleaner APIs  
- support patterns like Builder

Java supports **4 types**:
1. Member Inner Class  
2. Static Nested Class  
3. Local Class  
4. Anonymous Class  

---

## 2. The Four Types (with examples)

### 2.1 Member Inner Class
```java
class Outer {
    private int num = 10;

    class Inner {
        void print() {
            System.out.println("num = " + num);
        }
    }
}
```

---

### 2.2 Static Nested Class
```java
class Outer {
    static class Helper {
        void assist() {
            System.out.println("Assisting...");
        }
    }
}
```

---

### 2.3 Local Class (Method-Local)
```java
void compute() {
    class Validator {
        boolean valid(int n) { return n > 0; }
    }

    Validator v = new Validator();
    System.out.println(v.valid(5));
}
```

---

### 2.4 Anonymous Class
```java
Runnable r = new Runnable() {
    @Override public void run() {
        System.out.println("Running...");
    }
};
```

---

## 3. Why Inner Classes Exist
- Group related logic  
- Encapsulation  
- Cleaner APIs  
- Reduce package clutter  

---

## 4. Compilation Behavior
Example:
```java
class A {
    class B {}
    static class C {}
}
```

Produces:
```
A.class
A$B.class
A$C.class
```

---

## 5. Where Spring Boot Uses Inner Classes
- ResponseEntity.BodyBuilder (static nested)
- HttpStatus.Series (static nested)
- PageRequest.SortDirection (static nested)
- Anonymous classes in event listeners

---

## 6. Mini Quiz (Questions & Answers)

### Q1. What `.class` files will this generate?
```java
class A {
    class B {}
    static class C {}
}
```
**Answer:**  
A.class, A$B.class, A$C.class

---

### Q2. True/False:  
A static nested class requires an instance of the outer class.

**Answer:** False

---

### Q3. Identify the type:
```java
void test() {
    class Helper {}
}
```
**Answer:** Local (method-local) class

---

### Q4. What type is ResponseEntity.BodyBuilder?

**Answer:** Static nested class

---

# End of Chapter 1
