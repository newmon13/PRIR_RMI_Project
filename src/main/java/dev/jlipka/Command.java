package dev.jlipka;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public interface Command<T> {
    T execute() throws InterruptedException, NotBoundException, RemoteException;
}
