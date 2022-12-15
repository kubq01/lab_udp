package org.example.server;

import org.example.data.Answer;
import org.example.data.Question;

public interface PointCalculator {
    public int checkAnswer(Answer answer, Question question);
}
