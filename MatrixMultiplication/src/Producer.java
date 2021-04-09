import java.util.ArrayList;
import java.util.Random;

/**
 * Producer class creates a Producer object that takes a number of rows from one 
 * matrix (matrixA), and a number of columns from another (matrixB) two create two 
 * sub-matrices (subA and subB, respectively), with which the Producer object will 
 * then create a WorkItem that it will put in its SharedBuffer. The Producer also 
 * keeps an ArrayList of all WorkItem objects it creates and iterates through the 
 * list for which WorkItems the Consumer thread has finished multiplying.
 * 
 * @author Brian Steele
 * @author Cole Walsh
 * @author Allie Wolf
 *
 */
public class Producer implements Runnable {
	
	private Random rand = new Random();
	
	// Data members
	private int id;
	private SharedBuffer buffer;
	private int maxSleepTime;
	private int sleepTime;
	private int totalSleepTime;
	private int[][] matrixA;
	private int[][] matrixB;
	private int[][] matrixC;
	private int m;		// rows in Matrix A
	private int n;		// columns in Matrix A, rows in Matrix B
	private int p;		// columns in Matrix B
	private int splitSize;
	private ArrayList<WorkItem> producerItems;
	private int producerItemsCount;
	private int doneCount;
	private boolean stop;
	
	// Constructor
	/**
	 * Constructor method for Producer object.
	 * 
	 * @param id - int
	 * @param buffer - SharedBuffer object
	 * @param maxSleepTime - int
	 * @param matrixA - int[][]
	 * @param matrixB - int[][]
	 * @param splitSize - int
	 */
	public Producer(int id, SharedBuffer buffer, int maxSleepTime, int[][] matrixA, int[][] matrixB, int splitSize) {
		this.id = id;
		this.buffer = buffer;
		this.maxSleepTime = maxSleepTime;
		this.sleepTime = rand.nextInt(this.maxSleepTime);
		this.totalSleepTime = 0;
		this.matrixA = matrixA;
		this.matrixB = matrixB;
		this.m = this.matrixA.length;
		this.n = this.matrixB.length;
		this.p = this.matrixB[0].length;
		this.matrixC = new int[this.m][this.p];
		this.splitSize = splitSize;
		this.producerItems = new ArrayList<WorkItem>();
		this.producerItemsCount = this.producerItems.size();
		this.doneCount = 0;
		this.stop = false;
	}
	
	// Getters and Setters
	/**
	 * Returns Producer's int id.
	 * 
	 * @return id - int
	 */
	public int getId() {
		return this.id;
	}
	
	/**
	 * Returns Producer's int totalSleepTime.
	 * 
	 * @return totalSleepTime - int
	 */
	public int getTotalSleepTime() {
		return this.totalSleepTime;
	}
	
	/**
	 * get the values of matrix C
	 * @return int[][] the values of matrix C.
	 */
	public int[][] getMatrixC() {
		return this.matrixC;
	}
	
	/**
	 * Returns Producer's int producerItemsCount (should be the length of producerItems ArrayList).
	 * 
	 * @return producerItemsCount - int
	 */
	public int getProducerItemsCount() {
		return this.producerItemsCount;
	}
	
	/**
	 * Overridden method from the Runnable interface, this method locks the 
	 * SharedBuffer, performs matrix multiplication on a WorkItem, and increments 
	 * the consumerItemsCount by one every time a WorkItem is processed. Overridden 
	 * method from the Runnable interface, this method locks the SharedBuffer, 
	 * splits the sub-matrices of a WorkItem based on the Producer's splitSize, 
	 * calls the checkResults() method to see which WorkItem's have been completed 
	 * by the Consumer thread.
	 */
	@Override
	public void run() {
		System.out.println(this);		// Check that matrices have populated correctly.
		
		this.splitMatrices();			// Split matrices into sub-matrices.
		
		while (!this.stop) {
			this.checkResults();		// Check results to see if all workItems are 'DONE'.
			try {
				Thread.sleep(this.sleepTime);
				this.totalSleepTime += this.sleepTime;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (this.doneCount == producerItems.size()) {		// If all workItems are 'DONE' and Matrix C has been reassembled...
				this.stop();									// ...stop Producer thread.
			}
		}
		
	}
	
	
	/**
	 * This method stops the Producer thread by setting the value of the 
	 * 'stop' boolean to 'true' to break the while loop in the run method. 
	 */
	public void stop() {
		this.stop = true;
	}
	
	/**
	 * This method splits the Producer's matrixA and matrixB into smaller sub-matrices 
	 * based on the split size.
	 */
	public void splitMatrices() {
		int remainderA = (this.m % this.splitSize);		// remainder rows in matrixA
		int remainderB = (this.p % this.splitSize);		// remainder columns in matrixB
		int stepA = ((this.m - 1) / this.splitSize);
		int stepB = ((this.p - 1) / this.splitSize);
		
		// Split matrices into sub-matrices, starting with the first sub-matrix (subA) created from matrixA.
		for (int i = 0; i <= stepA; i++) {
			
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
			for (int j = 0; j <= stepB; j++) {
				
				int column = (j * this.splitSize);				// column index of matrixB = j * split size
				int littleP;									// number of columns that will be in subB
				
				if ((this.p - column) > remainderB) {		// If there are more columns left than remainderB...
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
				this.producerItems.add(workItem);
				this.producerItemsCount++;
				this.buffer.put(workItem);
				
				System.out.println(workItem.subAToString());
				System.out.println(workItem.subBToString());
				
				this.checkResults();
				
				// If we've created all necessary workItems for these matrices...
				if (highA == (subA.length - 1) && highB == (subB[0].length - 1)) {
					this.buffer.setState(State.DONE);		// ...set SharedBuffer state to 'DONE'.
				}
				
			} // End of inner for loop.
		} // End of outer for loop.
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
	}
	
	/**
	 * Populate matrixC with values in a workItem's subC matrix.
	 * 
	 * @param workItem - WorkItem object
	 */
	public void populateMatrixC(WorkItem workItem) {
		for (int i = 0; i <= (workItem.getHighA() - workItem.getLowA()); i++) {
			for (int j = 0; j <= (workItem.getHighB() - workItem.getLowB()); j++) {
				this.matrixC[i + workItem.getLowA()][j + workItem.getLowB()] = workItem.getSubC()[i][j];
			}
		}
		this.printResult();
	}
	
	
	/**
	 * Method for looping through the producerItems ArrayList to find WorkItems that have 
	 * been marked 'DONE' by the Consumer thread. Increments doneCount for every DONE WorkItem 
	 * object found and populates matrixC with the WorkItem's finished subC matrix.
	 */
	private void checkResults() {
		this.doneCount = 0;					// Reset doneCount to 0 every time method is called.
		
		for (WorkItem item : producerItems) {		// Loop through results ArrayList to find all 'DONE' workItems
			if (item.isDone()) {				// If a workItem is 'DONE' for calculating...
				this.doneCount++;				// ...just increment the count of 'DONE' workItems.
			} else if (item.isReady()) {		// Else, if workItem is 'READY'...
				populateMatrixC(item);			// ...add it's subC matrix to Matrix C
				item.setState(State.DONE);		// ...and set the workItem's state to 'DONE'.
				this.doneCount++;				// ...increment the count of 'DONE' workItems.
			}
		}
	}
	
	/**
	 * Prints values of matrixC.
	 */
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
