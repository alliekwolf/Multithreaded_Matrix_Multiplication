/**
 * 
 * @author Brian Steele
 * @author Cole Walsh
 * @author Allie Wolf
 *
 */
public class SharedBuffer {
	
	// Data members
	private int count;
	private int in;
	private int out;
	private int maxBuffSize;
	private WorkItem[] buffArray;
	
	// Constructor
	public SharedBuffer(int maxBuffSize) {
		this.count = 0;
		this.in = 0;
		this.out = 0;
		this.maxBuffSize = maxBuffSize;
		this.buffArray = new WorkItem[this.maxBuffSize];
	}
	
	// maxBuffSize Getter
	public int getMaxBuffSize() {
		return maxBuffSize;
	}
	
	// Synchronized methods
	public synchronized WorkItem get() {
		WorkItem workItem;
		while (count == 0) {
			try {
				wait();
			} catch(InterruptedException e) {}
		}
		workItem = this.buffArray[this.out];
		this.out = (this.out + 1) % this.maxBuffSize;
		notifyAll();
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
		
		notifyAll();
	}
	
}
