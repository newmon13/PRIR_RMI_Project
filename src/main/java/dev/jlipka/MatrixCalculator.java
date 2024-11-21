package dev.jlipka;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MatrixCalculator extends Remote {
    double[][] invertParallel(double[][] matrix, int threadCount) throws InterruptedException, RemoteException;
    double[][] invertSequential(double[][] matrix) throws InterruptedException, RemoteException;
    String printMatrix(double[][] matrix) throws RemoteException;
}
