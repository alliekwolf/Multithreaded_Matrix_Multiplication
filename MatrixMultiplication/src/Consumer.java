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
	
	// Constructor
	public Consumer(SharedBuffer buffer) {
		this.buffer = buffer;
		this.id = 1;
	}
	
	@Override
	public void run() {
		// TODO Apply matrix multiplication to the matrices in the SharedBuffer.
		
	}
	
}
