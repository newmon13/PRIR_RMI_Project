package dev.jlipka;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class PrintMatrixCommand implements Command<String>{
    MatrixCalculator matrixCalculator;
    double[][] matrix;

    public PrintMatrixCommand(double[][] matrix) throws RemoteException, NotBoundException {
        this.matrix = matrix;
        Registry registry = LocateRegistry.getRegistry(2732);
        this.matrixCalculator = (MatrixCalculator) registry.lookup(UNIQUE_BINDING_NAME);
    }

    @Override
    public String execute() {
        try {
            return matrixCalculator.printMatrix(matrix);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
