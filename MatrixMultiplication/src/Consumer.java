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
	
	
	@Override
	public void run() {
		
		// WHERE DO WE SET 'STOP = TRUE'?
		
		while (!this.stop) {
			this.calculateMatrixMultiplication();
			try {
				Thread.sleep(this.sleepTime);		// Force thread to sleep for a specified time.
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} // End of while loop
		
	}
	
	public void stop() {
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
		
		this.outputResult(workItem);		// Print result of multiplication.
		
		if (this.buffer.isDone() && this.buffer.getCount() == 0) {
			this.stop();
		}
	}
	
	private void outputResult(WorkItem workItem) {
		String output = workItem.subAToString() + "  X \n" + workItem.subBToString() + "  = \n" + workItem.subCToString();
		System.out.println(output);
	}
	
}
