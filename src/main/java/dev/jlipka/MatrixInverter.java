package dev.jlipka;

import java.rmi.RemoteException;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class MatrixInverter implements MatrixCalculator{
    private static final int PARALLEL_THRESHOLD = 100;

    @Override
    public double[][] invertParallel(double[][] matrix, int threadCount) throws  RemoteException {
        int n = matrix.length;
        double[][] copyMatrix = new double[n][];
        for (int i = 0; i < n; i++) {
            copyMatrix[i] = Arrays.copyOf(matrix[i], n);
        }
        ForkJoinPool pool = ForkJoinPool.commonPool();
        ParallelInversionTask task = new ParallelInversionTask(copyMatrix, 0, n);
        pool.invoke(task);
        return copyMatrix;
    }

    @Override
    public double[][] invertSequential(double[][] matrix) throws RemoteException {
        int n = matrix.length;

        double[][] augmentedMatrix = new double[n][2*n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(matrix[i], 0, augmentedMatrix[i], 0, n);
            augmentedMatrix[i][i + n] = 1;
        }

        for (int i = 0; i < n; i++) {
            int maxRow = i;
            for (int k = i + 1; k < n; k++) {
                if (Math.abs(augmentedMatrix[k][i]) > Math.abs(augmentedMatrix[maxRow][i])) {
                    maxRow = k;
                }
            }
            double[] temp = augmentedMatrix[maxRow];
            augmentedMatrix[maxRow] = augmentedMatrix[i];
            augmentedMatrix[i] = temp;

            for (int k = i + 1; k < n; k++) {
                double factor = augmentedMatrix[k][i] / augmentedMatrix[i][i];
                for (int j = i; j < 2 * n; j++) {
                    augmentedMatrix[k][j] -= factor * augmentedMatrix[i][j];
                }
            }
        }
        for (int i = n - 1; i >= 0; i--) {
            double divisor = augmentedMatrix[i][i];
            if (Math.abs(divisor) < 1e-10) {
                throw new IllegalArgumentException("Matrix is singular and cannot be inverted");
            }
            for (int j = i; j < 2 * n; j++) {
                augmentedMatrix[i][j] /= divisor;
            }
            for (int k = 0; k < i; k++) {
                double factor = augmentedMatrix[k][i];
                for (int j = i; j < 2 * n; j++) {
                    augmentedMatrix[k][j] -= factor * augmentedMatrix[i][j];
                }
            }
        }
        double[][] inverseMatrix = new double[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(augmentedMatrix[i], n, inverseMatrix[i], 0, n);
        }
        return inverseMatrix;
    }

    public String printMatrix(double[][] matrix) {
        StringBuilder builder = new StringBuilder();
        for (double[] row : matrix) {
            for (double val : row) {
                builder.append(String.format("%8.3f", val));
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    private static class ParallelInversionTask extends RecursiveAction {
        private final double[][] matrix;
        private final int startRow;
        private final int size;

        public ParallelInversionTask(double[][] matrix, int startRow, int size) {
            this.matrix = matrix;
            this.startRow = startRow;
            this.size = size;
        }

        @Override
        protected void compute() {
            if (size <= PARALLEL_THRESHOLD) {
                try {
                    MatrixCalculator calculator = new MatrixInverter();
                    double[][] subMatrix = Arrays.copyOfRange(matrix, startRow, startRow + size);
                    double[][] inverted = calculator.invertSequential(subMatrix);
                    for (int i = 0; i < size; i++) {
                        System.arraycopy(inverted[i], 0, matrix[startRow + i], 0, size);
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Matrix inversion failed", e);
                }
                return;
            }
            int mid = size / 2;
            ParallelInversionTask leftTask = new ParallelInversionTask(matrix, startRow, mid);
            ParallelInversionTask rightTask = new ParallelInversionTask(matrix, startRow + mid, size - mid);
            invokeAll(leftTask, rightTask);
        }
    }
}
