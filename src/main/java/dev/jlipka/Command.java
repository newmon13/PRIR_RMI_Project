package dev.jlipka;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public interface Command<T> {
    String UNIQUE_BINDING_NAME = "matrix.calculator";
    T execute() throws InterruptedException, NotBoundException, RemoteException;
}
