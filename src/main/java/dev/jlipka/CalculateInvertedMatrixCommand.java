package dev.jlipka;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CalculateInvertedMatrixCommand implements Command<double[][]>{
    String UNIQUE_BINDING_NAME = "matrix.calculator";
    private final MatrixCalculator matrixCalculator;
    private final double[][] matrix;
    private final int threads;

    public CalculateInvertedMatrixCommand(double[][] matrix, int threads) throws RemoteException, NotBoundException {
        this.matrix = matrix;
        this.threads = threads;
        Registry registry = LocateRegistry.getRegistry(2732);
        this.matrixCalculator = (MatrixCalculator) registry.lookup(UNIQUE_BINDING_NAME);
    }

    @Override
    public double[][] execute() throws InterruptedException, RemoteException {
        return matrixCalculator.invert(matrix, threads);
    }
}
