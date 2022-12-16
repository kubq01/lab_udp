package org.example.client;

import java.io.IOException;

public class ClientMain {

    public static void main(String[] args) throws IOException {
        Student student = new Student(4445, 1);
        student.start();
    }
}
