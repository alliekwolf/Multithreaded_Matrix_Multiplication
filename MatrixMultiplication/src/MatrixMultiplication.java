import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFileChooser;

/**
 * 
 * @author Brian Steele
 * @author Cole Walsh
 * @author Allie Wolf
 *
 */
public class MatrixMultiplication {
	
	public static void main(String[] args) throws IOException {
		Scanner console = new Scanner(System.in);

		int m = 10;		// number of rows in Matrix A
		int n = 10;		// number of columns in Matrix A, rows in Matrix B
		int p = 10;		// number of columns in Matrix B
		SharedBuffer buffer;		// shared buffer object for the producer and consumer
		int maxBuffSize = 5;		// maximum size of the shared buffer
		Producer producer;			// Producer object
		Consumer consumer;			// Consumer object;
		// the parameter to split rows of A into multiple sub-rows and
		// the columns of B into multiple sub-columns
		int splitSize = 3;
		// the number of consumer threads
		int numConsumer = 2;
		// the maximum sleep time of the producer thread
		// between putting two pairs of sub-rows of A and sub-columns of B
		// into the shared queue (must wait before next subsections)
		int maxProducerSleepTime = 5;
		// sleep time for Consumer object between doing sub-matrix
		// multiplication
		int maxConsumerSleepTime = 80;
		// the first matrix to multiply
		int[][] matrixA;
		// the second matrix to multiply
		int[][] matrixB;
		// holds the result of the matrix multiplication
		int[][] matrixC;
		// the current moment in simulation time
		// need to clarify, is this turn based or
		// actual system time.
		long simulationCurrentTime;
		// according to the instructions, this should be actual system time.
		long simulationStartTime;
		// according to instructions, this should be actual system time.
		long simulationEndTime;
		// the average amount of time that threads slept for.
		int averageThreadSleepTime;
		// the number of producer threads created
		int numProducerThreads;
		// the number of consumer threads created
		int numConsumerThreads;
		// the number of items each producer produced and the total
		// number of all produced items in the simulation
		int producerItemsTotal;
		// the number of items each consumer consumed and the total
		// number of all consumed items in the simulation
		int consumerItemsTotal;
		// number of times the buffer was full...
		int fullBufferCount;
		// number of times the buffer was empty...
		int emptyBufferCount;

		System.out.println("Would you like to load and configuration file, Y or N");
		String input = console.nextLine();
		if (input.equals("Y") || input.equals("y")) {
			String[] configurationArray = loadConfigurationFile();
			m = Integer.parseInt(configurationArray[0]);
			n = Integer.parseInt(configurationArray[1]);
			p = Integer.parseInt(configurationArray[2]);
			maxBuffSize = Integer.parseInt(configurationArray[3]);
			splitSize = Integer.parseInt(configurationArray[4]);
			numConsumer = Integer.parseInt(configurationArray[5]);
			maxProducerSleepTime = Integer.parseInt(configurationArray[6]);
			maxConsumerSleepTime = Integer.parseInt(configurationArray[7]);
		}

		matrixA = generateMatrix(m, n);
		System.out.println("Matrix A:");
		outputMatrix(matrixA);
		matrixB = generateMatrix(n, p);
		System.out.println("Matrix B:");
		outputMatrix(matrixB);
		int[][] sequentialSolution = calculateMatrixMultiplication(matrixA, matrixB);
		System.out.println("Sequential Solution:");
		outputMatrix(sequentialSolution);
		
		
		// Start running the SharedBuffer...
		buffer = new SharedBuffer(maxBuffSize);
		producer = new Producer(buffer, m, n, p, splitSize);
		consumer = new Consumer(buffer);
		
		Thread t1 = new Thread(producer);
		Thread t2 = new Thread(consumer);
		t1.start();
		t2.start();
		
	}
	
	
	// methods
	public static String[] loadConfigurationFile() throws IOException {
		BufferedReader br;
		File file;
		String line;
		String keyValue[];
		String[] configurationArray = new String[8];
		
		System.out.println("Select a configuration file to run.");
		
		// open a configuration file to run.
		// https://www.codejava.net/java-se/swing/show-simple-open-file-dialog-using-jfilechooser

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File("."));
		int result = fileChooser.showOpenDialog(null);
		file = fileChooser.getSelectedFile();
		
		try {
			br = new BufferedReader(new FileReader(file));
			for (int i = 0; i < 8; i++) {
				line = br.readLine();
				keyValue = line.split(" ");
				configurationArray[i] = keyValue[2];

			}
		} catch (FileNotFoundException e) {
			System.out.println("Cannot open file.");
			e.printStackTrace();
		}
		
		return configurationArray;
	}
	
	// Code modified from this tutorial: https://www.youtube.com/watch?v=MZenB6qYqc0
	public static int[][] generateMatrix(int m, int n) {
		int[][] result = new int[m][n];
		
		for (int row = 0; row < m; row++) {
			for (int column = 0; column < n; column++) {
				result[row][column] += Math.floor(Math.random() * 10);
			}
		}
		return result;
	}
	
	// from Allie's producer class
	private static void outputMatrix(int[][] m) {
		String output = "  [";
		for (int row = 0; row < m.length; row++) {
			if (row != 0) {
				output += String.format("%3s", "[");
			}
			for (int column = 0; column < m[0].length; column++) {
				output += " " + m[row][column] + " ";
			}
			output += "]\n";
		}
		System.out.println(output);
	}

	// Again, Allie's solution from Consumer class.
	public static int[][] calculateMatrixMultiplication(int[][] mA, int[][] mB) {
		int[][] result = new int[mA.length][mB[0].length];
		
		// Multiplication logic
		for (int row = 0; row < mA.length; row++) {
			for (int column = 0; column < mB[0].length; column++) {
				for (int i = 0; i < mA[0].length; i++) {
					result[row][column] += mA[row][i] * mB[i][column];
				}
			}
		}
		return result;
	}
	
	public void incrementFullBufferCount() {
		
	}
	
	public void incrementEmptyBufferCount() {
		
	}

}
