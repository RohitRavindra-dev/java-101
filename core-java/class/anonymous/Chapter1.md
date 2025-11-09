# Anonymous Classes Deep Dive — Chapter 1

## 🧭 Overview

Anonymous classes are a quick way to define and instantiate a class **without giving it a name**. They’re mostly used when you need to provide a one-off implementation — typically for interfaces, abstract classes, or callbacks.

They were heavily used before Java 8 (especially in event listeners and threading) and remain a core concept to understand before mastering **Lambdas**.

---

## 🧠 What Are Anonymous Classes?

> “Creating a one-time-use subclass or interface implementation right where you need it.”

They’re often used when:

* You need a **custom implementation** of an interface/class but only once.
* Creating a new `.java` file for it would be overkill.

---

## 🧩 Example 1 — Regular class vs Anonymous class

### Without Anonymous Class

```java
interface Greeter {
    void greet();
}

class FriendlyGreeter implements Greeter {
    public void greet() {
        System.out.println("Hello there!");
    }
}

public class Example1 {
    public static void main(String[] args) {
        Greeter g = new FriendlyGreeter();
        g.greet();
    }
}
```

### With Anonymous Class

```java
interface Greeter {
    void greet();
}

public class Example2 {
    public static void main(String[] args) {
        Greeter g = new Greeter() {
            public void greet() {
                System.out.println("Hello there!");
            }
        };
        g.greet();
    }
}
```

✅ Here, we define and instantiate an **anonymous implementation of `Greeter`** inline.

---

## 🧩 Example 2 — Anonymous Class Extending a Class

```java
class Animal {
    void speak() {
        System.out.println("Some sound...");
    }
}

public class Example3 {
    public static void main(String[] args) {
        Animal dog = new Animal() {
            void speak() {
                System.out.println("Woof! Woof!");
            }
        };
        dog.speak();
    }
}
```

✅ This creates a **subclass of `Animal`** dynamically and overrides `speak()`.

---

## 💼 Example 3 — Common Real-World Use (Pre-Lambda Java)

```java
public class Example4 {
    public static void main(String[] args) {
        Thread t = new Thread(new Runnable() {
            public void run() {
                System.out.println("Running in a thread...");
            }
        });
        t.start();
    }
}
```

Equivalent Lambda (for later reference):

```java
new Thread(() -> System.out.println("Running in a thread...")).start();
```

---

## ⚙️ Syntax Pattern (Generic Form)

```java
Type ref = new Type(constructorArgs...) {
    // class body — methods, variables, overrides
};
```

* `Type` can be an **interface**, **abstract class**, or **concrete class**.
* Defines and instantiates simultaneously.

---

## ⚖️ Comparison Table

| Feature                              | Normal Class          | Anonymous Class         |
| ------------------------------------ | --------------------- | ----------------------- |
| Has a name                           | ✅ Yes                 | ❌ No                    |
| Can be reused                        | ✅ Yes                 | ❌ No                    |
| File structure                       | Separate `.java` file | Inline inside code      |
| Typical use                          | Reusable logic        | One-off custom behavior |
| Can extend class/implement interface | ✅                     | ✅ (only one)            |

---

## 🧠 Key Takeaways

* Anonymous classes are **syntactic sugar** for quick implementations.
* They can **extend a class** or **implement an interface**, but **not both**.
* Commonly used for **callbacks**, **short-lived logic**, or **thread tasks**.
* They can **access final/effectively-final** variables from the enclosing scope.

---

## 🧩 Mini Quiz — Chapter 1

### 1️⃣ Identify the output

```java
interface Hello {
    void sayHello();
}

public class Test {
    public static void main(String[] args) {
        Hello h = new Hello() {
            public void sayHello() {
                System.out.println("Hey there!");
            }
        };
        h.sayHello();
    }
}
```

**Output:**

```
Hey there!
```

✅ Correct — `sayHello()` overridden and executed successfully.

---

### 2️⃣ Abstract Class Instantiation

```java
abstract class Shape {
    abstract void draw();
}

Shape s = new Shape(); // ❌ ERROR
s.draw();
```

❌ Cannot instantiate an abstract class.
✅ Correct form using anonymous class:

```java
Shape s = new Shape() {
    void draw() {
        System.out.println("Drawing...");
    }
};
s.draw();
```

---

### 3️⃣ Runnable One-Liner Example

```java
Runnable myRunnable = new Runnable() {
    public void run() {
        System.out.println("Task done!");
    }
};
```

To execute:

```java
new Thread(myRunnable).start();
```

✅ Correct syntax for inline interface implementation.

---

### 4️⃣ Can an Anonymous Class Have a Constructor?

❌ No. Because constructors must have the same name as the class, and anonymous classes have no name.

However, ✅ they can **call a superclass constructor** during instantiation:

```java
class Person {
    Person(String name) {
        System.out.println("Hi, " + name);
    }
}

Person p = new Person("Rohit") {
    // anonymous subclass body
};
```

---

## ✅ Summary

* Anonymous classes are **inline implementations** for interfaces or abstract/concrete classes.
* Used for short, one-time tasks or callbacks.
* Cannot define constructors.
* Often used before lambdas became mainstream.
* Foundation for understanding **functional programming** in Java.

---

**Next Chapter → Anonymous Classes with Interfaces and Abstract Classes**
