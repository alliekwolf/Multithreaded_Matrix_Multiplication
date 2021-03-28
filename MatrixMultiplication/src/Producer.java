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
	private int m;		// rows in Matrix A
	private int n;		// columns in Matrix A, rows in Matrix B
	private int p;		// columns in Matrix B
	
	// Constructor
	public Producer(SharedBuffer buffer, int m, int n, int p) {
		this.buffer = buffer;
		this.id = 1;
		this.m = m;
		this.n = n;
		this.p = p;
		this.matrixA = new int[this.m][this.n];
		this.matrixB = new int[this.n][this.p];
	}
	
	
	// Getters for Matrix A and Matrix B (just for testing)
	public int[][] getMatrixA() {
		return this.matrixA;
	}
	public int[][] getMatrixB() {
		return this.matrixB;
	}
	
	
	@Override
	public void run() {
		
		// Populate matrices.
		populateMatrix(this.matrixA);	// Note: For testing. Will probably have to call populateMatrix() in the 
		populateMatrix(this.matrixB);	// for loop below when we get the SharedBuffer working.
		
		System.out.println(this);		// Check that matrices have populated correctly.
		
		// Put matrices in SharedBuffer
		for (int i = 0; i < this.buffer.getMaxBuffSize(); i++) {
			// this.buffer.put();		// Not correct code, but this is how we will load the SharedBuffer.
		}
		
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
