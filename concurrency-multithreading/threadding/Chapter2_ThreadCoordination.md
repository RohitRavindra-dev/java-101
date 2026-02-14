# Chapter 2 – Thread Coordination & Behavior (Cheatsheet)

This cheatsheet captures *everything* discussed in Chapter 2, including all examples, explanations, analogies, quiz answers, and your detailed reasoning. It is meant to be a long-term revision reference.

---

## 1. `sleep()` — Pause Current Thread

**Purpose:** Temporarily pause the *current* thread.

```java
Thread.sleep(1000); // pauses for 1 second
```

**Key Details**
- Does **not** release locks.
- Moves thread to **TIMED_WAITING** state.
- Often used for retries, simulation, rate limiting.

---

## 2. `join()` — Wait for Another Thread

Thread A calling `t.join()` means:
- **A waits until t finishes**
- A enters **WAITING** state

Example:

```java
Thread t = new Thread(() -> {
    System.out.println("Task running...");
});

t.start();
t.join(); // main thread waits
System.out.println("Done");
```

Real-life usage:
- Waiting for worker threads to finish
- Parallel initialization tasks
- Batch jobs

---

## 3. `yield()` — Hint to Scheduler

```java
Thread.yield();
```

**Meaning:**  
“Scheduler, if someone else wants to run, let them.”

**But:**
- Scheduler may **ignore** it.
- Rarely used in modern backend systems.
- Not a reliable coordination tool.

---

## 4. Thread Scheduling Mini Example

```java
Thread t1 = new Thread(() -> System.out.println("T1"));
Thread t2 = new Thread(() -> System.out.println("T2"));

t1.start();
t1.join(); // ensures T1 runs first
t2.start();
t2.join();
```

Output:
```
T1
T2
```

---

## 5. Race Condition (Introduction)

### Basic Example

```java
class Counter {
    int count = 0;

    void increment() {
        count++;
    }
}
```

Two threads incrementing the same counter:

```java
Counter c = new Counter();

Thread t1 = new Thread(() -> {
    for (int i = 0; i < 1000; i++) c.increment();
});

Thread t2 = new Thread(() -> {
    for (int i = 0; i < 1000; i++) c.increment();
});

t1.start();
t2.start();
t1.join();
t2.join();

System.out.println(c.count); // may NOT be 2000
```

### Your explanation (included):
`count++` is not atomic. Internally it behaves like:

```
temp = count;        // read
temp = temp + 1;     // compute
count = temp;        // write
```

Interleaving example:
- Thread1 reads count = 100
- Thread2 reads count = 100
- Thread1 writes 101
- Thread2 writes 101

So even though both incremented, the final result only increased **once**.

This is the essence of a **race condition**.

Chapter 3 will fully address how to prevent this.

---

## 6. Real Backend Example — Parallel Startup

```java
Thread dbThread = new Thread(() -> loadDbConfig());
Thread redisThread = new Thread(() -> loadRedisConfig());

dbThread.start();
redisThread.start();

dbThread.join();
redisThread.join();

System.out.println("All configs loaded. Booting server...");
```

Used for:
- parallel config loading
- microservice startup
- CLI tools and batch processors

---

## 7. Thread States in Chapter 2 (Practical View)

| Method/Event        | Calling Thread State   |
|---------------------|------------------------|
| `sleep(ms)`         | TIMED_WAITING          |
| `join()`            | WAITING (or TIMED_WAITING if using join(timeout)) |
| waiting for lock    | BLOCKED                |

This will be very important in Chapter 9 (debugging).

---

## 8. Mini Quiz (with answers)

### Q1. State during `Thread.sleep(2000)`?  
**Answer:** TIMED_WAITING

### Q2. When A calls `t.join()`?  
**Answer:** A waits for t to finish

### Q3. Does `yield()` guarantee another thread runs?  
**Answer:** False

### Q4. Why final result < 2000 in shared counter example?  
**Answer:** Because `count++` is non-atomic; two threads may read the same value and overwrite each other’s increments

---

## End of Chapter 2
