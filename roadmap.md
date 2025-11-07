🧠 1. Core Language Deep Dive

These make your Java sharp. Top tech companies love engineers who know these inside out.

| Added | Topic | Why it Matters |
|:---:|-------|---------------|
| [x] | Generics (advanced) — wildcards, bounded types, type erasure | Used heavily in enterprise libraries & frameworks |
| [] | Inner Classes, Anonymous Classes, Lambdas | Needed to understand framework internals & functional programming |
| [] | Functional Interfaces, Streams API | Modern idiomatic Java, used in data transformations |
| [] | Records and Sealed Classes (Java 17+) | Used in modern domain modeling (immutability + pattern matching) |
| [x] | Enums with behavior | Often used for states, command types, error codes |

---

⚙️ 2. Concurrency and Multithreading (High Priority for Fintechs, Cloud PBCs etc)

This is non-negotiable in financial systems — parallel transactions, async calls, risk computations, etc.

| Added | Topic | Why it Matters |
|:---:|-------|---------------|
| [ ] | Thread lifecycle, Runnable/Callable/Future | Core concurrency primitives |
| [ ] | Executors, ThreadPools | Used in backend services for task execution |
| [ ] | CompletableFuture | Foundation for async APIs (used in Dropwizard, Spring Boot, etc.) |
| [ ] | Locks, Semaphores, Latches, Barriers | Control concurrent operations safely |
| [ ] | Concurrent Collections (CopyOnWrite, ConcurrentHashMap, etc.) | Safe data sharing in multithreaded apps |
| [ ] | ForkJoinPool & Parallel Streams | Efficient parallel computation |
| [ ] | Java Memory Model & volatile keyword | Understanding visibility and atomicity issues |
| [ ] | Reactive Programming (Project Reactor, RxJava, CompletableFuture chains) | Used in modern microservices for async I/O |

---

🧠 3. JVM Internals & Performance

Knowing how the JVM works separates a developer from an engineer.

| Added | Topic | Why it Matters |
|:---:|-------|---------------|
| [ ] | JVM architecture (heap, stack, metaspace) | Helps with debugging performance issues |
| [ ] | Garbage Collection (GC tuning, G1, ZGC, etc.) | Crucial for low-latency systems like fintech |
| [ ] | Classloading & Reflection | Frameworks like Spring use it heavily |
| [ ] | JIT Compilation & HotSpot optimizations | Explains runtime performance behavior |
| [ ] | Memory leaks, profiling tools (VisualVM, JConsole, Flight Recorder) | Detect issues in production apps |

---

🌐 4. Enterprise Development & Frameworks

These are what you'll actually use day-to-day.

| Added | Topic | Why it Matters |
|:---:|-------|---------------|
| [ ] | Spring & Spring Boot (Core, Data, Web, Security) | Most popular enterprise stack |
| [ ] | Dropwizard / Micronaut / Quarkus | Lightweight alternatives for microservices |
| [ ] | Dependency Injection & Inversion of Control | The foundation of all enterprise frameworks |
| [ ] | RESTful APIs (JAX-RS, Spring MVC) | Building and consuming APIs |
| [ ] | Persistence (JPA, Hibernate) | ORM layer, mapping DB tables to Java objects |
| [ ] | Transaction Management | Critical in financial systems to ensure consistency |
| [ ] | Caching (EhCache, Redis, Caffeine) | Performance optimization for reads |
| [ ] | Validation (Jakarta Bean Validation) | Clean input validation logic |

---

💾 5. Data & I/O

Used everywhere — from log processing to API input handling.

| Added | Topic | Why it Matters |
|:---:|-------|---------------|
| [ ] | Streams vs Readers/Writers | Understanding I/O layers |
| [ ] | NIO & NIO.2 | Scalable I/O used in servers |
| [ ] | Serialization / Deserialization (Jackson, Gson) | For JSON/XML data transfer |
| [ ] | File Watchers, Channels, Buffers | Used for large file processing |
| [ ] | Working with Databases (JDBC, connection pooling) | Still foundational in enterprise apps |

---

🔒 6. Security

Fintech-grade apps care about data integrity and confidentiality.

| Added | Topic | Why it Matters |
|:---:|-------|---------------|
| [ ] | Java Security APIs (MessageDigest, Signature, Cipher) | Encryption, signing, hashing |
| [ ] | KeyStores & Certificates | Used for SSL/TLS and authentication |
| [ ] | JWT & OAuth2 (Spring Security) | Common authentication/authorization method |
| [ ] | Secure coding practices (avoiding injections, leaks) | Security compliance requirement |

---

🧩 7. Design Patterns & Best Practices

Every enterprise engineer is expected to know these.

| Added | Category | Examples |
|:---:|----------|----------|
| [ ] | Creational | Singleton, Factory, Builder |
| [ ] | Structural | Adapter, Decorator, Proxy |
| [ ] | Behavioral | Strategy, Observer, Command |
| [ ] | Concurrency Patterns | Producer-Consumer, Thread Pool |
| [ ] | Enterprise Patterns | Repository, Service Layer, DTO, CQRS |

---

🧱 8. Testing, Logging, and Tooling

For building reliable and maintainable systems.

| Added | Topic | Why it Matters |
|:---:|-------|---------------|
| [ ] | JUnit 5, Mockito | Unit testing |
| [ ] | Integration Testing (Testcontainers, WireMock) | Test APIs and DBs |
| [ ] | Logging (SLF4J, Logback, Log4j2) | Production monitoring |
| [ ] | Code Quality (SonarQube, PMD, Checkstyle) | Required in enterprise CI/CD pipelines |

---

☁️ 9. Modern Architectures & Integration

Enterprise-grade systems are distributed and cloud-native.

| Added | Topic | Why it Matters |
|:---:|-------|---------------|
| [ ] | Microservices design (12-Factor App) | Fintech systems are microservice-heavy |
| [ ] | Event-driven systems (Kafka, RabbitMQ) | For real-time updates, transactions, alerts |
| [ ] | REST vs gRPC vs GraphQL | Service communication |
| [ ] | Docker, Kubernetes | Deployment and scaling |
| [ ] | Cloud SDKs (AWS, GCP, Azure) | Integration with managed services |

---

🧠 10. Advanced Topics

These make you stand out in senior roles.

| Added | Topic | Why it Matters |
|:---:|-------|---------------|
| [ ] | Reflection & Annotations | Understanding how frameworks like Spring work internally |
| [ ] | Custom Classloaders & Plugins | Advanced extensibility |
| [ ] | Dynamic Proxies | Used in AOP, transaction management |
| [ ] | Annotation Processing (APT) | Build-time code generation |
| [ ] | Modularization (Java Modules) | Large app structuring |
| [ ] | Native Images (GraalVM) | Performance optimization frontier |

