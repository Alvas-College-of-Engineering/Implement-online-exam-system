package com.onlineexam.service;

import com.onlineexam.model.ExamResult;
import com.onlineexam.model.Question;
import com.onlineexam.model.User;
import com.onlineexam.repository.QuestionRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ExamService {
    private final QuestionRepository questionRepository;

    public ExamService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public List<Question> getQuestions() {
        return questionRepository.findAll();
    }

    public ExamResult evaluate(User user, Map<Integer, Integer> answers) {
        int correctAnswers = 0;
        List<Question> questions = getQuestions();
        for (Question question : questions) {
            Integer selectedOption = answers.get(question.getId());
            if (selectedOption != null && question.isCorrect(selectedOption)) {
                correctAnswers++;
            }
        }
        return new ExamResult(user, questions.size(), correctAnswers, LocalDateTime.now());
    }
}
