# ARSW – Lab 1  
## Parallelism, Architecture and Quality from Day 1  
**Spring Boot 3.x – Java 21 – REST – Testing – OpenAPI**

---

## 🎯 Lab Objective

This lab introduces students to the **fundamentals of parallelism and concurrency**, integrating them from day one with **Software Architecture** best practices:

- Layered design  
- Well-defined REST services  
- Automated testing  
- Code coverage  
- API documentation  
- Justified technical decisions  

The lab is developed in **two phases within the same exercise**:
- **Phase 0:** base implementation (sequential)
- **Phase 1:** mandatory modification to add parallelism using threads

---

## 🧠 Context

Calculating the digits of π (Pi) is a classic problem used to illustrate **intensive computation and parallelism**.  
In this lab, this calculation is exposed as a **modern REST service**, which will be progressively evolved.

> ⚠️ Parallelism is **NOT introduced from the start**.  
> First, the solution is properly designed, then parallelized.

---

## 🏗️ Project Base Architecture

The project follows a layered architecture:

```
api/            → REST Controllers, DTOs, contracts  
core/           → Business logic and algorithms  
concurrency/    → Parallelism strategies  
monitoring/     → Time measurement and basic metrics  
```

Key rule:
- The **Controller does NOT create threads**
- The **Service orchestrates**
- The **strategies execute concurrency**

---

## 🌐 REST API

### Base Endpoint

```
GET /api/v1/pi/digits?start={int}&count={int}
```

### Extended Endpoint (Phase 1)

```
GET /api/v1/pi/digits?start=&count=&threads=&strategy=
```

Parameters:
- `start` ≥ 0  
- `count` > 0  
- `threads` > 0 (optional, default: availableProcessors)  
- `strategy` (optional): `sequential`, `threads`  

---

## 📘 OpenAPI / Swagger

Swagger should be available at:

```
http://localhost:8080/swagger-ui/index.html
```

---

## 🧪 PHASE 0 – Base Implementation (sequential)

### Objective
Build a functional, well-designed REST service covered by tests.

### Activities
1. Analyze the structure of the provided project.
2. Implement the sequential REST endpoint.
3. Validate input parameters.
4. Handle errors correctly.
5. Document the API with Swagger.
6. Implement unit and integration tests.

### Acceptance Criteria
- The endpoint responds correctly.
- Swagger is accessible.
- `mvn clean test` passes without errors.

---

## ⚙️ PHASE 1 – Mandatory Modification: add threads

### Objective
Evolve the solution to support parallelism **without breaking the architecture**.

### Mandatory Activities

#### 1. Extend the endpoint
Add support for:
- `threads`
- `strategy=threads`

If these parameters are not sent, the system should behave sequentially.

---

#### 2. Create the strategy interface

```java
public interface ParallelStrategy {
    String calculate(int start, int count, int threads);
    String name();
}
```

---

#### 3. Implement `ThreadJoinStrategy` (MANDATORY)

- Create N threads (platform threads).
- Divide the work into segments.
- Execute each segment in parallel.
- Synchronize using `join()`.
- Concatenate results in order.

The result **must be identical** to the sequential one.

---

#### 4. Modify the Service
- Delegate calculation to the strategy.
- Keep sequential calculation as fallback.

---

## 🧪 Testing and Quality (MANDATORY)

### Required Tests

#### Controller
- Valid cases (200 OK)
- Invalid cases (400 Bad Request)
- Parameter validation

#### Service
- Sequential vs parallel equivalence
- Determinism
- No deadlocks (tests with timeout)

### Coverage
- Minimum line coverage: **80%**
- `mvn clean verify` must pass

---

## 📊 Experiments and Analysis

### Activities
Measure execution times for a large `count` using:

- strategy=sequential
- strategy=threads with:
  - 1 thread
  - availableProcessors()
  - 2 × availableProcessors()
  - 200
  - 500

### Report (PDF) written with LaTeX
- Objective 
- Time table
- Results analysis
- Interpretation of Amdahl's Law
- Technical conclusions

---

## 📦 Deliverables

1. Source code  
2. Automated tests  
3. Coverage met  
4. Documented Swagger  
5. PDF report with analysis  

---

## 📝 Evaluation Rubric

| Criterion | Weight |
|--------|------|
| Phase 0 – Base implementation | 20% |
| Phase 1 – Parallelism with threads | 25% |
| Architecture and design | 20% |
| Testing and coverage | 20% |
| Analysis and conclusions | 15% |
| **Total** | **100%** |

---

## ❌ Zero Grade Causes

- The project does not compile  
- Tests fail  
- Minimum coverage is not met  
- Hardcoded results  
- Copying between teams  

---

## 🎓 Final Message

> *Parallelism is not an automatic optimization.*  
> *It is an architectural decision with costs and limits.*

Welcome to **Software Architecture (ARSW)**.