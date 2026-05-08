# 🎓 Online Examination System — Java

A **console-based Online Exam System** built with pure Java demonstrating OOP principles:
Classes, Constructors, Inheritance, Encapsulation, and Service-layer architecture.

---

## 📁 Project Structure

```
OnlineExamSystem/
├── src/
│   └── com/examSystem/
│       ├── Main.java                          # Entry point
│       ├── model/
│       │   ├── Student.java                   # Student entity
│       │   ├── Question.java                  # MCQ Question entity
│       │   ├── Exam.java                      # Exam entity
│       │   └── ExamResult.java                # Result + scoring logic
│       ├── service/
│       │   ├── StudentService.java            # Login & registration
│       │   ├── ExamService.java               # Exam/question bank
│       │   └── ScoreCalculatorService.java    # Auto score calculation
│       └── ui/
│           └── ExamController.java            # Console UI controller
├── .vscode/
│   ├── launch.json
│   └── settings.json
├── .gitignore
└── README.md
```

---

## ✨ Features

| Feature | Description |
|---|---|
| 🔐 Student Login | Authenticate with Student ID + Password |
| 📝 Registration | Register new students dynamically |
| 📋 Multiple Exams | Java, Maths, General Knowledge |
| ❓ MCQ Questions | 4-option questions (A/B/C/D) |
| 🧮 Auto Scoring | Marks calculated automatically |
| 📊 Result Card | Grade, percentage, pass/fail status |
| 🔍 Answer Review | Per-question correct vs. given answer |
| 📈 Progress Bar | Visual score progress bar |

---

## 👥 Demo Accounts

| Student ID | Password | Name |
|---|---|---|
| STU001 | alice123 | Alice Johnson |
| STU002 | bob456 | Bob Smith |
| STU003 | carol789 | Carol White |
| STU004 | david000 | David Brown |

---

## 📚 Available Exams

| Exam ID | Title | Questions | Duration |
|---|---|---|---|
| EXAM001 | Java Programming Fundamentals | 10 | 30 mins |
| EXAM002 | Mathematics Basics | 8 | 20 mins |
| EXAM003 | General Knowledge Quiz | 6 | 15 mins |

---

## 🚀 How to Run in VS Code

### Prerequisites
- **Java JDK 11 or higher** — [Download here](https://adoptium.net/)
- **VS Code** — [Download here](https://code.visualstudio.com/)
- **Extension Pack for Java** — Install from VS Code Extensions (`Ctrl+Shift+X`)

### Steps

1. **Open the project folder in VS Code:**
   ```
   File → Open Folder → Select "OnlineExamSystem"
   ```

2. **Wait for Java extension to index** (watch bottom status bar).

3. **Run the project** using any of these methods:
   - Press `F5` (uses `.vscode/launch.json`)
   - Click ▶️ **Run** button above `main()` in `Main.java`
   - Open terminal and run manually:
     ```bash
     # Compile
     javac -d bin src/com/examSystem/model/*.java src/com/examSystem/service/*.java src/com/examSystem/ui/*.java src/com/examSystem/Main.java

     # Run
     java -cp bin com.examSystem.Main
     ```

---

## ☁️ Push to GitHub

### Step 1 — Install Git
Download from: https://git-scm.com/downloads  
Verify: `git --version`

### Step 2 — Configure Git identity
```bash
git config --global user.name "Your Name"
git config --global user.email "your@email.com"
```

### Step 3 — Initialize local repo
```bash
cd OnlineExamSystem
git init
git add .
git commit -m "Initial commit: Java Online Exam System"
```

### Step 4 — Create GitHub repository
1. Go to https://github.com → **New repository**
2. Name it: `OnlineExamSystem`
3. Leave it **empty** (no README/gitignore from GitHub)
4. Click **Create repository**

### Step 5 — Push to GitHub
```bash
git remote add origin https://github.com/YOUR_USERNAME/OnlineExamSystem.git
git branch -M main
git push -u origin main
```

### Step 6 — Verify
Refresh your GitHub page — all files should be visible! 🎉

---

## 🧱 OOP Concepts Used

| Concept | Where Used |
|---|---|
| **Classes & Objects** | Student, Question, Exam, ExamResult |
| **Constructors** | All model classes with parameterized constructors |
| **Encapsulation** | Private fields with getters/setters |
| **Method Overloading** | Question constructor (with/without marks param) |
| **Service Layer** | StudentService, ExamService, ScoreCalculatorService |
| **Collections** | ArrayList, HashMap, LinkedHashMap |
| **Exception Handling** | Input validation, IllegalArgumentException |

---

## 📄 License
MIT License — Free to use and modify.
