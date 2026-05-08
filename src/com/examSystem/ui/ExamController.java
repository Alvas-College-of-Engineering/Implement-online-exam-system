package com.examSystem.ui;

import com.examSystem.model.*;
import com.examSystem.service.*;

import java.util.List;
import java.util.Scanner;

/**
 * Main controller class for the Online Exam System.
 * Handles all user interactions through console interface.
 */
public class ExamController {

    private StudentService studentService;
    private ExamService examService;
    private ScoreCalculatorService scoreCalculator;
    private Scanner scanner;

    // Currently logged-in student
    private Student currentStudent;

    // Constructor
    public ExamController() {
        studentService = new StudentService();
        examService = new ExamService();
        scoreCalculator = new ScoreCalculatorService();
        scanner = new Scanner(System.in);
    }

    /**
     * Entry point - launches the system.
     */
    public void launch() {
        displayWelcomeBanner();

        boolean running = true;
        while (running) {
            displayMainMenu();
            int choice = getIntInput("  Enter your choice: ");

            switch (choice) {
                case 1 -> loginAndStartExam();
                case 2 -> registerNewStudent();
                case 3 -> { System.out.println("\n  👋  Thank you for using the Online Exam System. Goodbye!\n"); running = false; }
                default -> System.out.println("\n  ⚠️  Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    // ─────────────────────────────────────────────
    //  Welcome Banner
    // ─────────────────────────────────────────────
    private void displayWelcomeBanner() {
        System.out.println("\n");
        System.out.println("  ╔═══════════════════════════════════════════════════════╗");
        System.out.println("  ║                                                       ║");
        System.out.println("  ║      🎓  ONLINE EXAMINATION SYSTEM  🎓               ║");
        System.out.println("  ║           Java-Based Student Portal                   ║");
        System.out.println("  ║                                                       ║");
        System.out.println("  ╚═══════════════════════════════════════════════════════╝");
        System.out.println("  Demo Accounts:  STU001/alice123 | STU002/bob456 | STU003/carol789");
    }

    // ─────────────────────────────────────────────
    //  Main Menu
    // ─────────────────────────────────────────────
    private void displayMainMenu() {
        System.out.println("\n  ┌─────────────────────────────┐");
        System.out.println("  │         MAIN MENU           │");
        System.out.println("  ├─────────────────────────────┤");
        System.out.println("  │  1. Login & Start Exam      │");
        System.out.println("  │  2. Register New Student    │");
        System.out.println("  │  3. Exit                    │");
        System.out.println("  └─────────────────────────────┘");
    }

    // ─────────────────────────────────────────────
    //  Login Flow
    // ─────────────────────────────────────────────
    private void loginAndStartExam() {
        System.out.println("\n  ── STUDENT LOGIN ──────────────────────────");
        System.out.print("  Student ID : ");
        String id = scanner.nextLine().trim();
        System.out.print("  Password   : ");
        String pwd = scanner.nextLine().trim();

        currentStudent = studentService.authenticate(id, pwd);

        if (currentStudent == null) {
            System.out.println("\n  ❌  Login failed! Invalid Student ID or Password.");
            return;
        }

        System.out.println("\n  ✅  Login successful! Welcome, " + currentStudent.getName() + "!");
        studentDashboard();
    }

    // ─────────────────────────────────────────────
    //  Student Dashboard
    // ─────────────────────────────────────────────
    private void studentDashboard() {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n  ┌─────────────────────────────────────┐");
            System.out.printf("  │  👤  %s%-31s│%n", currentStudent.getName(),
                    " [" + currentStudent.getStudentId() + "]");
            System.out.println("  ├─────────────────────────────────────┤");
            System.out.println("  │  1. Start an Exam                   │");
            System.out.println("  │  2. View Available Exams            │");
            System.out.println("  │  3. Logout                          │");
            System.out.println("  └─────────────────────────────────────┘");

            int choice = getIntInput("  Enter choice: ");
            switch (choice) {
                case 1 -> startExamFlow();
                case 2 -> examService.listAvailableExams();
                case 3 -> { System.out.println("\n  Logged out successfully."); loggedIn = false; }
                default -> System.out.println("  ⚠️  Invalid choice.");
            }
        }
    }

    // ─────────────────────────────────────────────
    //  Start Exam Flow
    // ─────────────────────────────────────────────
    private void startExamFlow() {
        examService.listAvailableExams();
        System.out.print("\n  Enter Exam ID to attempt (e.g., EXAM001): ");
        String examId = scanner.nextLine().trim().toUpperCase();

        Exam exam = examService.getExam(examId);
        if (exam == null) {
            System.out.println("  ❌  Exam not found! Please check the Exam ID.");
            return;
        }

        exam.displayExamHeader();

        System.out.print("\n  ⚠️  Press ENTER to begin the exam (once started, complete it)...");
        scanner.nextLine();

        // Conduct exam
        char[] studentAnswers = conductExam(exam);

        // Calculate score
        ExamResult result = scoreCalculator.calculateResult(currentStudent, exam, studentAnswers);

        // Display result
        result.displayResult();
        scoreCalculator.displayPerformanceBar(result);

        System.out.print("\n  Press ENTER to return to dashboard...");
        scanner.nextLine();
    }

    // ─────────────────────────────────────────────
    //  Conduct Exam - Collect Answers
    // ─────────────────────────────────────────────
    private char[] conductExam(Exam exam) {
        List<Question> questions = exam.getQuestions();
        char[] answers = new char[questions.size()];

        System.out.println("\n\n  ── EXAM IN PROGRESS ──────────────────────────────────");
        System.out.println("  Answer each question by entering A, B, C, or D.");
        System.out.println("  ─────────────────────────────────────────────────────");

        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            q.displayQuestion();

            char answer = getValidAnswerInput();
            answers[i] = answer;

            System.out.println("  ✔  Answer recorded: " + answer);
        }

        System.out.println("\n  ── All questions answered! Submitting exam... ──────────");
        return answers;
    }

    /**
     * Validates answer input - only accepts A, B, C, or D.
     */
    private char getValidAnswerInput() {
        while (true) {
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.length() == 1 && "ABCD".contains(input)) {
                return input.charAt(0);
            }
            System.out.print("  ⚠️  Invalid input! Enter A, B, C, or D: ");
        }
    }

    // ─────────────────────────────────────────────
    //  Register New Student
    // ─────────────────────────────────────────────
    private void registerNewStudent() {
        System.out.println("\n  ── STUDENT REGISTRATION ───────────────────────────");
        System.out.print("  Enter Student ID   : ");
        String id = scanner.nextLine().trim();
        System.out.print("  Enter Full Name    : ");
        String name = scanner.nextLine().trim();
        System.out.print("  Set Password       : ");
        String password = scanner.nextLine().trim();

        if (id.isEmpty() || name.isEmpty() || password.isEmpty()) {
            System.out.println("  ❌  All fields are required!");
            return;
        }

        Student newStudent = new Student(id, name, password);
        boolean success = studentService.registerStudent(newStudent);

        if (success) {
            System.out.println("  ✅  Registration successful! You can now login with ID: " + id);
        } else {
            System.out.println("  ❌  Student ID already exists. Please choose a different ID.");
        }
    }

    // ─────────────────────────────────────────────
    //  Utility: Safe integer input
    // ─────────────────────────────────────────────
    private int getIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int val = Integer.parseInt(scanner.nextLine().trim());
                return val;
            } catch (NumberFormatException e) {
                System.out.println("  ⚠️  Please enter a valid number.");
            }
        }
    }
}
