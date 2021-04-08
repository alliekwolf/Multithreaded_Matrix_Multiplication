import java.util.Random;

/**
 * Consumer class creates consumer objects that will retrieve chunks of matrixA 
 * and matrixB from the SharedBuffer from the WorkItem objects in the buffer and 
 * perform matrix multiplication on them. The Consumer stores the product of the 
 * subA matrix and subB matrix in subC, a variable in the WorkItem object.
 * 
 * @author Brian Steele
 * @author Cole Walsh
 * @author Allie Wolf
 *
 */
public class Consumer implements Runnable {
	
	private Random rand = new Random();
	
	// Data members
	private int id;
	private SharedBuffer buffer;
	private int maxSleepTime;
	private int sleepTime;
	private int totalSleepTime;
	private int consumerItemsCount;
	private boolean stop;
	
	// Constructor
	/**
	 * Constructor method for Consumer object.
	 * 
	 * @param id - int
	 * @param buffer - SharedBuffer object
	 * @param maxSleepTime - int
	 */
	public Consumer(int id, SharedBuffer buffer, int maxSleepTime) {
		this.id = id;
		this.buffer = buffer;
		this.maxSleepTime = maxSleepTime;
		this.sleepTime = rand.nextInt(this.maxSleepTime);
		this.totalSleepTime = 0;
		this.consumerItemsCount = 0;
		this.stop = false;
	}
	
	// Getters and Setters
	/**
	 * Returns Consumer's int id.
	 * 
	 * @return id - int
	 */
	public int getId() {
		return this.id;
	}
	
	/**
	 * Returns Consumer's int totalSleepTime.
	 * 
	 * @return totalSleepTime - int
	 */
	public int getTotalSleepTime() {
		return this.totalSleepTime;
	}
	
	/**
	 * Returns the number of WorkItem objects the Consumer has processed.
	 * 
	 * @return consumerItemsCount - int
	 */
	public int getConsumerItemsCount() {
		return consumerItemsCount;
	}
	
	/**
	 * Overridden method from the Runnable interface, this method locks the 
	 * SharedBuffer, performs matrix multiplication on a WorkItem, and increments 
	 * the consumerItemsCount by one every time a WorkItem is processed.
	 */
	@Override
	public void run() {
		
		while (!this.stop) {
			try {
				Thread.sleep(this.sleepTime);		// Force thread to sleep for a specified time.
				this.totalSleepTime += this.sleepTime;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.calculateMatrixMultiplication();
			this.consumerItemsCount++;
		} // End of while loop
		
	}
	
	/**
	 * Stops the Consumer thread's run() method by setting the stop boolean to true.
	 */
	public void stop() {
		this.stop = true;
	}
	
	
	// Code edited from this tutorial:  https://www.youtube.com/watch?v=MZenB6qYqc0
	/**
	 * Method for calculating a workItem's subC matrix. Calls the SharedBuffer get() method 
	 * to return a workItem from the SharedBuffer and multiplies its subA and subB matrices 
	 * together and puts the result in the workItem's subC matrix, then changes the workItem's 
	 * State to 'READY'.
	 */
	public void calculateMatrixMultiplication() {
		
		WorkItem workItem = this.buffer.get();		// This is where we will actually get the matrices from the SharedBuffer.
		int[][] mA = workItem.getSubA();
		int[][] mB = workItem.getSubB();
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
		workItem.setState(State.READY);		// WorkItem is 'READY' for the Producer to get subC result.
		
		this.outputResult(workItem);		// Print result of multiplication.
		
		if (this.buffer.isDone() && this.buffer.getCount() == 0) {
			this.stop();
		}
	}
	
	/**
	 * Outputs a workItem's subA, subB, and subC matrices as though they are part of a 
	 * multiplication equation. 
	 * 
	 * @param workItem - WorkItem object
	 */
	private void outputResult(WorkItem workItem) {
		String output = workItem.subAToString() + "  X \n" + workItem.subBToString() + "  = \n" + workItem.subCToString();
		System.out.println(output);
	}
	
}
