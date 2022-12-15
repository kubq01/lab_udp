package org.example.data;

import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
public class Quiz implements Serializable {

    private int QuizId;
    private List<Question> questions;

    public Quiz(int quizId, List<Question> questions) {
        QuizId = quizId;
        this.questions = questions;
    }

    public void addQuestion(Question question){
        questions.add(question);
    }

}
