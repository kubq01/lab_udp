package org.example.server;

import org.example.data.Answer;
import org.example.data.Quiz;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class ServerThread extends Thread{

    private DatagramSocket socket;

    private InetAddress userAddress;
    private final PointCalculator calculator;
    private List<Answer> answers;
    private Quiz quiz;

    public ServerThread(DatagramSocket socket, InetAddress userAddress, PointCalculator calculator, Quiz quiz) throws IOException {
        this.socket = socket;
        this.userAddress = userAddress;
        this.calculator = calculator;
        this.quiz = quiz;
        answers = new ArrayList<>();
    }

    @Override
    public void run() {
        byte[] buf = new byte[256];
        /*for (Question question : quiz.getQuestions()) {
            buf = question.toString().getBytes();
            try {
                DatagramPacket incomingPacket
                        = new DatagramPacket(buf, buf.length);
                socket.receive(incomingPacket);
                InetAddress ip = InetAddress.getByName("localhost");
                DatagramPacket packet;
                packet = new DatagramPacket(buf, buf.length, ip, 4446);
                socket.send(packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }*/
        buf = "HelloS".getBytes();
        try {
            DatagramPacket packet;
            packet = new DatagramPacket(buf, buf.length, userAddress, 4445);
            socket.send(packet);

            /*DatagramPacket incomingPacket
                    = new DatagramPacket(buf, buf.length);
            socket.receive(incomingPacket);*/

        } catch (IOException e) {
            throw new RuntimeException(e);
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
