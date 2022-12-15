package org.example.client;

import org.example.data.Answer;
import org.example.data.Question;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

public class Student extends Thread{

    private DatagramSocket socket;
    private InetAddress address;
    private final int port;
    private Question question;
    private int studentID;

    public Student(int port, int ID) throws IOException {
        socket = new DatagramSocket();
        address = InetAddress.getByName("localhost");
        this.port = port;
        studentID = ID;
    }

    public boolean getQuestion() throws IOException, ClassNotFoundException {
        byte[] data = new byte[500];
        DatagramPacket packet = new DatagramPacket(data,data.length);
        socket.receive(packet);
        byte[] reciviedData = new byte[packet.getLength()];
        String check = Arrays.toString(reciviedData).substring(0,3);

        if(!check.equals("end")) {
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(reciviedData));
            question = (Question) in.readObject();
            in.close();
        }else
        {
            System.out.println("Quiz finished. Your score is: "+Arrays.toString(reciviedData).substring(3));
            return false;
        }

        return true;
    }

    public void showQuestion() throws IOException {
        System.out.println("Question: "+question.getQuestionText());
        System.out.println("A: "+question.getAnswerA());
        System.out.println("B: "+question.getAnswerB());
        System.out.println("C: "+question.getAnswerC());
        System.out.println("D: "+question.getAnswerD());

        System.out.println("Write your answer: ");
        Scanner scanner = new Scanner(System.in);
        String ans = scanner.nextLine();
        ans = ans.toUpperCase();
        while((!ans.equals("A"))&&(!ans.equals("B"))&&(!ans.equals("C"))&&(!ans.equals("D")))
        {
            System.out.println("You cannot choose this answer. Write correct answer: ");
            ans = scanner.nextLine();
            ans = ans.toUpperCase();
        }

        Answer answer = new Answer(studentID,question.getID(),ans);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(answer);

        byte[] buf = baos.toByteArray();

        socket.send(new DatagramPacket(buf, buf.length,address,port));

    }

    @Override
    public void run()
    {
        while (true)
        {
            try {
                if (!getQuestion())
                    break;
                showQuestion();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
