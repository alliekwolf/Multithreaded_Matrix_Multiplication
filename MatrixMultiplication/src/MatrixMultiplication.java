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
	
	final static int RANGE = 10;
	static Scanner console = new Scanner(System.in);
	
	public static void main(String[] args) throws IOException {
		
		SharedBuffer buffer;		// shared buffer object for the producer and consumer
		int maxBuffSize = 5;		// maximum size of the shared buffer
		Producer producer;			// Producer object
		Consumer consumer;			// Consumer object
		int m = 10;				// number of rows in Matrix A
		int n = 10;				// number of columns in Matrix A, rows in Matrix B
		int p = 10;				// number of columns in Matrix B
		int splitSize = 3;		// number of sub-rows of Matrix A and sub-columns of Matrix B to be multiplied
		int[][] matrixA;		// Matrix A will be multiplied by...
		int[][] matrixB;		// ...Matrix B
		// result of the matrix multiplication
		int[][] matrixC;
		// the maximum sleep time of the producer thread
		// between putting two pairs of sub-rows of A and sub-columns of B
		// into the shared queue (must wait before next subsections)
		int maxProducerSleepTime = 20;		// sleep time for Producer object between creating sub-matrices
		int maxConsumerSleepTime = 400;		// sleep time for Consumer object between multiplying sub-matrices
		int maxThreadSleepTime = 0;			// the maximum thread sleep time
		// the current moment in simulation time
		// need to clarify, is this turn based or
		// actual system time.
		double simulationTotalTime = 0.0;
		long simulationStartTime;		// according to the instructions, this should be actual system time.
		long simulationEndTime;			// according to instructions, this should be actual system time.
		int averageThreadSleepTime;		// the average amount of time that threads slept for.
		// the number of producer and consumer threads created
		int numProducerThreads = 0;
		int numConsumerThreads = 0;
		// the number of items each producer produced and the total
		// number of all produced items in the simulation
		int producerItemsTotal = 0;
		// the number of items each consumer consumed and the total
		// number of all consumed items in the simulation
		int consumerItemsTotal = 0;
		// number of times the buffer was full...
		int fullBufferCount = 0;
		// number of times the buffer was empty...
		int emptyBufferCount = 0;
		
		boolean flag = false;		// Flag for next while loop.
		
		// While loop to ask user if they would like to load a configuration file.
		while (!flag) {
			System.out.print("Would you like to load and configuration file? Y or N: ");
			String input = console.next();
			
			switch (input) {
				case "Y":
				case "y":
					flag = true;
					
					String[] configurationArray = loadConfigurationFile();
					m = Integer.parseInt(configurationArray[0]);
					n = Integer.parseInt(configurationArray[1]);
					p = Integer.parseInt(configurationArray[2]);
					maxBuffSize = Integer.parseInt(configurationArray[3]);
					splitSize = Integer.parseInt(configurationArray[4]);
					numConsumerThreads = Integer.parseInt(configurationArray[5]);
					maxProducerSleepTime = Integer.parseInt(configurationArray[6]);
					maxConsumerSleepTime = Integer.parseInt(configurationArray[7]);
					
					break;
				case "N":
				case "n":
					numProducerThreads = 1;
					numConsumerThreads = 1;
					flag = true;	// If no file loaded, exit loop and use hard-coded data.
					break;
				default:
					System.out.println("*** Please enter 'Y' for 'yes', or 'N' for no. ***\n");
					break;
			}  // End of switch statement 
		}  // End of while loop
		System.out.println();
		
		// Populate and print matrices.
		matrixA = generateMatrix(m, n);
		System.out.println("Matrix A:");
		outputMatrix(matrixA);
		matrixB = generateMatrix(n, p);
		System.out.println("Matrix B:");
		outputMatrix(matrixB);
		
		// Get current time for calculating sequential solution.  -- CURRENTLY IN NANOSECONDS
		long sequentialStartTime = System.nanoTime();
		
		// Calculate matrix multiplication for sequential solution.
		int[][] sequentialSolution = calculateMatrixMultiplication(matrixA, matrixB);
		
		// Calculate sequentialTotalTime.  -- CURRENTLY IN NANOSECONDS
		long sequentialEndTime = System.nanoTime();
		double sequentialTotalTime = ((double)sequentialEndTime - (double)sequentialStartTime) / 1000000;		// Convert to milliseconds.
		
		System.out.println("Sequential Solution:");
		outputMatrix(sequentialSolution);
		
		
		// Start running the SharedBuffer...
		int producerId = numProducerThreads;
		int consumerId = numConsumerThreads;
		
		buffer = new SharedBuffer(maxBuffSize);
		producer = new Producer(producerId, buffer, maxProducerSleepTime, matrixA, matrixB, splitSize);
		consumer = new Consumer(consumerId, buffer, maxConsumerSleepTime);

		
		Thread t1 = new Thread(producer);
		
		
		Thread t2 = new Thread(consumer);
	
		

		
		// Get current time for calculating simulationTotalTime.
		simulationStartTime = System.nanoTime();
		
		t1.start();
		t2.start();
//		t3.start();
		
		try {
			t1.join();
			t2.join();
//			t3.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// Calculate simulationTotalTime.
		simulationEndTime = System.nanoTime();
		simulationTotalTime = ((double)simulationEndTime - (double)simulationStartTime) / 1000000;		// Convert to milliseconds.
		
		// Producer/Consumer metrics
		producerItemsTotal = producer.getProducerItemsCount();
		consumerItemsTotal = consumer.getConsumerItemsCount();
		averageThreadSleepTime = (producer.getTotalSleepTime() + consumer.getTotalSleepTime()) / (producerItemsTotal + consumerItemsTotal);
		fullBufferCount = buffer.getFullBufferCount();
		emptyBufferCount = buffer.getEmptyBufferCount();
		
		System.out.println("Sequential Solution:");
		outputMatrix(sequentialSolution);
		System.out.println();
		
		
		//output matrix sizes
		System.out.println("MATRIX DETAILS");
		System.out.println("             No. of Rows:    No. of Columns:");
		System.out.printf("Matrix A: %12s %18s%n", m, n);
		System.out.printf("Matrix B: %12s %18s%n", n, p);
		System.out.printf("Matrix C: %12s %18s%n", m, p);
		System.out.printf("%nSplit Size: %2s%n%n%n", splitSize);
		
		//output sequential metrics
		System.out.println("SEQUENTIAL SOLUTION SIMULATION RESULT");
		System.out.printf("Simulation Time:                   %12.2f ms%n", sequentialTotalTime);
		System.out.printf("                                   %12s ns)%n%n%n", "(" + (sequentialEndTime - sequentialStartTime));
		
		//output metrics
		System.out.println("PRODUCER/CONSUMER SIMULATION RESULT");
		System.out.printf("Simulation Time:                   %12.2f ms%n", simulationTotalTime);
		System.out.printf("                                   %12s ns)%n", "(" + (simulationEndTime - simulationStartTime));
		System.out.printf("Maximum Thread SleepTime:          %12s ms%n", maxThreadSleepTime);
		System.out.printf("Average Thread SleepTime:          %12s ms%n", averageThreadSleepTime);
		System.out.printf("Number of Producer Threads:        %12s%n", numProducerThreads);
		System.out.printf("Number of Consumer Threads:        %12s%n", numConsumerThreads);
		System.out.printf("Size of Buffer:                    %12s%n", maxBuffSize);
		System.out.printf("Total Number of Items Produced:    %12s%n", producerItemsTotal);
		System.out.printf("      Thread %2s:                   %12s%n", producer.getId(), producerItemsTotal);
		System.out.printf("Total Number of Items Consumed:    %12s%n", consumerItemsTotal);
		System.out.printf("      Thread %2s:                   %12s%n", consumer.getId(), consumerItemsTotal);
		System.out.printf("Number of Times Buffer was Full:   %12s%n", fullBufferCount);
		System.out.printf("Number of Times Buffer was Empty:  %12s%n", emptyBufferCount);
		System.out.println("\n\n");
		
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
				result[row][column] += Math.floor(Math.random() * RANGE);
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

}
