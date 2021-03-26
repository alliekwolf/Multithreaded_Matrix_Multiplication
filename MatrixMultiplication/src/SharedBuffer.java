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
	private int[] buffArray;
	
	// Constructor
	public SharedBuffer(int maxBuffSize) {
		this.count = 0;
		this.in = 0;
		this.out = 0;
		this.maxBuffSize = maxBuffSize;
		this.buffArray = new int[this.maxBuffSize];
	}
	
	// maxBuffSize Getter
	public int getMaxBuffSize() {
		return maxBuffSize;
	}
	
	// Synchronized methods
	public synchronized int get() {
		
		notifyAll();
		return 0;
	}
	
	public synchronized void put(int value) {
		
		notifyAll();
	}
	
}
