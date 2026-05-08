package com.examSystem.service;

import com.examSystem.model.Exam;
import com.examSystem.model.ExamResult;
import com.examSystem.model.Question;
import com.examSystem.model.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for automatic score calculation and result generation.
 */
public class ScoreCalculatorService {

    // History of all exam results in this session
    private List<ExamResult> resultHistory;

    // Constructor
    public ScoreCalculatorService() {
        resultHistory = new ArrayList<>();
    }

    /**
     * Calculates and returns an ExamResult for a given student and their answers.
     *
     * @param student      The student who attempted the exam
     * @param exam         The exam attempted
     * @param studentAnswers Array of answers given by student (indexed by question position)
     * @return ExamResult object with all marks calculated
     */
    public ExamResult calculateResult(Student student, Exam exam, char[] studentAnswers) {
        ExamResult result = new ExamResult(student, exam);

        List<Question> questions = exam.getQuestions();

        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            char answer = (i < studentAnswers.length) ? studentAnswers[i] : ' ';
            result.recordAnswer(question.getQuestionNumber(), answer);
        }

        // Automatically compute marks
        result.calculateMarks();

        // Update student score
        student.setScore(result.getMarksObtained());
        student.setTotalAttempted(exam.getTotalQuestions());

        // Store in history
        resultHistory.add(result);

        return result;
    }

    /**
     * Returns the performance summary string.
     */
    public String getPerformanceSummary(ExamResult result) {
        double pct = result.getPercentage();
        if (pct >= 90) return "🌟 Outstanding! Excellent performance!";
        else if (pct >= 80) return "👏 Great job! Very well done!";
        else if (pct >= 70) return "😊 Good performance! Keep it up!";
        else if (pct >= 60) return "📚 Average. A bit more effort needed.";
        else if (pct >= 50) return "⚠️  Just passed. Needs improvement.";
        else return "❌ Failed. Please study harder and retry.";
    }

    /**
     * Displays performance bar chart.
     */
    public void displayPerformanceBar(ExamResult result) {
        double pct = result.getPercentage();
        int filled = (int) (pct / 5);  // 20 units = 100%
        int empty = 20 - filled;

        System.out.println("\n  Score Progress:");
        System.out.print("  0% [");
        System.out.print("█".repeat(filled));
        System.out.print("░".repeat(empty));
        System.out.printf("] 100%%  (%.1f%%)%n", pct);
        System.out.println("  " + getPerformanceSummary(result));
    }

    public List<ExamResult> getResultHistory() {
        return resultHistory;
    }
}
