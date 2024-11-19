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
        OperationExecutor executor = new OperationExecutor();
        executor.setCommand(calculateCommand);
        matrix = (double[][]) executor.executeOperation();
        PrintMatrixCommand printCommand = new PrintMatrixCommand(matrix);
        executor.setCommand(printCommand);
        String result = (String) executor.executeOperation();
        System.out.println(result);

        AddCommand addCommand = new AddCommand(2,2);
        executor.setCommand(addCommand);
        System.out.println(executor.executeOperation());


    }
}
