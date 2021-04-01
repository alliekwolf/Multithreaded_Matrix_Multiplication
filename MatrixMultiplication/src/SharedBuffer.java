/**
 * 
 * @author Brian Steele
 * @author Cole Walsh
 * @author Allie Wolf
 *
 */
public class SharedBuffer {
	
	// Data members
	private WorkItem[] buffArray;
	private int count;
	private int in;
	private int out;
	private int maxBuffSize;
	private int itemCount;
	private boolean done;
	
	// Constructor
	public SharedBuffer(int maxBuffSize) {
		this.count = 0;
		this.in = 0;
		this.out = 0;
		this.maxBuffSize = maxBuffSize;
		this.buffArray = new WorkItem[this.maxBuffSize];
		this.itemCount = 0;
		this.done = false;
	}
	
	// Getters and Setters
	public int getMaxBuffSize() {
		return maxBuffSize;
	}
	
	public int getCount() {
		return this.count;
	}
	
	
	// Synchronized methods
	public synchronized WorkItem get() {
		WorkItem workItem;
		while (this.count == 0) {
			try {
				wait();
			} catch(InterruptedException e) {}
		}
		workItem = this.buffArray[this.out];
		this.buffArray[this.out] = null;
		
		this.out = (this.out + 1) % this.maxBuffSize;
		this.count--;
		
		notifyAll();
		
		System.out.println("\n* Consumer gets row " + workItem.lowA + "-" + workItem.highA + " and column " + 
				workItem.lowB + "-" + workItem.highB + " from the shared buffer. *");
		System.out.println("  Consumer finishes calculating...");
		
		return workItem;
	}
	
	public synchronized void put(WorkItem workItem) {
		while (this.count == this.maxBuffSize) {
			try {
				wait();
			} catch (InterruptedException e) {}
		}
		this.buffArray[this.in] = workItem;
		this.in = (this.in + 1) % this.maxBuffSize;
		this.count++;
		this.itemCount++;
		
		notifyAll();
		
		System.out.println("* Producer puts row " + workItem.lowA + "-" + workItem.highA + " and column " + 
							workItem.lowB + "-" + workItem.highB + " into the shared buffer. *");
		System.out.println("  Work Item No. " + this.itemCount);
	}
	
	// Method to switch 'done' bit to true, telling Producer/Consumer threads to STOP.
	public void setDone() {
		this.done = true;
	}
	public boolean isDone() {
		if(this.done == true) {
			return true;
		} else {
			return false;
		}
	}
	
}
