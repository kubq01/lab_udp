package org.example.server;

import org.example.data.Answer;
import org.example.data.Quiz;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class ServerThread extends Thread{

    private DatagramSocket socket;

    private InetAddress userAddress;
    private final PointCalculator calculator;
    private List<Answer> answers;
    private List<Boolean> isAnswered = new ArrayList<>();
    private Quiz quiz;
    private int userPort;
    private int port;
    private int testCounter = 0;
    private int studentPoints = 0;
    private int maxPoints = 0;

    private boolean finished = false;

    public ServerThread(InetAddress userAddress,
                        int port, PointCalculator calculator, Quiz quiz, int myPort) throws IOException {
       // this.socket = socket;
        this.socket = new DatagramSocket(myPort);
        this.userAddress = userAddress;
        this.calculator = calculator;
        this.quiz = quiz;
        answers = new ArrayList<>();
        userPort = port;
        for(int i = 0; i<quiz.getQuestions().size(); i++)
            isAnswered.add(false);
        this.port = myPort;
    }

    public int getPort() {
        return port;
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
        }
        buf = "HelloS".getBytes();
        try {
            DatagramPacket packet;
            packet = new DatagramPacket(buf, buf.length, userAddress, userPort);
            socket.send(packet);

            DatagramPacket incomingPacket
                    = new DatagramPacket(buf, buf.length);
            socket.receive(incomingPacket);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

         */
        sendQuestion();
        while(true)
        {
            if(isInterrupted())
            {
                System.out.println("Thread interrupted");
                break;
            }
            byte[] data = new byte[500];
            DatagramPacket answerPacket = new DatagramPacket(data,data.length);
            try {
                socket.receive(answerPacket);
                socket.setSoTimeout(1000);
                getAnswer(answerPacket);
            }catch (SocketTimeoutException e)
            {
                System.out.println("Timeout");
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public void getAnswer(DatagramPacket packet) throws IOException {
        //System.out.println("getAnswer1");
        String received = new String(packet.getData(), 0, packet.getLength());
        if(received.equals("stop"))
            return;
        Answer answer = new Answer(received);
        //System.out.println("getAnswer2");
        answers.add(answer);
        isAnswered.set(answer.getQuestionID(),true);
        maxPoints += 1;
        //System.out.println("getAnswer3");
        short ans = -1;
        switch (answer.getAnswer())
        {
            case "A": ans = 1; break;
            case "B": ans = 2; break;
            case "C": ans = 3; break;
            case "D": ans = 4; break;
            default:
                break;
        }

        if(ans == quiz.getQuestions().get(answer.getQuestionID()).getCorrect())
            studentPoints++;

        sendQuestion();



    }

    public void writeAnswers() throws IOException {
        FileWriter fileWriter = new FileWriter(new File("bazaOdpowiedzi.txt"), true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        for(Answer answer : answers){
            bufferedWriter.write(answer.getStudentID() + " student, pytanie "
                    + answer.getQuestionID() + ": " + answer.getPoints() + "\n");
        }
        bufferedWriter.close();
    }

    public void writeScores() throws IOException {
        FileWriter fileWriter = new FileWriter(new File("wyniki.txt"), true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write("Student " + answers.get(0).getStudentID()
                + " score: " + (studentPoints * 1.0 / maxPoints)*100 + "%\n");
        bufferedWriter.close();
    }


    public void addToCounter()
    {
        testCounter++;
        sendMessage(String.valueOf(testCounter).getBytes());

    }

    public boolean sendQuestion()
    {

        for(int i = 0; i< isAnswered.size(); i++)
        {
            if(!isAnswered.get(i))
            {
                sendMessage(quiz.getQuestions().get(i).toString().getBytes());
                return true;
            }
        }

        sendMessage(String.valueOf("end"+((studentPoints * 1.0 / maxPoints)*100)+"%").getBytes());
        try {
            finished = true;
            writeAnswers();
            writeScores();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    public void sendMessage(byte[] mess)
    {
        try {
            DatagramPacket packet;
            packet = new DatagramPacket(mess, mess.length, userAddress, userPort);
            socket.send(packet);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isFinished() {
        return finished;
    }
}
