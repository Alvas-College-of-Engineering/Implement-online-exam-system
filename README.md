
Implement-online-exam-system

## 📌 Project Overview
The Online Exam System is a Java-based application developed to allow students to log in, attend multiple-choice exams, calculate scores automatically, and view final results.

This project demonstrates Object-Oriented Programming (OOP) concepts such as classes, constructors, encapsulation, methods, and modular package structure.

---

## 🚀 Features
- 👤 Student login system
- 📝 Multiple-choice online examination
- ✅ Automatic answer evaluation
- 📊 Instant score calculation
- 🌐 Simple web-based interface using Java HTTP server
- 🧱 Modular OOP-based architecture

---

## 🛠️ Technologies Used
- Java (JDK 8 or above)
- OOP Concepts
- Java HTTP Server
- VS Code
- Git & GitHub

---

## 📂 Project Structure

onlineexamsystems/
│── src/main/java/com/onlineexam
│
├── model
│   ├── ExamResult.java
│   ├── Question.java
│   └── User.java
│
├── repository
│   ├── QuestionRepository.java
│   └── UserRepository.java
│
├── service
│   ├── AuthService.java
│   └── ExamService.java
│
├── web
│   ├── ExamServer.java
│   ├── Html.java
│   ├── RequestUtil.java
│   └── SessionManager.java
│
└── Main.java

---

## ▶️ How to Run the Project

### Step 1: Open Project Folder
Open the project in VS Code.

### Step 2: Compile Java Files
```bash
javac src/main/java/com/onlineexam/**/*.java
