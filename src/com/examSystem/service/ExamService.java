package com.examSystem.service;

import com.examSystem.model.Exam;
import com.examSystem.model.Question;
import java.util.HashMap;
import java.util.Map;

/**
 * Service class for Exam operations: loading exams, question banks.
 */
public class ExamService {

    // In-memory exam database (examId -> Exam)
    private Map<String, Exam> examDatabase;

    // Constructor
    public ExamService() {
        examDatabase = new HashMap<>();
        loadDefaultExams();
    }

    /**
     * Loads pre-built exams into the system.
     */
    private void loadDefaultExams() {
        loadJavaExam();
        loadMathExam();
        loadGeneralKnowledgeExam();
    }

    // ─────────────────────────────────────────────
    //  Java Programming Exam
    // ─────────────────────────────────────────────
    private void loadJavaExam() {
        Exam javaExam = new Exam("EXAM001", "Java Programming Fundamentals", "Computer Science", 30);

        javaExam.addQuestion(new Question(1,
                "Which keyword is used to create a class in Java?",
                new String[]{"create", "class", "Class", "new"},
                'B'));

        javaExam.addQuestion(new Question(2,
                "What is the size of an int in Java?",
                new String[]{"2 bytes", "4 bytes", "8 bytes", "16 bytes"},
                'B'));

        javaExam.addQuestion(new Question(3,
                "Which method is the entry point of a Java program?",
                new String[]{"start()", "run()", "main()", "init()"},
                'C'));

        javaExam.addQuestion(new Question(4,
                "What does JVM stand for?",
                new String[]{"Java Variable Machine", "Java Virtual Machine",
                        "Java Verified Module", "Java Version Manager"},
                'B'));

        javaExam.addQuestion(new Question(5,
                "Which of these is NOT a primitive data type in Java?",
                new String[]{"int", "float", "String", "boolean"},
                'C'));

        javaExam.addQuestion(new Question(6,
                "What is the default value of a boolean in Java?",
                new String[]{"true", "false", "null", "0"},
                'B'));

        javaExam.addQuestion(new Question(7,
                "Which OOP concept is achieved using interfaces?",
                new String[]{"Encapsulation", "Polymorphism", "Abstraction", "Inheritance"},
                'C'));

        javaExam.addQuestion(new Question(8,
                "What does 'this' keyword refer to in Java?",
                new String[]{"Parent class object", "Current class object",
                        "Static context", "Interface reference"},
                'B'));

        javaExam.addQuestion(new Question(9,
                "Which exception is thrown when dividing by zero in Java?",
                new String[]{"NullPointerException", "ArithmeticException",
                        "NumberFormatException", "IndexOutOfBoundsException"},
                'B'));

        javaExam.addQuestion(new Question(10,
                "What is the output of: System.out.println(10 + 20 + \"30\")?",
                new String[]{"102030", "3030", "10 + 20 + 30", "Compilation Error"},
                'B'));

        examDatabase.put(javaExam.getExamId(), javaExam);
    }

    // ─────────────────────────────────────────────
    //  Mathematics Exam
    // ─────────────────────────────────────────────
    private void loadMathExam() {
        Exam mathExam = new Exam("EXAM002", "Mathematics Basics", "Mathematics", 20);

        mathExam.addQuestion(new Question(1,
                "What is the value of π (Pi) approximately?",
                new String[]{"2.718", "3.141", "1.618", "1.732"},
                'B'));

        mathExam.addQuestion(new Question(2,
                "What is the square root of 144?",
                new String[]{"11", "12", "13", "14"},
                'B'));

        mathExam.addQuestion(new Question(3,
                "Solve: 5! (5 factorial)",
                new String[]{"100", "120", "240", "60"},
                'B'));

        mathExam.addQuestion(new Question(4,
                "What type of triangle has all sides equal?",
                new String[]{"Scalene", "Isosceles", "Equilateral", "Right-angled"},
                'C'));

        mathExam.addQuestion(new Question(5,
                "What is the derivative of x²?",
                new String[]{"x", "2x", "x²", "2"},
                'B'));

        mathExam.addQuestion(new Question(6,
                "What is log₁₀(1000)?",
                new String[]{"2", "3", "4", "10"},
                'B'));

        mathExam.addQuestion(new Question(7,
                "How many degrees are in a full circle?",
                new String[]{"180°", "270°", "360°", "90°"},
                'C'));

        mathExam.addQuestion(new Question(8,
                "What is 2^10?",
                new String[]{"512", "1024", "2048", "256"},
                'B'));

        examDatabase.put(mathExam.getExamId(), mathExam);
    }

    // ─────────────────────────────────────────────
    //  General Knowledge Exam
    // ─────────────────────────────────────────────
    private void loadGeneralKnowledgeExam() {
        Exam gkExam = new Exam("EXAM003", "General Knowledge Quiz", "General Studies", 15);

        gkExam.addQuestion(new Question(1,
                "Which planet is known as the Red Planet?",
                new String[]{"Venus", "Jupiter", "Mars", "Saturn"},
                'C'));

        gkExam.addQuestion(new Question(2,
                "Who wrote the play 'Romeo and Juliet'?",
                new String[]{"Charles Dickens", "William Shakespeare",
                        "Mark Twain", "Jane Austen"},
                'B'));

        gkExam.addQuestion(new Question(3,
                "What is the capital of Japan?",
                new String[]{"Beijing", "Seoul", "Tokyo", "Bangkok"},
                'C'));

        gkExam.addQuestion(new Question(4,
                "What is the chemical symbol for Gold?",
                new String[]{"Go", "Gd", "Au", "Ag"},
                'C'));

        gkExam.addQuestion(new Question(5,
                "Which is the largest ocean on Earth?",
                new String[]{"Atlantic Ocean", "Indian Ocean",
                        "Arctic Ocean", "Pacific Ocean"},
                'D'));

        gkExam.addQuestion(new Question(6,
                "In which year did World War II end?",
                new String[]{"1943", "1944", "1945", "1946"},
                'C'));

        examDatabase.put(gkExam.getExamId(), gkExam);
    }

    /**
     * Retrieves an exam by its ID.
     */
    public Exam getExam(String examId) {
        return examDatabase.get(examId);
    }

    /**
     * Displays available exams.
     */
    public void listAvailableExams() {
        System.out.println("\n  ┌──────────────────────────────────────────────────────────┐");
        System.out.println("  │                   AVAILABLE EXAMS                        │");
        System.out.println("  ├────────────┬──────────────────────────────┬───────┬──────┤");
        System.out.println("  │  Exam ID   │  Title                       │  Qs   │ Time │");
        System.out.println("  ├────────────┼──────────────────────────────┼───────┼──────┤");
        for (Exam exam : examDatabase.values()) {
            System.out.printf("  │  %-10s│  %-28s│  %-5d│ %3dm │%n",
                    exam.getExamId(),
                    exam.getExamTitle().length() > 26 ? exam.getExamTitle().substring(0, 26) + ".." : exam.getExamTitle(),
                    exam.getTotalQuestions(),
                    exam.getDurationMinutes());
        }
        System.out.println("  └────────────┴──────────────────────────────┴───────┴──────┘");
    }

    public Map<String, Exam> getAllExams() {
        return examDatabase;
    }
}
