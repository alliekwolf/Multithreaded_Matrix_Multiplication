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
	private int m;
	private int n;
	private int p;
	
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
	
	@Override
	public void run() {
		
		// Populate matrices.
		populateMatrix(this.matrixA);
		populateMatrix(this.matrixB);
		
		System.out.println(this);		// Check that matrices have populated correctly.
		
		// Put matrices in SharedBuffer
		for (int i = 0; i < this.buffer.getMaxBuffSize(); i++) {
			
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
		String s = "Producer " + this.id + "\n";
		s += "Matrix A: [";
		for (int i = 0; i < this.matrixA.length; i++) {
			if (i != 0) { 
				s += String.format("%11s", "[");
			}
			for (int j = 0; j < this.matrixA[0].length; j++) {
				s += " " + this.matrixA[i][j] + " ";
			}
			s += "]\n";
		}
		
		s += "Matrix B: [";
		for (int i = 0; i < this.matrixB.length; i++) {
			if (i != 0) { 
				s += String.format("%11s", "[");
			}
			for (int j = 0; j < this.matrixB[0].length; j++) {
				s += " " + this.matrixB[i][j] + " ";
			}
			s += "]\n";
		}
		return s;
	}
	
}
