package org.example.server;

import org.example.data.Answer;
import org.example.data.Question;
import org.example.data.Quiz;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class ServerThread extends Thread{

    private DatagramSocket socket;
    private final PointCalculator calculator;
    private List<Answer> answers;
    private Quiz quiz;

    public ServerThread(PointCalculator calculator) throws IOException {
        this.calculator = calculator;
        answers = new ArrayList<>();
        FileReader reader = new FileReader("bazaPytan.txt");
        quiz = new Quiz(1, new BufferedReader(reader));
        socket = new DatagramSocket(4446);
    }

    @Override
    public void run() {
        byte[] buf = new byte[256];
        for (Question question : quiz.getQuestions()) {
            buf = question.toString().getBytes();
            try {
                InetAddress group = InetAddress.getByName("203.0.113.0");
                DatagramPacket packet;
                packet = new DatagramPacket(buf, buf.length, group, 4446);
                socket.send(packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void writeScores() throws IOException {
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
