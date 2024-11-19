package dev.jlipka;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {
    private static final String UNIQUE_BINDING_NAME = "matrix.calculator";

    public static void main(String[] args) throws RemoteException, AlreadyBoundException, InterruptedException, NotBoundException {
        MatrixCalculatorImpl calculator = new MatrixCalculatorImpl();

        final Registry registry = LocateRegistry.createRegistry(2732);

        Remote stub = UnicastRemoteObject.exportObject(calculator, 0);
        registry.bind(UNIQUE_BINDING_NAME, stub);

        Thread.sleep(Integer.MAX_VALUE);
    }
}
