package dev.jlipka;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {
    private static final String UNIQUE_BINDING_NAME_MATRIX_CALCULATOR = "matrix.calculator";
    private static final String UNIQUE_BINDING_NAME_SIMPLE_CALCULATOR = "simple.calculator";

    public static void main(String[] args) throws RemoteException, AlreadyBoundException, InterruptedException, NotBoundException {
        MatrixCalculatorImpl matrixCalculator = new MatrixCalculatorImpl();
        SimpleCalculatorImpl simpleCalculator = new SimpleCalculatorImpl();

        final Registry registry = LocateRegistry.createRegistry(2732);

        Remote matrixCalculatorStub = UnicastRemoteObject.exportObject(matrixCalculator, 0);
        Remote simpleCalculatorStub = UnicastRemoteObject.exportObject(simpleCalculator, 0);
        registry.bind(UNIQUE_BINDING_NAME_MATRIX_CALCULATOR, matrixCalculatorStub);
        registry.bind(UNIQUE_BINDING_NAME_SIMPLE_CALCULATOR, simpleCalculatorStub);

        Thread.sleep(Integer.MAX_VALUE);
    }
}
