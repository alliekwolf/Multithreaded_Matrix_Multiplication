/**
 * 
 * @author Brian Steele
 * @author Cole Walsh
 * @author Allie Wolf
 *
 */
public class Consumer implements Runnable {
	
	// Data members
	private SharedBuffer buffer;
	private Producer producer;		// FOR TESTING ONLY. ALL REFERENCES TO 'PRODUCER' WILL EVENTUALLY REFERENCE 'BUFFER'. DELETE LATER.
	private int id;
	private int sleepTime;
	private boolean stop;
	
	
	// Constructor
	public Consumer(SharedBuffer buffer) {
		this.buffer = buffer;
		this.id = 1;
		this.sleepTime = 80;
		this.stop = false;
	}
	
	// THIS CONSTRUCTOR IS USED JUST FOR TESTING. DELETE LATER.
	public Consumer(SharedBuffer buffer, Producer producer) {
		this.buffer = buffer;
		this.producer = producer;
		this.id = 1;
		this.sleepTime = 80;
	}
	
	@Override
	public void run() {
		
		while (this.stop == false) {
			this.calculateMatrixMultiplication();
			try {
				Thread.sleep(this.sleepTime);		// Force thread to sleep for a specified time.
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void stopThread() {
		this.stop = true;
	}
	
	
	// Code edited from this tutorial:  https://www.youtube.com/watch?v=MZenB6qYqc0
	public void calculateMatrixMultiplication() {
		
		WorkItem workItem = this.buffer.get();		// This is where we will actually get the matrices from the SharedBuffer.
		int[][] mA = workItem.subA;
		int[][] mB = workItem.subB;
		int[][] result = new int[mA.length][mB[0].length];
		
		// Multiplication logic
		for (int row = 0; row < mA.length; row++) {
			for (int column = 0; column < mB[0].length; column++) {
				for (int i = 0; i < mA[0].length; i++) {
					result[row][column] += mA[row][i] * mB[i][column];
				}
			}
		}
		workItem.setSubC(result);
		workItem.setDone();
		this.buffer.put(workItem);
		
		this.outputMatrixMultiplication(result);		// Print result of multiplication.
	}
	
	private void outputMatrixMultiplication(int[][] m) {
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
	
}
