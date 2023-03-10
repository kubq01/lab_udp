package org.example.data;

import java.io.Serializable;

public class Question implements Serializable {
    private int ID;
    private String questionText;
    private String answerA;
    private String answerB;
    private String answerC;
    private String answerD;
    private int points;
    private short correct;

    @Override
    public String toString() {
        return String.valueOf(ID +
                ";" + questionText +
                ";" + answerA +
                ";" + answerB +
                ";" + answerC +
                ";" + answerD +
                ";" + points);
    }

    public Question(int ID, String questionText, String answerA,
                    String answerB, String answerC, String answerD, short correct) {
        this.ID = ID;
        this.questionText = questionText;
        this.answerA = answerA;
        this.answerB = answerB;
        this.answerC = answerC;
        this.answerD = answerD;
        this.correct = correct;
    }

    public Question(String data) {
        String dataArray[] = data.split(";");
        ID = Integer.parseInt(dataArray[0]);
        questionText = dataArray[1];
        answerA = dataArray[2];
        answerB = dataArray[3];
        answerC = dataArray[4];
        answerD = dataArray[5];
        points = Integer.parseInt(dataArray[6]);
    }

    public int getID() {
        return ID;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getAnswerA() {
        return answerA;
    }

    public String getAnswerB() {
        return answerB;
    }

    public String getAnswerC() {
        return answerC;
    }

    public String getAnswerD() {
        return answerD;
    }

    public int getPoints() {
        return points;
    }

    public short getCorrect() {
        return correct;
    }


}
