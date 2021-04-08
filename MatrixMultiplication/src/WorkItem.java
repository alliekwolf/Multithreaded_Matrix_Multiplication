/**
 * WorkItem class to hold sub-rows of matrix A and sub-columns of
 * matrix B to implement matrix multiplication. The WorkItem objects
 * will be stored in a SharedBuffer so that they can be passed from 
 * a producer to a consumer.
 * 
 * @author Brian Steele
 * @author Cole Walsh
 * @author Allie Wolf
 *
 */
public class WorkItem {
	
	// Data members
	private int[][] subA;		//two dimensional array with the sub-rows of matrix A
	private int[][] subB;		//two dimensional array with the sub-rows of matrix B
	private int[][] subC;		//matrix multiplication result of subA X subB 
	private int lowA, highA;	//low and high row indexes of the sub-rows copied from matrix A,
	private int lowB, highB;	//low and high row indexes of the sub-rows copied from matrix B,
	private State state;
	
	// Constructor
	/**
	 * Constructor method for WorkItem
	 * 
	 * @param subA - int[][] of sub rows from matrix A
	 * @param subB - int[][] of sub rows from matrix B
	 * @param subC - int[][] will hold the result of subA X subB - Consumer class
	 * 					will do the multiplication and store the result in subC
	 * @param lowA - int the lowest number of rows and columns from matrixA
	 * 				that will be used in subA
	 * @param highA - int the highest row and column numbers from matrixA
	 * 					that will be used to create subA
	 * @param lowB - int the lowest row and column numbers from matrixB
	 * 					that will be used to create subB
	 * @param highB - int the highest row and column numbers from matrixB that
	 * 					will be used to create subC
	 * @param state - enum indicating where in the process the workItem is,
	 * 					READY indicates workItem is ready for the producer to
	 * 					get the subC results, DONE means the consumer has 
	 * 					returned subC and the Producer has retrieved it. DONE
	 * 					is also used to track the number of completed work
	 * 					items. WAITING is used when the workItem is created.
	 * 					
	 */
	public WorkItem(int[][]subA, int[][]subB, int lowA, int highA, int lowB, int highB) {
		this.subA = subA;
		this.subB = subB;
		this.subC = new int[this.subA[0].length][this.subB.length];
		this.lowA = lowA;
		this.lowB = lowB;
		this.highA = highA;
		this.highB = highB;
		this.state = State.WAITING;
	}
	
	// Getters and Setters
	/**
	 * Get the contents of subA
	 * @return int[][] the contents of subA
	 */
	public int[][] getSubA() {
		return this.subA;
	}
	
	/**
	 * Get the contents of subB
	 * @return - int[][] the contents of subB
	 */
	public int[][] getSubB() {
		return this.subB;
	}
	
	/**
	 * Get the contents of subC
	 * @return - int[][] the contents of subC
	 */
	public int[][] getSubC() {
		return this.subC;
	}
	
	/**
	 * Set the contents of subC
	 * @param subC - int[][] holding the product of subA and subB
	 */
	public void setSubC(int[][] subC) {
		this.subC = subC;
	}
	
	/**
	 * Get the lowest row and column locations for the items held in 
	 * subA - they will be used to assemble the entire matrixC result in the 
	 * 		Producer.
	 * @return int low row and column locations for subA contents.
	 */
	public int getLowA() {
		return this.lowA;
	}
	
	/**
	 * Get the lowest row and column locations for the items held in 
	 * subB - they will be used to assemble the entire matrixC result in the 
	 * 		Producer.
	 * @return int low row and column locations for subB contents.
	 */
	public int getLowB() {
		return this.lowB;
	}
	
	/**
	 * Get the highest row and column locations for the items held in 
	 * subA - they will be used to assemble the entire matrixC result in the 
	 * 		Producer.
	 * @return int low row and column locations for subA contents.
	 */
	public int getHighA() {
		return this.highA;
	}
	
	/**
	 * Get the highest row and column locations for the items held in 
	 * subB - they will be used to assemble the entire matrixC result in the 
	 * 		Producer.
	 * @return int low row and column locations for subB contents.
	 */
	public int getHighB() {
		return this.highB;
	}
	
	/**
	 * Set the state of the workItem.
	 * @param state State object that holds an enum for possible states.
	 */
	public void setState(State state) {
		this.state = state;
	}
	
	/**
	 * Checks if the state is set to READY
	 * @return boolean checks if the state of the workItem is ready.
	 */
	public boolean isReady() {
		if (this.state == State.READY) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Checks if the state is set to DONE
	 * @return boolean checks if the state of the workItem is DONE.
	 */
	public boolean isDone() {
		if (this.state == State.DONE) {
			return true;
		} else {
			return false;
		}
	}
	
	
	// Multiplication method from the Consumer class.
	/**
	 * Performs the matrix math for subA and subB and puts the 
	 * product in subC.
	 */
	public void multiplySubMatrices() {
		// Multiplication logic
		for (int row = 0; row < this.subA.length; row++) {
			for (int column = 0; column < this.subB[0].length; column++) {
				for (int i = 0; i < this.subA[0].length; i++) {
					this.subC[row][column] += this.subA[row][i] * this.subB[i][column];
				}
			}
		}
	}
	
	
	// Print and String methods
	/**
	 * Creates a formated string for subA that can be printed.
	 * @return String - formated version of subA
	 */
	public String subAToString() {
		String output = "Sub A: [";
		for (int row = 0; row < this.subA.length; row++) {
			if (row != 0) { 
				output += String.format("%8s", "[");
			}
			for (int column = 0; column < this.subA[0].length; column++) {
				output += " " + this.subA[row][column] + " ";
			}
			output += "]\n";
		}
		return output; 
	}
	
	/**
	 * Creates a formated string for subB that can be printed.
	 * @return String - formated version of subB
	 */
	public String subBToString() {
		String output = "Sub B: [";
		for (int row = 0; row < this.subB.length; row++) {
			if (row != 0) { 
				output += String.format("%8s", "[");
			}
			for (int column = 0; column < this.subB[0].length; column++) {
				output += " " + this.subB[row][column] + " ";
			}
			output += "]\n";
		}
		return output; 
	}
	
	/**
	 * Creates a formated string for subC that can be printed.
	 * @return String - formated version of subC
	 */
	public String subCToString() {
		String output = "Sub C: [";
		for (int row = 0; row < this.subC.length; row++) {
			if (row != 0) { 
				output += String.format("%8s", "[");
			}
			for (int column = 0; column < this.subC[0].length; column++) {
				output += " " + this.subC[row][column] + " ";
			}
			output += "]\n";
		}
		return output; 
	}
	
	/**
	 * Creates a printable string representation of the WorkItem
	 * 
	 * @return string representing workItem.
	 */
	@Override
	public String toString() {
		String output = "Work Item: \n";
		boolean isDone;
		
		output += this.subAToString();
		output += this.subBToString();
		
		if (this.isDone()) {
			output += this.subCToString();
			isDone = true;
		} else {
			output += "Sub C: [ ... ]\n";
			isDone = false;
		}
		
		output += "lowA: " + lowA + "\nhighA: " + highA + "\nlowB: " + lowB + "\nhighB: " + highB + "\nDone: " + isDone;
		
		return output;
	}
	
	
}
