# Java Streams API — Chapter 8: Streams in Spring Boot (Real-World Use)

## 🎯 Goal
Learn practical, **real-world ways to use Streams** in a Spring Boot backend: DTO mapping, filtering results, grouping data, and combining repository calls.

---

## 💼 Example 1 — Transform Repository Results into DTOs

```java
List<User> users = userRepository.findAll();

List<UserDTO> dtos = users.stream()
    .map(u -> new UserDTO(u.getId(), u.getName(), u.getEmail()))
    .toList();
```

✅ Converts JPA entities to DTOs cleanly and immutably.

---

## 💼 Example 2 — Filter Before Returning Response

```java
List<Product> products = productRepository.findAll();

List<Product> available = products.stream()
    .filter(p -> p.getStock() > 0)
    .sorted(Comparator.comparing(Product::getPrice))
    .toList();
```

✅ Keeps response layer clean — only active, sorted products returned.

---

## 💼 Example 3 — Grouping Data (Orders by Customer)

```java
Map<Long, List<Order>> ordersByCustomer = orderRepository.findAll().stream()
    .collect(Collectors.groupingBy(Order::getCustomerId));
```

✅ Simplifies data organization in-memory without manual maps.

---

## 💼 Example 4 — Combining Multiple Repository Results

```java
List<String> activeEmails = Stream.concat(
        employeeRepo.findAll().stream()
            .filter(Employee::isActive)
            .map(Employee::getEmail),
        customerRepo.findAll().stream()
            .filter(Customer::isActive)
            .map(Customer::getEmail)
    )
    .toList();
```

✅ Combines results from two sources elegantly.

---

## 💼 Example 5 — Parallelizing Heavy CPU Tasks

```java
double avg = largeDataset.parallelStream()
    .mapToInt(Item::getValue)
    .average()
    .orElse(0);
```

✅ Effective only for large, CPU-heavy datasets.

---

## 💼 Example 6 — Returning Processed Results from Controller

```java
@GetMapping("/active-users")
public List<UserDTO> getActiveUsers() {
    return userRepository.findAll().stream()
        .filter(User::isActive)
        .map(u -> new UserDTO(u.getId(), u.getName()))
        .toList();
}
```

✅ Clear, declarative, and controller-friendly.

---

## 💼 Example 7 — Using Streams with Optional

```java
Optional<User> userOpt = userRepository.findById(id);

userOpt.map(User::getEmail)
       .ifPresent(emailService::sendWelcomeMail);

String email = userOpt.map(User::getEmail)
                      .orElse("default@example.com");
```

✅ Elegant null handling without `if` checks.

---

## 🧩 Mini Quiz (with Examples & Answers)

**Q1.** What are Streams mainly used for in Spring Boot?  
✅ Transforming and filtering repository results (e.g., entities → DTOs).

Example:
```java
userRepository.findAll().stream()
    .filter(User::isActive)
    .map(UserDTO::fromEntity)
    .toList();
```

---

**Q2.** Should filtering and grouping be done in Streams or SQL?  
✅ Usually in SQL/Hibernate for large datasets (faster).  
✅ Streams are great for **light in-memory transformations** after fetching.

---

**Q3.** How can you combine data from multiple repositories using Streams?  
✅ With `Stream.concat()` — merge, transform, and collect.

Example:
```java
Stream.concat(repoA.stream(), repoB.stream())
      .filter(Objects::nonNull)
      .toList();
```

---

✅ **End of Chapter 8 (Final Chapter)**  
🎉 You’ve officially mastered the **Java Streams API**, from fundamentals to production-grade use.
