//ECEN 424 MP2
//CEDAR MAXWELL + EUGENE ASARE-MENSAH
//REFERENCES:
//https://www.w3schools.com/java/java_threads.asp

import java.util.Random;

public class MultithreadedMatrixMultiplication {

    private static final int SIZE = 20;  // Matrix size (20x20)

    // Method to create a random matrix
    public static int[][] generateMatrix(int size) {
        Random random = new Random();
        int[][] matrix = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = random.nextInt(10);  // Random values between 0 and 9
            }
        }
        return matrix;
    }

    // Method to print a matrix
    public static void printMatrix(int[][] matrix) { 
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    // Method for single-threaded matrix multiplication (for validation)
    public static int[][] singleThreadedMultiply(int[][] matrixA, int[][] matrixB) {
        int[][] result = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                result[i][j] = 0;
                for (int k = 0; k < SIZE; k++) {
                    result[i][j] += matrixA[i][k] * matrixB[k][j];
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        int[][] matrixA = generateMatrix(SIZE);
        int[][] matrixB = generateMatrix(SIZE);
        int[][] resultMultiThreaded = new int[SIZE][SIZE];
        int[][] resultSingleThreaded;

	 // Print generated matrices
	System.out.println("Matrix A:");
        printMatrix(matrixA);
	System.out.println("Matrix B:");
        printMatrix(matrixB);

        // Create 5 threads for matrix multiplication
        Thread[] threads = new Thread[5];
        int rowsPerThread = SIZE / 5;

        for (int i = 0; i < 5; i++) {
            int startRow = i * rowsPerThread;
	    int endRow;
		if (i == 4) {
			endRow = SIZE;
		} else {
			endRow = startRow + rowsPerThread;
		}
            threads[i] = new Thread(new MatrixMultiplier(matrixA, matrixB, resultMultiThreaded, startRow, endRow));
            threads[i].start();
        }

        // Wait for all threads to finish
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

	// Print the multithreaded result matrix
	System.out.println("Multithreaded Result");
        printMatrix(resultMultiThreaded);

        // Single-threaded multiplication for verification
        resultSingleThreaded = singleThreadedMultiply(matrixA, matrixB);
	System.out.println("Singlethreaded Result");
	printMatrix(resultSingleThreaded);

        // Verify the results
        boolean isCorrect = true;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (resultMultiThreaded[i][j] != resultSingleThreaded[i][j]) {
                    isCorrect = false;
                    break;
                }
            }
        }
	
        if (isCorrect) {
            System.out.println("Multithreaded multiplication is correct!");
        } else {
            System.out.println("There is a mismatch in the results.");
        }
    }
}

