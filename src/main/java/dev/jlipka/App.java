package dev.jlipka;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class App {

    public static double[][] generateMatrix(int n) {
        SecureRandom random = new SecureRandom();
        double[][] resultMatrix = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                resultMatrix[i][j] = random.nextInt(50);
            }
        }
        return resultMatrix;
    }

    public static void main( String[] args ) throws NotBoundException, RemoteException, InterruptedException {
        double[][] matrix  = generateMatrix(1000);
        OperationExecutor executor = new OperationExecutor();

        Command<double[][]> parallelCommand = new CalculateInvertedMatrixParallelCommand(matrix, 8);
        Command<double[][]> sequentialCommand = new CalculateInvertedMatrixSequentialCommand(matrix);

        TimedCommand<double[][]> timedParallelCommand = new TimedCommand<>(parallelCommand);
        TimedCommand<double[][]> timedSequentialCommand = new TimedCommand<>(sequentialCommand);
        executor.setCommand(timedParallelCommand);
        matrix = (double[][]) executor.executeOperation();
        System.out.println(timedParallelCommand.getExecutionTime());

        executor.setCommand(timedSequentialCommand);
        matrix = (double[][]) executor.executeOperation();
        System.out.println(timedSequentialCommand.getExecutionTime());

//
//
//        PrintMatrixCommand printCommand = new PrintMatrixCommand(matrix);
//        executor.setCommand(printCommand);
//        String result = (String) executor.executeOperation();
//
//        System.out.println(result);
//        System.out.println(timedCommand.getExecutionTime());
//
//        AddCommand addCommand = new AddCommand(2,2);
//        executor.setCommand(addCommand);
//        System.out.println(executor.executeOperation());


    }
}
