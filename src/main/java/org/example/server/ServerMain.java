package org.example.server;

import java.io.IOException;

public class ServerMain {

    public static void main(String[] args) throws IOException {
        ConnectionHandler handler = new ConnectionHandler();
        handler.start();
    }

}
