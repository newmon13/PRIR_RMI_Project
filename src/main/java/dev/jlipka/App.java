package dev.jlipka;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class App {

    public static void main( String[] args ) throws NotBoundException, RemoteException, InterruptedException {
        double[][] matrix = {
                {4, 7, 2},
                {2, 6, 3},
                {1, 8, 5}
        };

        CalculateInvertedMatrixCommand calculateCommand = new CalculateInvertedMatrixCommand(matrix, 4);
        MatrixOperationExecutor executor = new MatrixOperationExecutor();
        executor.setCommand(calculateCommand);
        matrix = (double[][]) executor.executeOperation();
        PrintMatrixCommand printCommand = new PrintMatrixCommand(matrix);
        executor.setCommand(printCommand);
        String result = (String) executor.executeOperation();
        System.out.println(result);

    }
}
