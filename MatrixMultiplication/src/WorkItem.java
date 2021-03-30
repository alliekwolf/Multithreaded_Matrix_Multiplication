
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
	int[][] subA;		//two dimensional array with the sub-rows of matrix A
	int[][] subB;		//two dimensional array with the sub-rows of matrix B
	int[][] subC;		//matrix multiplication result of subA X subB 
	int lowA, highA;	//low and high row indexes of the sub-rows copied from matrix A,
	int lowB, highB;	//low and high row indexes of the sub-rows copied from matrix B,
	boolean done;		//boolean indicating whether or not the work has completed on this item.
	
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
	public void setDone() {
		this.done = true;
	}
	
	public boolean getDone() {
		return this.done;
	}
	
	public void setSubC(int[][] subC) {
		this.subC = subC;
	}
	
	public int[][] getSubC() {
		return this.subC;
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
	
	public String printSubMatrices() {
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
		
		output += "Sub B: [";
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
	
}
