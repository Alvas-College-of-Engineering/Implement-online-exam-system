package com.examSystem.model;

/**
 * Represents a Multiple-Choice Question in the Online Exam System.
 */
public class Question {

    private int questionNumber;
    private String questionText;
    private String[] options;       // 4 options: A, B, C, D
    private char correctAnswer;     // 'A', 'B', 'C', or 'D'
    private int marks;

    // Constructor
    public Question(int questionNumber, String questionText, String[] options, char correctAnswer, int marks) {
        if (options.length != 4) {
            throw new IllegalArgumentException("Each question must have exactly 4 options.");
        }
        this.questionNumber = questionNumber;
        this.questionText = questionText;
        this.options = options;
        this.correctAnswer = Character.toUpperCase(correctAnswer);
        this.marks = marks;
    }

    // Overloaded constructor with default 1 mark
    public Question(int questionNumber, String questionText, String[] options, char correctAnswer) {
        this(questionNumber, questionText, options, correctAnswer, 1);
    }

    // Getters
    public int getQuestionNumber() { return questionNumber; }
    public String getQuestionText() { return questionText; }
    public String[] getOptions() { return options; }
    public char getCorrectAnswer() { return correctAnswer; }
    public int getMarks() { return marks; }

    /**
     * Validates if a student's answer is correct.
     */
    public boolean isCorrect(char studentAnswer) {
        return Character.toUpperCase(studentAnswer) == correctAnswer;
    }

    /**
     * Displays the question in a formatted way.
     */
    public void displayQuestion() {
        System.out.println("\n  Q" + questionNumber + ". " + questionText);
        System.out.println("  ┌─────────────────────────────────────────┐");
        System.out.println("  │  A) " + padRight(options[0], 36) + "│");
        System.out.println("  │  B) " + padRight(options[1], 36) + "│");
        System.out.println("  │  C) " + padRight(options[2], 36) + "│");
        System.out.println("  │  D) " + padRight(options[3], 36) + "│");
        System.out.println("  └─────────────────────────────────────────┘");
        System.out.print("  Your Answer (A/B/C/D): ");
    }

    private String padRight(String text, int length) {
        if (text.length() >= length) return text.substring(0, length);
        return text + " ".repeat(length - text.length());
    }

    @Override
    public String toString() {
        return "Q" + questionNumber + ": " + questionText;
    }
}
