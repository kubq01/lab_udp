package org.example.data;

import lombok.Getter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Quiz implements Serializable {

    private int QuizId;
    private List<Question> questions;

    public Quiz(int quizId, BufferedReader reader) throws IOException {
        QuizId = quizId;
        questions = readQuestions(reader);
    }

    public void addQuestion(Question question){
        questions.add(question);
    }

    private List<Question> readQuestions(BufferedReader reader) throws IOException {
        List<Question> readQuestions = new ArrayList<>();
        int qId = 0;
        String q;
        String[] answers = {"", "" , "", ""};
        short correct;
        while((q = reader.readLine()) != null) {
            for (int i = 0 ; i < 4; i++){
                answers[i] = reader.readLine();
                if (answers[i] == null){
                    throw new IOException("Question is not correctly written in txt file.");
                }
            }
            correct = Short.parseShort(reader.readLine());
            readQuestions.add(new Question(qId, q, answers[0], answers[1], answers[2], answers[3], correct));
        }
        reader.close();
        return readQuestions;
    }



}
