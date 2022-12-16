package org.example.server;

import org.example.data.Quiz;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class ConnectionHandler extends Thread {

    private DatagramSocket socket;

    public ConnectionHandler() throws SocketException {
        socket = new DatagramSocket(4445);
    }

    @Override
    public void run() {
        Quiz quiz = null;
        /*try {
        FileReader reader = new FileReader("bazaPytan.txt");
        quiz = new Quiz(1, new BufferedReader(reader));
        reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
        byte[] buf = new byte[256];
        while(true){
            try {
                DatagramPacket incomingPacket
                        = new DatagramPacket(buf, buf.length);
                socket.receive(incomingPacket);
                String received = new String(incomingPacket.getData(), 0, incomingPacket.getLength());
                System.out.println(received);
                if (received.equals("HelloC")) {
                    InetAddress ip = incomingPacket.getAddress();
                    ServerThread thread = new ServerThread(socket, ip, null, quiz);
                    thread.run();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
