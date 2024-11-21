package dev.jlipka;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.Duration;
import java.time.Instant;

public class TimedCommand<T> implements Command<T> {
    private final Command<T> command;
    private Duration executionTime;

    public TimedCommand(Command<T> command) {
        this.command = command;
    }

    @Override
    public T execute() throws NotBoundException, RemoteException, InterruptedException {
        Instant start = Instant.now();
        T result = command.execute();
        Instant end = Instant.now();
        executionTime = Duration.between(start, end);
        return result;
    }

    public Duration getExecutionTime() {
        return executionTime;
    }
}
