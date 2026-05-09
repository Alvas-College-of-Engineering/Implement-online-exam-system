package com.onlineexam.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Question {
    private final int id;
    private final String text;
    private final List<String> options;
    private final int correctOptionIndex;

    public Question(int id, String text, List<String> options, int correctOptionIndex) {
        this.id = id;
        this.text = text;
        this.options = new ArrayList<>(options);
        this.correctOptionIndex = correctOptionIndex;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public List<String> getOptions() {
        return Collections.unmodifiableList(options);
    }

    public int getCorrectOptionIndex() {
        return correctOptionIndex;
    }

    public boolean isCorrect(int selectedOptionIndex) {
        return selectedOptionIndex == correctOptionIndex;
    }
}
