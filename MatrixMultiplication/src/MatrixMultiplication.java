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
		int m = 2;		// rows in Matrix A
		int n = 3;		// columns in Matrix A, rows in Matrix B
		int p = 2;		// columns in Matrix B
		
		// Create Producer and Consumer threads.
		Producer producer1 = new Producer(buff, m, n, p);
		Consumer consumer1 = new Consumer(buff, producer1);
		
		Thread t1 = new Thread(producer1);
		Thread t2 = new Thread(consumer1);
		
		t1.start();
		t2.start();
		
	}

}
