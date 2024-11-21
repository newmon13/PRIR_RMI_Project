package dev.jlipka;

import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.CountDownLatch;

public class MatrixCalculatorImpl implements MatrixCalculator {
    private double[][] matrix;
    private int size;
    private int threadCount;
    private double[][] augmentedMatrix;


    @Override
    public double[][] invertSequential(double[][] matrix) throws RemoteException {
        this.matrix = matrix;
        this.size = matrix.length;

        if (!isSquareMatrix(matrix)) {
            throw new IllegalArgumentException("Matrix must be square");
        }

        initializeAugmentedMatrix();

        for (int i = 0; i < size; i++) {
            performPivoting(i);
            performElimination(i, true);
        }

        for (int i = size - 1; i >= 0; i--) {
            performElimination(i, false);
        }

        return extractInvertedMatrix();
    }

    private boolean isSquareMatrix(double[][] matrix) {
        if (matrix == null) return false;
        for (double[] row : matrix) {
            if (row.length != matrix.length) return false;
        }
        return true;
    }

    private void initializeAugmentedMatrix() {
        augmentedMatrix = new double[size][2 * size];
        for (int i = 0; i < size; i++) {
            System.arraycopy(matrix[i], 0, augmentedMatrix[i], 0, size);
            augmentedMatrix[i][i + size] = 1.0;
        }
    }

    private void performPivoting(int pivotRow) {
        double pivot = augmentedMatrix[pivotRow][pivotRow];
        if (Math.abs(pivot) < 1e-10) {
            throw new ArithmeticException("Matrix is singular");
        }

        for (int j = 0; j < 2 * size; j++) {
            augmentedMatrix[pivotRow][j] /= pivot;
        }
    }

    private void performElimination(int pivotRow, boolean isForward) {
        for (int i = 0; i < size; i++) {
            if (i != pivotRow) {
                eliminateEntry(pivotRow, isForward, i);
            }
        }
    }

    private void eliminateEntry(int pivotRow, boolean isForward, int currentRow) {
        if ((isForward && currentRow > pivotRow) || (!isForward && currentRow < pivotRow)) {
            double factor = augmentedMatrix[currentRow][pivotRow];
            for (int j = 0; j < 2 * size; j++) {
                augmentedMatrix[currentRow][j] -= factor * augmentedMatrix[pivotRow][j];
            }
        }
    }

    @Override
    public double[][] invertParallel(double[][] matrix, int threadCount) throws InterruptedException, RemoteException {
        this.matrix = matrix;
        this.size = matrix.length;
        this.threadCount = threadCount;

        if (!isSquareMatrix(matrix)) {
            throw new IllegalArgumentException("Matrix must be square");
        }
        if (threadCount < 1) {
            throw new IllegalArgumentException("Thread count must be positive");
        }

        initializeAugmentedMatrix();

        for (int i = 0; i < size; i++) {
            performParallelPivoting(i);
            performParallelElimination(i, true);
        }

        for (int i = size - 1; i >= 0; i--) {
            performParallelElimination(i, false);
        }

        return extractInvertedMatrix();
    }

    private void performParallelPivoting(int pivotRow) throws InterruptedException {
        double pivot = augmentedMatrix[pivotRow][pivotRow];
        if (Math.abs(pivot) < 1e-10) {
            throw new ArithmeticException("Matrix is singular");
        }

        CountDownLatch latch = new CountDownLatch(1);
        Thread pivotThread = new Thread(() -> {
            for (int j = 0; j < 2 * size; j++) {
                augmentedMatrix[pivotRow][j] /= pivot;
            }
            latch.countDown();
        });
        pivotThread.start();
        latch.await();
    }

    private void performParallelElimination(int pivotRow, boolean isForward) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(size - 1);

        for (int i = 0; i < size; i++) {
            if (i != pivotRow) {
                final int currentRow = i;
                executor.execute(() -> {
                    eliminateEntry(pivotRow, isForward, currentRow);
                    latch.countDown();
                });
            }
        }

        latch.await();
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
    }

    private double[][] extractInvertedMatrix() {
        double[][] result = new double[size][size];
        for (int i = 0; i < size; i++) {
            System.arraycopy(augmentedMatrix[i], size, result[i], 0, size);
        }
        return result;
    }

    public String printMatrix(double[][] matrix) throws RemoteException {
        StringBuilder builder = new StringBuilder();
        for (double[] row : matrix) {
            for (double val : row) {
                builder.append(String.format("%8.4f", val));
            }
            builder.append("\n");
        }
        return builder.toString();
    }
}