package com.examSystem.model;

/**
 * Represents a Student in the Online Exam System.
 */
public class Student {

    private String studentId;
    private String name;
    private String password;
    private int score;
    private int totalAttempted;

    // Constructor
    public Student(String studentId, String name, String password) {
        this.studentId = studentId;
        this.name = name;
        this.password = password;
        this.score = 0;
        this.totalAttempted = 0;
    }

    // Getters and Setters
    public String getStudentId() { return studentId; }
    public String getName() { return name; }
    public String getPassword() { return password; }
    public int getScore() { return score; }
    public int getTotalAttempted() { return totalAttempted; }

    public void setScore(int score) { this.score = score; }
    public void setTotalAttempted(int totalAttempted) { this.totalAttempted = totalAttempted; }

    public void incrementScore() { this.score++; }
    public void incrementAttempted() { this.totalAttempted++; }

    /**
     * Returns percentage score.
     */
    public double getPercentage(int totalQuestions) {
        if (totalQuestions == 0) return 0;
        return ((double) score / totalQuestions) * 100;
    }

    /**
     * Returns grade based on percentage.
     */
    public String getGrade(int totalQuestions) {
        double percentage = getPercentage(totalQuestions);
        if (percentage >= 90) return "A+";
        else if (percentage >= 80) return "A";
        else if (percentage >= 70) return "B";
        else if (percentage >= 60) return "C";
        else if (percentage >= 50) return "D";
        else return "F";
    }

    @Override
    public String toString() {
        return "Student{ID='" + studentId + "', Name='" + name + "'}";
    }
}
