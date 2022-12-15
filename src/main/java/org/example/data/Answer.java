package org.example.data;

import java.io.*;

public class Answer implements Serializable {
    private int studentID;
    private int questionID;
    private String answer;

    private int points;

    public Answer(int studentID, int questionID, String answer) {
        this.studentID = studentID;
        this.questionID = questionID;
        this.answer = answer;
    }

    public Answer(String data) {
        String dataArray[] = data.split(";");
        studentID = Integer.parseInt(dataArray[0]);
        questionID = Integer.parseInt(dataArray[1]);
        answer = dataArray[2];
    }

    public int getStudentID() {
        return studentID;
    }

    public int getQuestionID() {
        return questionID;
    }

    public String getAnswer() {
        return answer;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return String.valueOf(
                studentID +
                ";" + questionID +
                ";" + answer);
    }

    public synchronized void saveToFile() throws IOException {
        FileWriter fileWriter = new FileWriter(new File("bazaOdpowiedzi.txt"), true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(toString());
        bufferedWriter.close();
    }

}
