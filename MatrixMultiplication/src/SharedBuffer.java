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
	private State state;
	
	// Constructor
	public SharedBuffer(int maxBuffSize) {
		this.MAX_BUFF_SIZE = maxBuffSize;
		this.count = 0;
		this.in = 0;
		this.out = 0;
		this.buffArray = new WorkItem[this.MAX_BUFF_SIZE];
		this.itemCount = 0;
	}
	
	// Getters and Setters
	public int getMaxBuffSize() {
		return MAX_BUFF_SIZE;
	}
	
	public int getCount() {
		return this.count;
	}
	
	public int getItemCount() {
		return this.itemCount;
	}
	
	// Methods controlling SharedBuffer State
	public void setState(State state) {
		this.state = state;
	}
	public boolean isReady() {
		if(this.state == State.READY) {
			return true;
		} else {
			return false;
		}
	}
	public boolean isDone() {
		if(this.state == State.DONE) {
			return true;
		} else {
			return false;
		}
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
		
		this.out = (this.out + 1) % this.MAX_BUFF_SIZE;
		this.count--;
		
		notifyAll();
		
		System.out.println("\n* Consumer gets row " + workItem.getLowA() + "-" + workItem.getHighA() + " and column " + 
				workItem.getLowB() + "-" + workItem.getHighB() + " from the shared buffer. *");
		System.out.println("  Consumer finishes calculating...");
		
		return workItem;
	}
	
	public synchronized void put(WorkItem workItem) {
		while (this.count == this.MAX_BUFF_SIZE) {
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
