import java.util.ArrayList;
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
	private int m;		// rows in Matrix A
	private int n;		// columns in Matrix A, rows in Matrix B
	private int p;		// columns in Matrix B
	private int splitSize;
	private ArrayList<WorkItem> results;
	private boolean stop;
	
	// Constructor
	public Producer(SharedBuffer buffer, int[][] matrixA, int[][] matrixB, int m, int n, int p, int splitSize) {
		this.buffer = buffer;
		this.id = 1;
		this.m = m;
		this.n = n;
		this.p = p;
		this.matrixA = matrixA;
		this.matrixB = matrixB;
		this.matrixC = new int[this.m][this.p];
		this.splitSize = splitSize;
		this.results = new ArrayList<WorkItem>();
		this.stop = false;
	}
	
	
	@Override
	public void run() {
		
		System.out.println(this);		// Check that matrices have populated correctly.
		
		// QUESTION:  WHERE DO WE FLIP THE 'STOP' BIT TO TRUE?
		
		while (!this.stop) {
			
			// Split matrices into sub-matrices, starting with the first sub-matrix (subA) created from matrixA.
			for (int i = 0; i <= (this.m / this.splitSize); i++) {
				
				int remainderA = (this.m % this.splitSize);		// remainder rows in matrixA
				int row = (i * this.splitSize);					// row index of matrixA = i * split size
				int littleM;									// number of rows that will be in subA
				
				if ((this.m - row) > remainderA) {			// If there are more rows left than remainderA...
					littleM = this.splitSize;				// ...number of rows in subA = split size
				} else {									// Else, if we have reached the remainderA...
					littleM = remainderA;					// ...number of rows = remainderA
				}
				
				int[][] subA = new int[littleM][this.n];		// Declare size of subA.
				populateSubMatrix(subA, this.matrixA, row, 0);	// Populate subA.
				
				// Next, create a second sub-matrix (subB) from matrixB. 
				for (int j = 0; j <= (this.p / this.splitSize); j++) {
					
					int remainderB = (this.p % this.splitSize);		// remainder columns in matrixB
					int column = (j * this.splitSize);				// column index of matrixB = j * split size
					int littleP;									// number of columns that will be in subB
					
					if ((this.n - column) > remainderB) {		// If there are more columns left than remainderB...
						littleP = this.splitSize;				// ...number of columns in subB = split size
					} else {									// Else, if we have reached the remainderB...
						littleP = remainderB;					// ...number of columns = remainderB
					}
					
					int[][] subB = new int[this.n][littleP];			// Declare size of subB.
					populateSubMatrix(subB, this.matrixB, 0, column);	// Populate subB.
					
					// Set the WorkItem's column and row information (low's and high's)
					int highA = row + (littleM - 1);
					int highB = column + (littleP - 1);
					
					// Create new WorkItem object from sub-matrices, then put in results ArrayList and SharedBuffer.
					WorkItem workItem = new WorkItem(subA, subB, row, highA, column, highB);
					this.results.add(workItem);
					this.buffer.put(workItem);
					
					System.out.println(workItem.subAToString());
					System.out.println(workItem.subBToString());
					
					int count = 0;		// This is a counter to track all 'DONE' workItems in the next for loop.
					
					for (WorkItem item : results) {		// Loop through results ArrayList to find all 'DONE' workItems
						if (item.isReady()) {				// If a workItem is 'READY' for calculating...
							populateMatrixC(item);			// ...add it's subC matrix to Matrix C
							item.setState(State.DONE);		// ...and set the workItem's state to 'DONE'.
//							item.setDone();
							count++;						// ...increment the count of 'DONE' workItems.
						} else if (item.isDone()) {			// Else, if workItem is 'DONE'...
							count++;						// ...just increment the count of 'DONE' workItems.
						}
					}
					
					// If we've created all necessary workItems for these matrices...
					if (highA == (subA.length - 1) && highB == (subB[0].length - 1)) {
						this.buffer.setState(State.DONE);		// ...set SharedBuffer state to 'DONE'.
//						this.buffer.setDone();
					}
					
					// If all workItems are 'DONE' and Matrix C has been reassembled...
					if (count == results.size()) {
						this.printResult();
						this.stop();
					}
					
				} // End of inner for loop.
			} // End of outer for loop.
			
			
		} // End of while loop.
	}
	
	
	/**
	 * This method stops the Producer thread by setting the value of the 
	 * 'stop' boolean to 'true' to break the while loop in the run method. 
	 */
	public void stop() {
		this.stop = true;
	}
	
	
	/**
	 * This method populates a sub-matrix created from a larger matrix. The user passes 
	 * a parent matrix parameter, and both a row and a column index from which the method 
	 * will begin copying values into the new sub-matrix. A sub-matrix (subMatrix) parameter 
	 * of a declared size must be passed.
	 * 
	 * @param subM - int[][] - Sub-matrix to be populated.
	 * @param matrix - int[][] - Parent matrix from which the sub-matrix will receive its values.
	 * @param rowIndex - int - The starting row index of the parent matrix.
	 * @param columnIndex - int - The starting column index of the parent matrix.
	 */
	private void populateSubMatrix(int[][] subMatrix, int[][] matrix, int rowIndex, int columnIndex) {
		int numOfRows = subMatrix.length;
		int numOfColumns = subMatrix[0].length;
		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < numOfColumns; j++) {
				subMatrix[i][j] = matrix[i+rowIndex][j+columnIndex];
			}
		}
		this.printResult();
	}
	
	/**
	 * 
	 * @param workItem
	 */
	public void populateMatrixC(WorkItem workItem) {
		for (int i = 0; i <= (workItem.getHighA() - workItem.getLowA()); i++) {
			for (int j = 0; j <= (workItem.getHighB() - workItem.getLowB()); j++) {
				this.matrixC[i + workItem.getLowA()][j + workItem.getLowB()] = workItem.getSubC()[i][j];
			}
		}
		this.printResult();
	}
	
	
	public void printResult() {
		String output = "Matrix C: [";
		for (int row = 0; row < this.matrixC.length; row++) {
			if (row != 0) { 
				output += String.format("%11s", "[");
			}
			for (int column = 0; column < this.matrixC[0].length; column++) {
				output += " " + this.matrixC[row][column] + " ";
			}
			output += "]\n";
		}
		System.out.println(output);
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
		
		output += "\nMatrix B: [";
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
