package org.example.server;

import org.example.data.Answer;

import java.net.DatagramSocket;
import java.util.concurrent.Callable;

public class ConnectionHandler implements Callable<Answer> {

    private DatagramSocket socket;

    public ConnectionHandler(DatagramSocket socket) {
        this.socket = socket;
    }

    @Override
    public Answer call() throws Exception {
        return null;
    }
}
