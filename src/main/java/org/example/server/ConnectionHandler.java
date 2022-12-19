package org.example.server;

import org.example.data.Question;
import org.example.data.Quiz;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

public class ConnectionHandler extends Thread {

    private DatagramSocket socket;
    private Map<InetAddress,ServerThread> activeUsers = new HashMap<>();
    private Quiz quiz;

    public ConnectionHandler() throws SocketException {
        socket = new DatagramSocket(4445);
    }

    public Question getQuestion(int ID)
    {
        if(quiz.getQuestions().size()>ID)
        {
            return quiz.getQuestions().get(ID);
        }else
        {
            throw new IllegalStateException("This Question doesnt exist");
        }
    }

    @Override
    public void run() {

        try {
        FileReader reader = new FileReader("bazaPytan.txt");
        quiz = new Quiz(1, new BufferedReader(reader));
        //reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        byte[] buf = new byte[256];
        while(true){
            try {
                DatagramPacket incomingPacket
                        = new DatagramPacket(buf, buf.length);
                socket.receive(incomingPacket);
                String received = new String(incomingPacket.getData(), 0, incomingPacket.getLength());
                System.out.println(received);
                if( (received.equals("HelloC")) &&(!activeUsers.containsKey(incomingPacket.getAddress()))
                && activeUsers.size() <= 250) {
                    InetAddress ip = incomingPacket.getAddress();
                    int port = incomingPacket.getPort();
                    ServerThread thread = new ServerThread(socket, ip, port, null, quiz);
                    System.out.println("New user");
                    activeUsers.put(ip,thread);
                    thread.start();
                }else if(activeUsers.containsKey(incomingPacket.getAddress()))
                {
                    InetAddress address = incomingPacket.getAddress();
                    ServerThread currentlyServed = activeUsers.get(address);
                currentlyServed.getAnswer(incomingPacket);
                if(currentlyServed.isFinished()){
                    activeUsers.remove(address);
                }
                }else if(activeUsers.size() > 250) {
                    System.out.println("Error Server at maximum capacity");
                }
                else{
                    System.out.println("Error Non existing user");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
