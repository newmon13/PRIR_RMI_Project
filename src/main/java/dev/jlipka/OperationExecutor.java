package dev.jlipka;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class MatrixOperationExecutor {
    Command<?> command;

    void setCommand(Command<?> command) {
        this.command = command;
    }

    Object executeOperation() throws InterruptedException, NotBoundException, RemoteException {
        return this.command.execute();
    }
}
