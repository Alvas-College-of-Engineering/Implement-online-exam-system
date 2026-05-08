package com.examSystem.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Stores and displays the result of an exam attempt by a student.
 */
public class ExamResult {

    private Student student;
    private Exam exam;
    private Map<Integer, Character> studentAnswers;   // questionNumber -> studentAnswer
    private int marksObtained;
    private LocalDateTime submittedAt;

    // Constructor
    public ExamResult(Student student, Exam exam) {
        this.student = student;
        this.exam = exam;
        this.studentAnswers = new LinkedHashMap<>();
        this.marksObtained = 0;
        this.submittedAt = LocalDateTime.now();
    }

    // Record an answer
    public void recordAnswer(int questionNumber, char answer) {
        studentAnswers.put(questionNumber, answer);
    }

    // Calculate marks automatically
    public void calculateMarks() {
        marksObtained = 0;
        for (Question question : exam.getQuestions()) {
            char studentAnswer = studentAnswers.getOrDefault(question.getQuestionNumber(), ' ');
            if (question.isCorrect(studentAnswer)) {
                marksObtained += question.getMarks();
            }
        }
    }

    // Getters
    public int getMarksObtained() { return marksObtained; }
    public Student getStudent() { return student; }
    public Exam getExam() { return exam; }

    public double getPercentage() {
        if (exam.getTotalMarks() == 0) return 0;
        return ((double) marksObtained / exam.getTotalMarks()) * 100;
    }

    public String getGrade() {
        double pct = getPercentage();
        if (pct >= 90) return "A+  🏆";
        else if (pct >= 80) return "A   🥇";
        else if (pct >= 70) return "B   🥈";
        else if (pct >= 60) return "C   🥉";
        else if (pct >= 50) return "D   📘";
        else return "F   ❌";
    }

    public String getStatus() {
        return getPercentage() >= 50 ? "PASSED ✅" : "FAILED ❌";
    }

    /**
     * Displays the detailed result report.
     */
    public void displayResult() {
        String divider = "  ════════════════════════════════════════════════";
        String formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss").format(submittedAt);

        System.out.println("\n\n" + divider);
        System.out.println("           📋  EXAMINATION RESULT CARD");
        System.out.println(divider);
        System.out.printf("  Student Name   : %s%n", student.getName());
        System.out.printf("  Student ID     : %s%n", student.getStudentId());
        System.out.printf("  Exam           : %s%n", exam.getExamTitle());
        System.out.printf("  Subject        : %s%n", exam.getSubject());
        System.out.printf("  Submitted At   : %s%n", formatter);
        System.out.println(divider);
        System.out.printf("  Total Questions: %d%n", exam.getTotalQuestions());
        System.out.printf("  Marks Obtained : %d / %d%n", marksObtained, exam.getTotalMarks());
        System.out.printf("  Percentage     : %.2f%%%n", getPercentage());
        System.out.printf("  Grade          : %s%n", getGrade());
        System.out.printf("  Result         : %s%n", getStatus());
        System.out.println(divider);

        // Detailed answer review
        System.out.println("\n  📝  DETAILED ANSWER REVIEW:");
        System.out.println("  ─────────────────────────────────────────────────");
        System.out.printf("  %-4s %-40s %-8s %-8s %-6s%n", "No.", "Question", "Yours", "Correct", "Marks");
        System.out.println("  ─────────────────────────────────────────────────");

        for (Question q : exam.getQuestions()) {
            char studentAns = studentAnswers.getOrDefault(q.getQuestionNumber(), '-');
            boolean correct = q.isCorrect(studentAns);
            String status = correct ? "✅ +" + q.getMarks() : "❌  0";
            String shortQ = q.getQuestionText().length() > 38
                    ? q.getQuestionText().substring(0, 35) + "..."
                    : q.getQuestionText();
            System.out.printf("  %-4d %-40s %-8s %-8s %-6s%n",
                    q.getQuestionNumber(), shortQ, studentAns, q.getCorrectAnswer(), status);
        }
        System.out.println("  ─────────────────────────────────────────────────");
        System.out.printf("  %55s TOTAL: %d/%d%n", "", marksObtained, exam.getTotalMarks());
        System.out.println(divider);
        System.out.println();
    }
}
