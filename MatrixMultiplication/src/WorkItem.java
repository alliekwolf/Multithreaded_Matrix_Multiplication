import java.util.Arrays;

/**
 * WorkItem class to hold subrows of matrix A and subcolumns of
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
	protected int[][] subA;		//two dimensional array with the sub-rows of matrix A
	protected int[][] subB;		//two dimensional array with the sub-rows of matrix B
	protected int[][] subC;		//matrix multiplication result of subA X subB 
	protected int lowA, highA;	//low and high row indexes of the sub-rows copied from matrix A,
	protected int lowB, highB;	//low and high row indexes of the sub-rows copied from matrix B,
	protected boolean done;		//boolean indicating whether or not the work has completed on this item.
	
	// Constructor
	/**
	 * Constructor method for WorkItem
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
	}
	
	public WorkItem(int[][]subA, int[][]subB) {
		this.subA = subA;
		this.subB = subB;
		this.subC = new int[this.subA[0].length][this.subB.length];
		this.lowA = 0;
		this.lowB = 0;
		this.highA = 0;
		this.highB = 0;
	}
	
	// Getters and Setters
	public boolean getDone() {
		return this.done;
	}
	
	public void setDone() {
		this.done = true;
	}
	
	public int[][] getSubC() {
		return this.subC;
	}
	
	public void setSubC(int[][] subC) {
		this.subC = subC;
	}
	
	
	// Multiplication method from the Consumer class.
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
		output += this.subAToString();
		
		output += this.subBToString();
		
		if (this.done == true) {
			output += this.subCToString();
		} else {
			output += "Sub C: [ EMPTY ]\n";
		}
		
		output += "lowA: " + lowA + "\nhighA: " + highA + "\nlowB: " + lowB + "\nhighB: " + highB + "\nDone: " + done;
		
		return output;
	}
	
	
}
