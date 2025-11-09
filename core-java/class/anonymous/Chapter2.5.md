# Anonymous Classes Deep Dive — Chapter 2.5 (Advanced Notes)

## 🧭 Overview

This chapter expands on two deep technical aspects of anonymous classes:

1. **Access modifiers and synthetic methods** — how anonymous classes can access private fields of their enclosing class.
2. **Variable capture** — why local variables (stack) behave differently from instance fields (heap) when used inside anonymous classes.

---

## 🧩 Part 1: Access Modifiers & Anonymous Classes

Consider:

```java
public class Example {
    int counter = 10;

    void test() {
        Runnable r = new Runnable() {
            public void run() {
                counter++; // works fine
                System.out.println(counter);
            }
        };
        r.run();
    }
}
```

### 💡 What Happens Internally

The compiler secretly rewrites the above code into something like:

```java
class Example$1 implements Runnable {
    final Example this$0; // reference to outer instance

    Example$1(Example enclosing) {
        this.this$0 = enclosing;
    }

    public void run() {
        this$0.counter++; // access outer field
        System.out.println(this$0.counter);
    }
}
```

This means the anonymous class actually **holds a reference to the outer instance (`Example`)** through a synthetic field called `this$0`.

---

### ✅ Access Rules for Inner/Anonymous Classes

| Modifier                    | Accessible from Anonymous Inner Class? | Reason                                     |
| --------------------------- | -------------------------------------- | ------------------------------------------ |
| `public`                    | ✅ Yes                                  | Visible everywhere                         |
| `protected`                 | ✅ Yes                                  | Inner class is part of same type hierarchy |
| `default` (package-private) | ✅ Yes                                  | Inner class is in same package             |
| `private`                   | ✅ Yes (surprising!)                    | Compiler adds *synthetic bridge methods*   |

#### Example — Private Field Access

```java
public class Example {
    private int secret = 42;

    void test() {
        Runnable r = new Runnable() {
            public void run() {
                System.out.println(secret); // ✅ Works!
            }
        };
        r.run();
    }
}
```

✅ Works perfectly.

Behind the scenes, the compiler generates a *synthetic accessor method*:

```java
/* synthetic */ static int access$000(Example e) {
    return e.secret;
}
```

The inner class calls this method instead of directly accessing the private field.

---

### 🧠 Summary

* Anonymous/inner classes have **special privileges** — they can access all members (even private) of their enclosing instance.
* The compiler ensures safety by generating **synthetic methods** under the hood.
* Access modifiers protect against *external* classes, not *inner* ones.

---

## 🧩 Part 2: Why `counter` can be modified but not `var2`

```java
public class Example {
    int counter = 10;

    void test() {
        int var2 = 10;
        Runnable r = new Runnable() {
            public void run() {
                counter++; // ✅ Works
                // var2++; // ❌ Compile-time error
                System.out.println(counter);
            }
        };
        r.run();
    }
}
```

### 🔍 Under the Hood

| Variable  | Where it lives                  | Access mechanism            | Modifiable? | Why                                   |
| --------- | ------------------------------- | --------------------------- | ----------- | ------------------------------------- |
| `counter` | Heap (part of `Example` object) | via reference to `this$0`   | ✅ Yes       | Shared reference to outer object      |
| `var2`    | Stack (method frame)            | Copied into generated class | ❌ No        | Captured *by value*, not by reference |

---

### 🧠 Stack vs Heap Analogy

* `counter` lives in the **heap** → accessible and modifiable through the reference of the enclosing object.
* `var2` lives in the **stack frame** of the `test()` method → disappears after the method finishes.

If Java allowed `var2` to be referenced directly, the anonymous class could attempt to modify a variable that no longer exists after the stack frame is gone. To prevent that, Java copies the value at creation time.

---

### 🧩 Conceptual Compiler Rewrite

```java
void test() {
    int var2 = 10;
    Runnable r = new Example$1(this, var2);
    r.run();
}

class Example$1 implements Runnable {
    final Example this$0;
    final int val$var2; // captured copy

    Example$1(Example outer, int var2) {
        this.this$0 = outer;
        this.val$var2 = var2;
    }

    public void run() {
        this$0.counter++; // modifies original object
        System.out.println(val$var2); // read-only copy
    }
}
```

Hence:

* Instance fields → accessed by reference, can change.
* Local variables → captured by value, must be *final or effectively final*.

---

### ✅ TL;DR Summary

| Concept          | `counter` (field)                   | `var2` (local variable)                     |
| ---------------- | ----------------------------------- | ------------------------------------------- |
| Memory location  | Heap                                | Stack                                       |
| Access mechanism | Shared reference                    | Copied value                                |
| Modifiable?      | ✅ Yes                               | ❌ No                                        |
| Why              | Object lives as long as outer class | Stack frame disappears after method returns |

---

## 🧩 Final Notes

* Anonymous and inner classes are **tightly coupled** to their enclosing instance.
* They can **see and modify everything** in that instance.
* Local variables are treated differently due to **lifetime and memory safety constraints**.
* Understanding this is key to understanding **lambda variable capture** — which behaves the same way.

---

**Next → Chapter 3: Limitations and Transition to Lambdas**
