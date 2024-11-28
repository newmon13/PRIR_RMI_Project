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
        double[][] matrix  = generateMatrix(2000);
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
    }
}
