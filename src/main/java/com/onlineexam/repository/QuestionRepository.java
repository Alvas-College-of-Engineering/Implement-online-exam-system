package com.onlineexam.repository;

import com.onlineexam.model.Question;
import java.util.ArrayList;
import java.util.List;

public class QuestionRepository {
    private final List<Question> questions = new ArrayList<>();

    public QuestionRepository() {
        questions.add(new Question(1, "Which keyword is used to inherit a class in Java?",
                List.of("this", "extends", "implements", "import"), 1));
        questions.add(new Question(2, "Which method starts a Java application?",
                List.of("run()", "main()", "start()", "execute()"), 1));
        questions.add(new Question(3, "Which collection does not allow duplicate elements?",
                List.of("List", "Queue", "Set", "ArrayList"), 2));
        questions.add(new Question(4, "What is the default value of a boolean field?",
                List.of("true", "false", "0", "null"), 1));
        questions.add(new Question(5, "Which concept keeps data and methods together?",
                List.of("Inheritance", "Encapsulation", "Polymorphism", "Abstraction"), 1));
        questions.add(new Question(6, "Which exception is checked by the compiler?",
                List.of("IOException", "NullPointerException", "ArithmeticException", "ArrayIndexOutOfBoundsException"), 0));
    }

    public List<Question> findAll() {
        return List.copyOf(questions);
    }
}
