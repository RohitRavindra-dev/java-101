# Java Streams API — Chapter 4: Collectors Deep Dive

## 🎯 Goal
Master `Collectors` — the real power behind `collect()`, allowing you to build complex aggregations, groupings, and analytics.

---

## 💡 1. `toList()` / `toSet()`

Gather stream elements into collections.

```java
List<Integer> list = Stream.of(1, 2, 3).collect(Collectors.toList());
Set<Integer> set = Stream.of(1, 2, 2).collect(Collectors.toSet());
```

---

## 💡 2. `joining()`

Join elements into a single string.

```java
String csv = Stream.of("A", "B", "C")
    .collect(Collectors.joining(", ", "[", "]"));
System.out.println(csv); // [A, B, C]
```

---

## 💡 3. `groupingBy()`

Group elements by a classifier (like SQL `GROUP BY`).

```java
List<String> names = List.of("Alice", "Bob", "Alex", "Brian", "Charlie");

Map<Character, List<String>> grouped = names.stream()
    .collect(Collectors.groupingBy(name -> name.charAt(0)));

System.out.println(grouped);
// {A=[Alice, Alex], B=[Bob, Brian], C=[Charlie]}
```

---

## 💡 4. `partitioningBy()`

Partition into true/false groups.

```java
List<Integer> nums = List.of(1, 2, 3, 4, 5, 6);

Map<Boolean, List<Integer>> partitioned = nums.stream()
    .collect(Collectors.partitioningBy(n -> n % 2 == 0));

System.out.println(partitioned);
// {false=[1, 3, 5], true=[2, 4, 6]}
```

---

## 💡 5. Combining Collectors (Downstream Collectors)

```java
List<String> words = List.of("apple", "bat", "ball", "cat", "carrot");

Map<Character, Long> wordCount = words.stream()
    .collect(Collectors.groupingBy(
        w -> w.charAt(0),
        Collectors.counting()
    ));

System.out.println(wordCount); // {a=1, b=2, c=2}
```

---

# 🧩 Real-World Example — Employee Data Processing

```java
record Employee(String name, String dept, int salary) {}

List<Employee> employees = List.of(
    new Employee("Alice", "IT", 70000),
    new Employee("Bob", "HR", 50000),
    new Employee("Charlie", "IT", 90000),
    new Employee("David", "Finance", 60000),
    new Employee("Eva", "Finance", 75000)
);
```

---

### **1️⃣ Filter + Map + Collect**
```java
List<String> itEmployees = employees.stream()
    .filter(e -> e.dept().equals("IT"))
    .map(e -> e.name().toUpperCase())
    .toList();
System.out.println(itEmployees); // [ALICE, CHARLIE]
```

---

### **2️⃣ Group by Department**
```java
Map<String, List<Employee>> byDept = employees.stream()
    .collect(Collectors.groupingBy(Employee::dept));
```

---

### **3️⃣ Average Salary per Department**
```java
Map<String, Double> avgSalary = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::dept,
        Collectors.averagingInt(Employee::salary)
    ));
System.out.println(avgSalary);
// {Finance=67500.0, HR=50000.0, IT=80000.0}
```

---

### **4️⃣ Highest Paid Employee per Department**
```java
Map<String, Optional<Employee>> topEarner = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::dept,
        Collectors.maxBy(Comparator.comparingInt(Employee::salary))
    ));
```

---

### **5️⃣ Joined Names per Department**
```java
Map<String, String> namesByDept = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::dept,
        Collectors.mapping(Employee::name, Collectors.joining(", "))
    ));

System.out.println(namesByDept);
// {Finance=David, Eva, HR=Bob, IT=Alice, Charlie}
```

---

✅ **End of Chapter 4**
Next: **Primitive Streams & Optional** — `IntStream`, `mapToInt`, and handling null-like scenarios with `Optional`.
