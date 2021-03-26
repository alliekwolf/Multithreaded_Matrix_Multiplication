/**
 * 
 * @author Brian Steele
 * @author Cole Walsh
 * @author Allie Wolf
 *
 */
public class MatrixMultiplication {

	public static void main(String[] args) {
		
		// variables
		SharedBuffer buff = new SharedBuffer(5);
		int m = 3;
		int n = 4;
		int p = 3;
		
		// Create Producer and Consumer threads.
		Thread t1 = new Thread(new Producer(buff, m, n, p));
//		Thread t2 = new Thread(new Consumer(buff));
		t1.start();
//		t2.start();
		
	}

}
