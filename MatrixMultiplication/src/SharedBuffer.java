/**
 * 
 * @author Brian Steele
 * @author Cole Walsh
 * @author Allie Wolf
 *
 */
public class SharedBuffer {
	
	// Data members
	private final int MAX_BUFF_SIZE;
	private WorkItem[] buffArray;
	private int count;
	private int in;
	private int out;
	private int itemCount;
	private int fullBufferCount;
	private int emptyBufferCount;
	private State state;
	
	// Constructor
	/**
	 * Constructor method for SharedBuffer object.
	 * 
	 * @param maxBuffSize - int
	 */
	public SharedBuffer(int maxBuffSize) {
		this.MAX_BUFF_SIZE = maxBuffSize;
		this.count = 0;
		this.in = 0;
		this.out = 0;
		this.buffArray = new WorkItem[this.MAX_BUFF_SIZE];
		this.itemCount = 0;
	}
	
	// Getters and Setters
	/**
	 * Returns SharedBuffer's MAX_BUFF_SIZE as an int.
	 * 
	 * @return MAX_BUFF_SIZE - int
	 */
	public int getMaxBuffSize() {
		return MAX_BUFF_SIZE;
	}
	
	/**
	 * Returns SharedBuffer's count as an int.
	 * 
	 * @return count - int
	 */
	public int getCount() {
		return this.count;
	}
	
	/**
	 * Returns SharedBuffer's fullBufferCount as an int.
	 * 
	 * @return count - int
	 */
	public int getFullBufferCount() {
		return this.fullBufferCount;
	}
	
	/**
	 * Returns SharedBuffer's emptyBufferCount as an int.
	 * 
	 * @return emptyBufferCount - int
	 */
	public int getEmptyBufferCount() {
		return this.emptyBufferCount;
	}
	
	/**
	 * Returns SharedBuffer's itemCount as an int.
	 * 
	 * @return itemCount - int
	 */
	public int getItemCount() {
		return this.itemCount;
	}
	
	// Methods controlling SharedBuffer State
	/**
	 * Sets the SharedBuffer's State to either 'WAITING', 'READY', or 'DONE' based 
	 * on the parameter.
	 * 
	 * @param state - State
	 */
	public void setState(State state) {
		this.state = state;
	}
	
	/**
	 * Checks the SharedBuffer's State and returns true if 'READY'.
	 * 
	 * @return true or false
	 */
	public boolean isReady() {
		if(this.state == State.READY) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Checks the SharedBuffer's State and returns true if 'DONE'.
	 * 
	 * @return true or false
	 */
	public boolean isDone() {
		if(this.state == State.DONE) {
			return true;
		} else {
			return false;
		}
	}
	
	
	// Synchronized methods
	/**
	 * 
	 * 
	 * @return WorkItem object
	 */
	public synchronized WorkItem get() {
		WorkItem workItem;
		
		while (this.count == 0) {
			this.emptyBufferCount++;		// Increment count of times SharedBuffer is EMPTY.
			try {
				wait();
			} catch(InterruptedException e) {}
		}
		workItem = this.buffArray[this.out];
		this.buffArray[this.out] = null;
		
		this.out = (this.out + 1) % this.MAX_BUFF_SIZE;
		this.count--;
		
		notifyAll();
		
		System.out.println("\n* Consumer gets row " + workItem.getLowA() + "-" + workItem.getHighA() + " and column " + 
				workItem.getLowB() + "-" + workItem.getHighB() + " from the shared buffer. *");
		System.out.println("  Consumer finishes calculating...");
		
		return workItem;
	}
	
	/**
	 * 
	 * 
	 * @param workItem - WorkItem object
	 */
	public synchronized void put(WorkItem workItem) {
		
		while (this.count == this.MAX_BUFF_SIZE) {
			this.fullBufferCount++;			// Increment count of times SharedBuffer is FULL.
			try {
				wait();
			} catch (InterruptedException e) {}
		}
		this.buffArray[this.in] = workItem;
		this.in = (this.in + 1) % this.MAX_BUFF_SIZE;
		this.count++;
		this.itemCount++;
		
		notifyAll();
		
		System.out.println("* Producer puts row " + workItem.getLowA() + "-" + workItem.getHighA() + " and column " + 
							workItem.getLowB() + "-" + workItem.getHighB() + " into the shared buffer. *");
		System.out.println("  Work Item No. " + this.itemCount);
	}
	
}
