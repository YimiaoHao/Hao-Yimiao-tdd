# Hao-Yimiao-tdd 
## 1. Introduction

This repository strictly follows **Test-Driven Development (TDD)**: write a failing test (**red**) → implement the minimal code to pass (**green**) → **refactor**, iteratively. The commit history demonstrates “**tests before production code**”, and the use of AI is disclosed in accordance with the coursework requirements. 

## 2. Repo Layout
src/
  main/java/
    Book.java
    User.java
    Reservation.java
    ReservationService.java
    IBookRepository.java
    IReservationRepository.java
    IWaitlistRepository.java
    MemoryBookRepository.java
    MemoryReservationRepository.java
    MemoryWaitlistRepository.java
    Calculator.java
  test/java/
    CalculatorTest.java
    ReservationServiceTest.java
    ReservationServicePriorityTest.java
    UserTest.java
lib/
  junit-platform-console-standalone-6.0.0-M2.jar

## 3. How to Run

* **VS Code**: Open the project and run all tests from the **Test Explorer**. 
* **Maven**:
  `mvn -q -DskipTests=false test` 
* **Gradle**:
  `./gradlew test` 
* **JUnit Console** (stand-alone JAR placed under `lib/`):

  java -jar lib/junit-platform-console-standalone-*.jar \
    -cp "src/main/java:src/test/java" \
    --scan-classpath


## 4. What’s Implemented

### 4.1 Part A — Calculator

* Basic four arithmetic operations.
* Division by zero throws **IllegalArgumentException**. 

### 4.2 Part B — Core Library

#### 4.2.1 Domain

`Book (id, title, copiesAvailable)`, `User (id, name)`, `Reservation (userId, bookId)`. 

#### 4.2.2 Service Rules (ReservationService)

1. A reservation is allowed only when `copiesAvailable > 0`.
2. On a successful reservation, inventory **decrements by 1**.
3. The same user **cannot** reserve the same book twice.
4. When a reservation is cancelled, inventory **increments by 1**.
5. If no copies are available, an error is thrown (implemented as **IllegalStateException** or an equivalent exception).
6. Users can view **their own** list of active reservations. 

### 4.3 Part C — Priority & Waitlist

#### 4.3.1 Priority Queueing

When no copies are available, **priority users** are placed on a **FIFO** waitlist. When someone cancels, the book is automatically assigned to the **head of the queue**; the inventory remains **0** (it does not increase and then decrease again). 

#### 4.3.2 New Components

`IWaitlistRepository`, `WaitlistEntry`, `MemoryWaitlistRepository` (in-memory FIFO). 

#### 4.3.3 Constructor Compatibility

The original **two-argument** constructor is retained (behaviour unchanged when no waitlist is used), and a new **three-argument** constructor enables the waitlist. 

## 5. AI Usage Disclosure

ChatGPT was used to organise test-case naming, draft the README text, and clarify behavioural boundaries for **Part C**. When code errors occurred, the Chinese AI tool **“Doubao”** was used to interpret error messages and suggest fixes. Some code fragments were AI-assisted; such commits are explicitly marked **“(AI used)”** in the history. 

## 6. Notes

During **Part C**, an incorrect approach was initially taken, so some Part C code was uploaded **twice**. The following **three commits/files** are **incorrect** and can be ignored:

* `test: priority waits on zero copies and auto-allocates on cancel (AI used)`
* `feat: priority waitlist and auto-allocation on cancel (AI used)`
* `test: priority waitlist is FIFO (It is not a failed test; it can be run directly without further code modifications or resubmission.)`

All commits that contain the **correct** Part C implementation are prefixed with **“[Correct Part C]”**. To review Part C, please refer directly to the commits/files with this prefix. 
