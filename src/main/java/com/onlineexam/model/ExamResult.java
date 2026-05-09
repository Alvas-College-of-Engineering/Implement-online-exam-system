package com.onlineexam.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExamResult {
    private final User user;
    private final int totalQuestions;
    private final int correctAnswers;
    private final LocalDateTime submittedAt;

    public ExamResult(User user, int totalQuestions, int correctAnswers, LocalDateTime submittedAt) {
        this.user = user;
        this.totalQuestions = totalQuestions;
        this.correctAnswers = correctAnswers;
        this.submittedAt = submittedAt;
    }

    public User getUser() {
        return user;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public int getWrongAnswers() {
        return totalQuestions - correctAnswers;
    }

    public int getScorePercent() {
        if (totalQuestions == 0) {
            return 0;
        }
        return Math.round((correctAnswers * 100f) / totalQuestions);
    }

    public String getGrade() {
        int score = getScorePercent();
        if (score >= 85) {
            return "Excellent";
        }
        if (score >= 65) {
            return "Good";
        }
        if (score >= 40) {
            return "Needs Practice";
        }
        return "Retry Recommended";
    }

    public String getSubmittedAtText() {
        return submittedAt.format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"));
    }
}
