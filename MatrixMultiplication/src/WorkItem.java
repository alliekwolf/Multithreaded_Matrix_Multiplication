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
	 * Constructor method for WorkItem object.
	 * 
	 * @param subA - int[][] of sub rows from matrix A
	 * @param subB - int[][] of sub rows from matrix B
	 * @param subC - int[][] will hold the result of subA X subB - Consumer class
	 * 					will do the multiplication and store the result in subC
	 * @param lowA
	 * @param highA
	 * @param lowB
	 * @param highB
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
	 * Returns WorkItem's subA matrix as a 2-D int array.
	 * 
	 * @return subA - int[][]
	 */
	public int[][] getSubA() {
		return this.subA;
	}
	
	/**
	 * Returns WorkItem's subB matrix as a 2-D int array.
	 * 
	 * @return subB - int[][]
	 */
	public int[][] getSubB() {
		return this.subB;
	}
	
	/**
	 * Returns WorkItem's subC matrix as a 2-D int array.
	 * 
	 * @return subC - int[][]
	 */
	public int[][] getSubC() {
		return this.subC;
	}
	
	/**
	 * Sets WorkItem's subC matrix to a 2-D int array.
	 * 
	 * @param subC - int[][]
	 */
	public void setSubC(int[][] subC) {
		this.subC = subC;
	}
	
	/**
	 * 
	 * 
	 * @return lowA - int
	 */
	public int getLowA() {
		return this.lowA;
	}
	
	/**
	 * 
	 * 
	 * @return lowB - int
	 */
	public int getLowB() {
		return this.lowB;
	}
	
	/**
	 * 
	 * 
	 * @return highA - int
	 */
	public int getHighA() {
		return this.highA;
	}
	
	/**
	 * 
	 * 
	 * @return highB - int
	 */
	public int getHighB() {
		return this.highB;
	}
	
	/**
	 * Sets the WorkItem's State to either 'WAITING', 'READY', or 'DONE' based 
	 * on the parameter.
	 * 
	 * @param state - State
	 */
	public void setState(State state) {
		this.state = state;
	}
	
	/**
	 * Checks the WorkItem's State and returns true if 'WAITING'.
	 * 
	 * @return true or false
	 */
	public boolean isWaiting() {
		if (this.state == State.WAITING) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Checks the WorkItem's State and returns true if 'READY'.
	 * 
	 * @return true or false
	 */
	public boolean isReady() {
		if (this.state == State.READY) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Checks the WorkItem's State and returns true if 'DONE'.
	 * 
	 * @return true or false
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
	 * Multiplies the WorkItem's subA and subB matrices together and puts the result 
	 * in the subC matrix.
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
	 * Outputs the WorkItem's subA matrix.
	 * 
	 * @return String
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
	 * Outputs the WorkItem's subB matrix.
	 * 
	 * @return String
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
	 * Outputs the WorkItem's subC matrix.
	 * 
	 * @return String
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
