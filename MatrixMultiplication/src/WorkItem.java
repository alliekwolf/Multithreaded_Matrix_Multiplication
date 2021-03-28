/*
 * 
 * 
 *
 */


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

	//two dimensional array with the sub-rows of matrix A
	int[][] subA;
	//two dimensional array with the sub-rows of matrix B
	int[][] subB;
	//maxtrix multiplication result of subA X subB
	int [][] subC;
	//low and high row indexes of the sub-rows copied from matrix A, 
	int lowA, highA;
	//low and high row indexes of the sub-rows copied from matrix B,
	int lowB, highB;
	//boolean indicating whether or not the work has completed on this item.
	boolean done;
	
	//constructor
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
	public WorkItem(int[][]subA, int[][]subB,
						int lowA, int highA, int lowB, int highB) {
		
		this.subA = subA;
		this.subB = subB;
		this.subC = subC;
		this.lowA = lowA;
		this.lowB = lowB;
		this.highA = highA;
		this.highB = highB;
		
	}
	
	
	
	public void setDone() {
		this.done = true;
	}
	
	public boolean getDone() {
		return this.done;
	}
	
	public void setSubC(int[][] subC) {
		this.subC = subC;
	}
	
	public int[][] getsubC() {
		return this.subC;
	}
}
