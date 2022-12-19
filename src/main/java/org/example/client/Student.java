package org.example.client;

import org.example.data.Answer;
import org.example.data.Question;

import java.io.*;
import java.net.*;
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

        //handling server not responding
        socket.setSoTimeout(5000);
        try {
            socket.receive(packet);
        }catch(SocketTimeoutException e)
        {
            getQuestion();
        }


        /*
        byte[] reciviedData = new byte[packet.getLength()];
        String check = Arrays.toString(reciviedData).substring(0,3);

        for(int i = 0; i<packet.getLength();i++)
            reciviedData[i] = data[i];

         */

        String received = new String(packet.getData(), 0, packet.getLength());
        String check = received.substring(0,3);

        if((!check.equals("end"))&&(!check.equals("max"))) {
            /*
            System.out.println("getQuestionErr");
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(reciviedData));
            System.out.println("getQuestionErr2");
            question = (Question) in.readObject();
            in.close();
            System.out.println("getQuestionErr3");

             */
           // System.out.println(received);
            question = new Question(received);

        }else if(check.equals("end"))
        {
            System.out.println("Quiz finished. Your score is: "+received.substring(3));
            return false;
        }else {
            System.out.println("Too many user are using the service now. Try agaoin later");
            return false;
        }


        return true;
    }

    public void showQuestion() throws IOException {

        Timerthread timer = new Timerthread(10000);

        System.out.println("Question: "+question.getQuestionText());
        System.out.println("A: "+question.getAnswerA());
        System.out.println("B: "+question.getAnswerB());
        System.out.println("C: "+question.getAnswerC());
        System.out.println("D: "+question.getAnswerD());

        System.out.println("Write your answer: ");

        timer.start();

        Scanner scanner = new Scanner(System.in);
        String ans = scanner.nextLine();
        ans = ans.toUpperCase();
        while((!ans.equals("A"))&&(!ans.equals("B"))&&(!ans.equals("C"))&&(!ans.equals("D")))
        {
            System.out.println("You cannot choose this answer. Write correct answer: ");
            ans = scanner.nextLine();
            ans = ans.toUpperCase();
        }

        if(timer.isTimeEnded())
        {
            System.out.println("Time's up! You wont receive points for this question");
            ans = "Q";
        }

        Answer answer = new Answer(studentID,question.getID(),ans);

        /*
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(answer);

         */

        byte[] buf = answer.toString().getBytes();



        socket.send(new DatagramPacket(buf, buf.length,address,port));

    }

    private class Timerthread extends Thread{


        private int timeout;
        private boolean timeEnded = false;
        public Timerthread(int timeout) {
            this.timeout = timeout;
        }
        @Override
        public void run(){
            try {
                sleep(timeout);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }finally {
                timeEnded = true;
            }
        }

        public boolean isTimeEnded() {
            return timeEnded;
        }
    }



    @Override
    public void run()
    {
        /*
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

         */
        String msg = "HelloC";
        byte[] buf = msg.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 4445);
        try {
            socket.send(packet);
            /*
            packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
            String received = new String(packet.getData(), 0, packet.getLength());
            System.out.println(received);
            while(true)
            {
                Scanner scanner = new Scanner(System.in);
                System.out.println("press any key and ENTER");
                scanner.nextLine();
                DatagramPacket p2 = new DatagramPacket(buf, buf.length, address, 4445);
                socket.send(p2);
                p2 = new DatagramPacket(buf, buf.length);
                socket.receive(p2);
                String receivedMess = new String(p2.getData(), 0, p2.getLength());
                System.out.println(receivedMess);
            }

             */while(getQuestion())
                 showQuestion();

            String msgStop = "stop";
            byte[] bufStop = msgStop.getBytes();
            DatagramPacket stopThread = new DatagramPacket(bufStop,bufStop.length,address,4445);
            socket.send(stopThread);

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


}
