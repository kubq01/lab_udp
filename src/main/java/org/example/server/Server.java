package org.example.server;

import org.example.client.Student;

import java.util.List;

public class Server {
    private final PointCalculator calculator;
    private List<Student> activeStudents;

    public Server(PointCalculator calculator, List<Student> activeStudents) {
        this.calculator = calculator;
        this.activeStudents = activeStudents;
    }

    //czeka na podłączenie studenta
    //wysyła do wszystkich studentów pytanie
    //otrzymuje odpowiedź, zapisuje ją
}
