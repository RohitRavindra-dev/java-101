# Chapter 1 – Java Threads 101 (Cheatsheet)

## 1. What is a Thread?
- Independent path of execution.
- Helps use multiple CPU cores.
- Main thread starts your program; additional threads run tasks in parallel.

---

## 2. Ways to Create Threads

### A. Extending Thread
```java
class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println("Running in: " + Thread.currentThread().getName());
    }
}

new MyThread().start();
```

### B. Implementing Runnable
```java
Runnable task = () -> {
    System.out.println("Hello from: " + Thread.currentThread().getName());
};
new Thread(task).start();
```

### C. Callable + Future (returns a value)
```java
ExecutorService service = Executors.newSingleThreadExecutor();

Callable<Integer> task = () -> 42;
Future<Integer> f = service.submit(task);

System.out.println(f.get());
service.shutdown();
```

---

## 3. start() vs run()
- `start()` → creates new thread.
- `run()` → normal method call, runs on the current thread.

---

## 4. Thread Lifecycle
```
NEW → RUNNABLE → RUNNING → BLOCKED/WAITING/TIMED_WAITING → TERMINATED
```

### Analogy:
- NEW → Chef hired  
- RUNNABLE → Chef ready to cook  
- BLOCKED → Kitchen locked by another chef  
- WAITING → Chef waiting for signal  
- TERMINATED → Chef done for the day  

---

## 5. Daemon Threads
- Background helpers.
- JVM exits even if they’re still running.
- Example:
```java
Thread t = new Thread(() -> {...});
t.setDaemon(true);
t.start();
```

Analogy: Cleaning staff in a restaurant — the restaurant doesn’t stay open for them.

---

## 6. Thread Naming & Priority
```java
Thread t = new Thread(task, "worker-1");
t.setPriority(Thread.MAX_PRIORITY);
```

---

## 7. Why You Cannot Restart a Thread
- A Thread object has a one-time lifecycle.
- Once TERMINATED, you cannot call start() again.
- Trying to do so:
```
java.lang.IllegalThreadStateException
```
- Native thread resources are released; Java does not recreate them via restart.

---

## 8. Correct Pattern: Use Thread Pools
```java
ExecutorService pool = Executors.newFixedThreadPool(4);
pool.submit(() -> work());
pool.shutdown();
```

---

## 9. Mini Quiz Answers

### 1. Output of t.run()? → **main**
`t.run()` is a normal method call.

### 2. Which allows returning value? → **Callable**

### 3. Daemon thread behavior? → **JVM does not wait for it on shutdown**

### 4. Why error on calling start() twice?  
Because a Thread can only transition from NEW → RUNNABLE once.

---

End of Chapter 1
