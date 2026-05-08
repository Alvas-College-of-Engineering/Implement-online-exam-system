package com.examSystem.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an Exam containing a set of questions.
 */
public class Exam {

    private String examId;
    private String examTitle;
    private String subject;
    private int durationMinutes;
    private List<Question> questions;
    private int totalMarks;

    // Constructor
    public Exam(String examId, String examTitle, String subject, int durationMinutes) {
        this.examId = examId;
        this.examTitle = examTitle;
        this.subject = subject;
        this.durationMinutes = durationMinutes;
        this.questions = new ArrayList<>();
        this.totalMarks = 0;
    }

    // Add a question to the exam
    public void addQuestion(Question question) {
        questions.add(question);
        totalMarks += question.getMarks();
    }

    // Getters
    public String getExamId() { return examId; }
    public String getExamTitle() { return examTitle; }
    public String getSubject() { return subject; }
    public int getDurationMinutes() { return durationMinutes; }
    public List<Question> getQuestions() { return questions; }
    public int getTotalMarks() { return totalMarks; }
    public int getTotalQuestions() { return questions.size(); }

    /**
     * Displays exam header information.
     */
    public void displayExamHeader() {
        System.out.println("\n");
        System.out.println("  ╔══════════════════════════════════════════════╗");
        System.out.println("  ║           ONLINE EXAMINATION SYSTEM          ║");
        System.out.println("  ╠══════════════════════════════════════════════╣");
        System.out.printf("  ║  Exam   : %-35s║%n", examTitle);
        System.out.printf("  ║  Subject: %-35s║%n", subject);
        System.out.printf("  ║  Total Questions: %-27s║%n", questions.size());
        System.out.printf("  ║  Total Marks    : %-27s║%n", totalMarks);
        System.out.printf("  ║  Duration       : %-23s mins ║%n", durationMinutes);
        System.out.println("  ╚══════════════════════════════════════════════╝");
    }

    @Override
    public String toString() {
        return "Exam{ID='" + examId + "', Title='" + examTitle + "', Subject='" + subject + "'}";
    }
}
