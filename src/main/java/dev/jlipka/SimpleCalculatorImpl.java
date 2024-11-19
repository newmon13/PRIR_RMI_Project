package dev.jlipka;

import java.rmi.RemoteException;

public class SimpleCalculatorImpl implements SimpleCalculator<Integer>{

    @Override
    public Integer add(Integer a, Integer b) throws RemoteException {
        return a + b;
    }

    @Override
    public Integer subtract(Integer a, Integer b) throws RemoteException {
        return a - b;
    }

    @Override
    public Integer multiply(Integer a, Integer b) throws RemoteException {
        return a * b;
    }

    @Override
    public double divide(double a, double b) throws RemoteException {
        return a / b;
    }
}
