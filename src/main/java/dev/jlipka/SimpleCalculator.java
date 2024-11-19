package dev.jlipka;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SimpleCalculator<T> extends Remote {
    T add(T a, T b) throws RemoteException;
    T subtract(T a, T b) throws RemoteException;
    T multiply(T a, T b) throws RemoteException;
    double divide(double a, double b) throws RemoteException;
}
