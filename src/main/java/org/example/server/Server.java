package org.example.server;

import org.example.client.Student;
import org.example.data.Answer;
import org.example.data.Quiz;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Server {
    private final PointCalculator calculator;
    private List<Student> activeStudents;
    private List<Answer> answers;
    private Quiz quiz;
    private BufferedWriter writer;

    public Server(PointCalculator calculator, List<Student> activeStudents) throws IOException {
        this.calculator = calculator;
        this.activeStudents = activeStudents;
    }

    public void writeAnswers() throws IOException {
        FileWriter fileWriter = new FileWriter(new File("wyniki.txt"), true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        for(Answer answer : answers){
            bufferedWriter.write(answer.getStudentID() + " student, pytanie "
                    + answer.getQuestionID() + ": " + answer.getPoints());
        }
        bufferedWriter.close();
    }

    //czeka na podłączenie studenta
    //wysyła do wszystkich studentów pytanie
    //otrzymuje odpowiedź, zapisuje ją
}
