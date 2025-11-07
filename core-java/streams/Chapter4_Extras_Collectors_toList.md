# Bonus: `.collect(Collectors.toList())` vs `.toList()`

## 💡 Overview

Both collect elements from a stream into a list, but there are subtle differences in behavior.

---

## ⚙️ The Two Variants

### Pre–Java 16 (Old Way)
```java
List<String> result = list.stream()
                          .filter(x -> x.length() > 3)
                          .collect(Collectors.toList());
```

### Java 16+ (New Shortcut)
```java
List<String> result = list.stream()
                          .filter(x -> x.length() > 3)
                          .toList();
```

---

## 🔍 Key Differences

| Property | `.collect(Collectors.toList())` | `.toList()` |
|-----------|----------------------------------|-------------|
| **Available since** | Java 8 | Java 16 |
| **Mutability** | Mutable (like ArrayList) | Unmodifiable (read-only) |
| **Implementation** | Uses `Collector` logic | Returns immutable list copy |
| **Can modify after creation** | ✅ Yes | ❌ Throws `UnsupportedOperationException` |
| **Return type** | `List<T>` | `List<T>` |

---

## 🧩 Example: Mutability Difference

```java
List<String> list1 = list.stream()
                         .filter(x -> x.length() > 3)
                         .collect(Collectors.toList());

list1.add("extra"); // ✅ Works fine

List<String> list2 = list.stream()
                         .filter(x -> x.length() > 3)
                         .toList();

list2.add("extra"); // ❌ Throws UnsupportedOperationException
```

---

## ⚙️ When to Use Which

| Situation | Recommended |
|------------|-------------|
| You plan to modify the list | ✅ Use `.collect(Collectors.toList())` |
| You just need a read-only result | ✅ Use `.toList()` |
| You're using Java 8–15 | Must use `.collect(Collectors.toList())` |
| You're using Java 16+ | Prefer `.toList()` for cleaner code |

---

✅ **Summary**
- `.toList()` → concise, returns **immutable** list.  
- `.collect(Collectors.toList())` → older, returns **mutable** list.  
- Functionally identical in content; differ mainly in **mutability** and **Java version**.
