package dev.jlipka;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MatrixCalculator extends Remote {
    double[][] invert(double[][] matrix, int threadCount) throws InterruptedException, RemoteException;
    String printMatrix(double[][] matrix) throws RemoteException;
}
