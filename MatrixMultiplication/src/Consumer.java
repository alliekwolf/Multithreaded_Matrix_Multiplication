/**
 * Consumer class creates consumer objects that will retrieve
 * chunks of matrixA and matrixB from the SharedBuffer from the
 * WorkItems in the buffer and perform matrix multiplication
 * on them. The consumer stores the product of subMatrixA subMatrixB in 
 * subC, a variable in the WorkItem.
 * 
 * @author Brian Steele
 * @author Cole Walsh
 * @author Allie Wolf
 *
 */
public class Consumer implements Runnable {
	
	// Data members
	private int id;
	private SharedBuffer buffer;
	private int sleepTime;
	private int consumerItemsCount;
	private boolean stop;
	
	
	// Constructor
	/**
	 * Default constructor for Consumer
	 * 
	 * @param buffer - SharedBuffer object to hold WorkItems.
	 */
	public Consumer(SharedBuffer buffer) {
		this.id = 1;
		this.buffer = buffer;
		this.sleepTime = 80;
		this.consumerItemsCount = 0;
		this.stop = false;
	}
	
	// Getters and Setters
	/**
	 * Returns the number of WorkItems the consumer has processed
	 * 
	 * @return int, the consumerItemsCount
	 */
	public int getConsumerItemsCount() {
		return consumerItemsCount;
	}
	
	
	/**
	 * Overridden method from the Runnable interface,
	 * this method locks the sharedBuffer and performs 
	 * the matrix multiplication on a WorkItem
	 */
	@Override
	public void run() {
		
		while (!this.stop) {
			try {
				Thread.sleep(this.sleepTime);		// Force thread to sleep for a specified time.
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.calculateMatrixMultiplication();
			this.consumerItemsCount++;
		} // End of while loop
		
	}
	
	/**
	 * Useful method for stopping the run() method.
	 */
	public void stop() {
		this.stop = true;
	}
	
	
	// Code edited from this tutorial:  https://www.youtube.com/watch?v=MZenB6qYqc0
	
	/**
	 * Retrieves a WorkItem from the buffer using buffer.get(),
	 * loads the subA and subB submatricies, performs the matrix
	 * multiplication and puts the results into subC of the WorkItem.
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
		workItem.setState(State.READY);
		
		this.outputResult(workItem);		// Print result of multiplication.
		
		if (this.buffer.isDone() && this.buffer.getCount() == 0) {
			this.stop();
		}
	}
	
	/**
	 * outputs a pretty-printed representation of subA, subB and the
	 * result subC of the parts of the matricies in the WorkItem.
	 * @param workItem
	 */
	private void outputResult(WorkItem workItem) {
		String output = workItem.subAToString() + "  X \n" + workItem.subBToString() + "  = \n" + workItem.subCToString();
		System.out.println(output);
	}
	
}
