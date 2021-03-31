import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Random;

/**
 * 
 * @author Brian Steele
 * @author Cole Walsh
 * @author Allie Wolf
 *
 */
public class Producer implements Runnable {

	private static final int RANGE = 10;
	private static Random rand = new Random();

	// Data members
	private SharedBuffer buffer;
	private int id;
	private int[][] matrixA;
	private int[][] matrixB;
	private int[][] matrixC;
	private int splitSize;
	private int maxProducerSleeptime;
	private int producedWorkItemsCount;
	private int totalSleepTime;
	private int m; // rows in Matrix A
	private int n; // columns in Matrix A, rows in Matrix B
	private int p; // columns in Matrix B

	// Constructor
	public Producer(SharedBuffer buffer, int[][] matrixA, int[][] matrixB, int splitSize, int maxProducerSleeptime) {
		this.buffer = buffer;
		this.matrixA = matrixA;
		this.matrixB = matrixB;
		this.splitSize = splitSize;
		this.maxProducerSleeptime = maxProducerSleeptime;
		run();
	}

	@Override
	public void run() {
		
		
		//https://stackoverflow.com/questions/5463781/java-how-to-split-a-2d-array-into-two-2d-arrays
//		int[][] array = new int[][] { {1, 2, 3}, 
//										{4, 5, 6}, 
//										{7, 8 , 9}, 
//										{10, 11, 12} };
//		
//		int size = 3;								
//		int arrayLength = array.length;
//		int chunks = arrayLength/size;
//		int remainder = arrayLength % size;
//		int pointer = 0;
//		
//		for (int i = 0; i < chunks; i++) {
//			if (remainder == 0) {
//				for (int j = 0; j < size; j++) {
//					System.out.print(" " + array[pointer]);
//					pointer++;
//				}
//			} else {
//				if (i == chunks - 1) {
//					for (int j = 0; j < size + remainder; j++) {
//						System.out.print(" " + array[pointer]);
//						pointer++;
//					}
//				} else {
//					for (int j = 0; j < size; j++) {
//						System.out.print(" " + array[pointer]);
//						pointer++;
//					}
//				}
//			}
//			System.out.println("\n");
//		}
		
		
		int[][] array = new int[][] { {1, 2, 3}, 
			{4, 5, 6}, 
			{7, 8 , 9}, 
			{10, 11, 12} };
		
		
			int size = 2;								
			int arrayLength = array.length;
			int chunks = arrayLength/size;
			int remainder = arrayLength % size;
			int pointer = 0;
		
		
			for (int i = 0; i < chunks; i++) {
				if (remainder == 0) {
					for (int j = 0; j < size; j++) {
						for (int k = 0; k < array[pointer].length; k++) {
							System.out.print(" " + array[pointer][k]);
						}
						System.out.println("\n");
						pointer++;
					}
				}
			}
		
		
		
	}

	// from Allie's producer class
	private static void outputMatrix(int[][] m) {
		String output = "Result: [";
		for (int row = 0; row < m.length; row++) {
			if (row != 0) {
				output += String.format("%9s", "[");
			}
			for (int column = 0; column < m[0].length; column++) {
				output += " " + m[row][column] + " ";
			}
			output += "]\n";
		}
		System.out.println(output);
	}

	private void populateMatrix(int[][] matrix) {
		int numOfRows = matrix.length;
		int numOfColumns = matrix[0].length;
		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < numOfColumns; j++) {
				matrix[i][j] = rand.nextInt(RANGE);
			}
		}
	}

	@Override
	public String toString() {
		String output = "Producer " + this.id + "\n";
		output += "Matrix A: [";
		for (int row = 0; row < this.matrixA.length; row++) {
			if (row != 0) {
				output += String.format("%11s", "[");
			}
			for (int column = 0; column < this.matrixA[0].length; column++) {
				output += " " + this.matrixA[row][column] + " ";
			}
			output += "]\n";
		}

		output += "Matrix B: [";
		for (int row = 0; row < this.matrixB.length; row++) {
			if (row != 0) {
				output += String.format("%11s", "[");
			}
			for (int column = 0; column < this.matrixB[0].length; column++) {
				output += " " + this.matrixB[row][column] + " ";
			}
			output += "]\n";
		}
		return output;
	}

}
