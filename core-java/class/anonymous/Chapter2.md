# Anonymous Classes Deep Dive — Chapter 2

## 🧭 Overview

This chapter focuses on how anonymous classes interact with **interfaces** and **abstract classes**, how variable capture works, and the access rules around fields and variables. These are the practical foundations that connect directly to how Lambdas behave.

---

## 🧩 Section 1: Anonymous Classes Implementing Interfaces

### Example 1 — Basic Interface Implementation

```java
interface Worker {
    void doWork();
}

public class Example1 {
    public static void main(String[] args) {
        Worker worker = new Worker() {
            public void doWork() {
                System.out.println("Working hard...");
            }
        };
        worker.doWork();
    }
}
```

✅ Defines and instantiates an **anonymous implementation** of `Worker` inline.

---

### Example 2 — Multiple Methods (Full Implementation Required)

```java
interface MultiTasker {
    void task1();
    void task2();
}

public class Example2 {
    public static void main(String[] args) {
        MultiTasker mt = new MultiTasker() {
            public void task1() { System.out.println("Doing Task 1"); }
            public void task2() { System.out.println("Doing Task 2"); }
        };
        mt.task1();
        mt.task2();
    }
}
```

⚠️ **Rule:** Must implement **all abstract methods** of the interface.

---

### Example 3 — Realistic Example (Comparator)

```java
import java.util.*;

public class Example3 {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Zoe", "Alex", "Mike");

        Collections.sort(names, new Comparator<String>() {
            public int compare(String a, String b) {
                return a.compareTo(b);
            }
        });

        System.out.println(names);
    }
}
```

✅ Common pre-lambda Java pattern.

---

## 🧩 Section 2: Anonymous Classes Extending Abstract Classes

### Example 1 — Basic Abstract Class

```java
abstract class Animal {
    abstract void makeSound();
}

public class Example4 {
    public static void main(String[] args) {
        Animal cat = new Animal() {
            void makeSound() {
                System.out.println("Meow!");
            }
        };
        cat.makeSound();
    }
}
```

✅ Instantiate and provide implementation inline.

---

### Example 2 — Abstract Class with Constructor + Field

```java
abstract class Shape {
    String color;
    Shape(String color) {
        this.color = color;
    }
    abstract double area();
}

public class Example5 {
    public static void main(String[] args) {
        Shape circle = new Shape("Red") {
            double radius = 5.0;
            double area() {
                System.out.println("My color is: " + color);
                return Math.PI * radius * radius;
            }
        };
        System.out.println(circle.area());
    }
}
```

✅ Anonymous classes can call superclass constructors but **cannot define their own constructors**.

---

## 🧩 Section 3: Variable Capture (Final & Effectively Final)

Anonymous classes can access:

* Fields of their enclosing class
* **Final or effectively final** local variables

### Example 1 — Capturing Local Variable

```java
public class Example6 {
    public static void main(String[] args) {
        int count = 10;

        Runnable r = new Runnable() {
            public void run() {
                System.out.println("Count is " + count);
            }
        };

        r.run(); // ✅ Works
    }
}
```

✅ Works because `count` is *effectively final* (not modified).

---

### Example 2 — Compile-time Error When Modified

```java
int count = 10;
Runnable r = new Runnable() {
    public void run() {
        System.out.println(count);
    }
};
count++; // ❌ ERROR: variable used in inner class must be final or effectively final
```

💡 Anonymous classes **capture a snapshot** of the variable’s value when created. Allowing mutation would lead to ambiguity and thread-safety issues.

---

### Example 3 — Fields Work Without Restriction

```java
public class Example {
    int counter = 10;

    void test() {
        Runnable r = new Runnable() {
            public void run() {
                counter++; // ✅ Works fine
                System.out.println(counter);
            }
        };
        r.run();
    }
}
```

Instance and static fields are **not restricted** by the final rule.

---

## 🧩 Section 4: Field Access Rules in Anonymous Classes

Using modifiers on fields and how they behave in an anonymous subclass.

| Modifier                      | Accessible inside anonymous subclass? | Can modify? | Notes                            |
| ----------------------------- | ------------------------------------- | ----------- | -------------------------------- |
| **public**                    | ✅ Yes                                 | ✅ Yes       | Always accessible                |
| **protected**                 | ✅ Yes                                 | ✅ Yes       | Inherited visibility             |
| **default (package-private)** | ✅ Yes (same package)                  | ✅ Yes       | Normal behavior                  |
| **private**                   | ❌ No                                  | ❌ No        | Not inherited, even in same file |
| **static**                    | ✅ Yes                                 | ✅ Yes       | Access via `Shape.color`         |

### Example — Demonstration

```java
abstract class Shape {
    protected String color = "Red";
    private String secret = "Hidden";
    static String shapeType = "2D Shape";

    abstract void draw();
}

public class Example {
    public static void main(String[] args) {
        Shape s = new Shape() {
            void draw() {
                System.out.println(color);        // ✅ allowed
                System.out.println(shapeType);    // ✅ allowed
                // System.out.println(secret);    // ❌ Error: private member
            }
        };
        s.draw();
    }
}
```

---

## ⚖️ Section 5: Practical Uses in Modern Code

Even though lambdas replaced most use cases, anonymous classes are still relevant:

* When dealing with **interfaces with multiple abstract methods**
* For **custom comparators or callbacks** in legacy code
* When needing **stateful logic** within a short-lived object

Example:

```java
button.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        System.out.println("Button clicked!");
    }
});
```

---

## 🧠 Key Takeaways

| Concept                    | Key Point                                         |
| -------------------------- | ------------------------------------------------- |
| Implementing interfaces    | Must implement **all abstract methods**           |
| Extending abstract classes | Provide missing implementations inline            |
| Variable access            | Only **final/effectively final** locals allowed   |
| Superclass constructor     | Can be called, but cannot define new constructors |
| Common uses                | Callbacks, Comparators, Event listeners           |

---

## 🧩 Mini Quiz — Chapter 2

### 1️⃣ What happens if you modify a variable from the enclosing scope used inside an anonymous class?

**Answer:** Compile-time error. Only *final or effectively final* variables can be accessed.

### 2️⃣ Anonymous class extending abstract class example

```java
Processor p = new Processor() {
    public void process() {
        System.out.println("Processing data...");
    }
};
```

### 3️⃣ Output question

```java
int x = 5;
Runnable r = new Runnable() {
    public void run() {
        System.out.println(x);
    }
};
x = 10;
r.run();
```

**Answer:** ❌ Compile-time error — `x` is no longer effectively final.

### 4️⃣ Can an anonymous class implement multiple interfaces?

**Answer:** ❌ No. Only one base type (interface or class) can be specified in `new Type() {}`.

### 5️⃣ When to prefer an anonymous class over a lambda?

**Answer:** When:

* Interface has **multiple abstract methods**
* You need **state or extra methods** inside the class
* You’re in **pre-Java 8** code
* You need `this` to refer to the anonymous instance

---

## ✅ Summary

* Anonymous classes can extend classes or implement interfaces inline.
* They cannot define constructors.
* Local variables must be *final/effectively final*.
* They can access public, protected, and static members but not private ones.
* They remain useful in callbacks, legacy code, or where Lambdas can’t be used.

---

**Next Chapter → Limitations and Transition to Lambdas**
