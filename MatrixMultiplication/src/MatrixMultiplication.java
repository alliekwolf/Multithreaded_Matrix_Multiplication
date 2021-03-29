
/**
 * 
 * @author Brian Steele
 * @author Cole Walsh
 * @author Allie Wolf
 *
 */
public class MatrixMultiplication {

	public static void main(String[] args) {

		// number of rows in matrixA
		int m = 10;
		// number of columns in matrixA (also number of rows in matrixB
		int n = 10;
		// number of columns in matrixB
		int p = 10;
		// shared buffer object for the producer and consumer
		SharedBuffer sharedB;
		// maximum size of the shared buffer
		int maxBuffSize = 5;
		// Producer object
		Producer producer;
		// Consumer object;
		Consumer consumer;
		// the parameter to split rows of A into multiple sub-rows and
		// the columns of B into multiple sub-columns
		int SplitSize = 3;
		// the number of consumer threads
		int NumConsumer = 2;
		// the maximum sleep time of the producer thread
		// between puttin two pairs of sub-rows of A and sub-columns of B
		// into the shared queue (must wait before next subsections)
		int maxProducerSleepTime = 5;
		// sleep time for Consumber object between doing sub-matrix
		// multiplication
		int maxConsumerSleepTime = 80;
		// the first matrix to multiply
		int[][] matrixA;
		// the second matrix to multiply
		int[][] matrixB;
		// holds the result of the matrix multiplication
		int[][] matrixC;
		// the current moment in simulation time
		// need to clarify, is this turn based or
		// actual system time.
		long simulationCurrentTime;
		// according to the instructions, this should be actual system time.
		long simulationStartTime;
		// according to instructions, this should be actual system time.
		long simulationEndTime;
		// the average amoun of time that threads slept for.
		int averageThreadSleepTime;
		// the number of producer threads created
		int numProducerThreads;
		// the number of consumer threads created
		int numConsumerThreads;
		// the number of items each producer produced and the total
		// numberof all produced items in the simulation
		int producerItemsTotal;
		// the number of items each consumer consumed and the total
		// number of all consumed items in the simulation
		int consumerItemsTotal;
		// number of times the buffer was full...
		int fullBufferCount;
		// number of times the buffer was empty...
		int emptyBufferCount;

		matrixA = generateMatrix(m, n);
		matrixB = generateMatrix(n, p);
		sharedB = new SharedBuffer(maxBuffSize);
		producer = new Producer(sharedB, m, n, p);
		consumer = new Consumer(sharedB);
		int[][] sequentialSolution = calculateMatrixMultiplication(matrixA, matrixB);
		outputMatrixMultiplication(sequentialSolution);

	}

	public String loadConfigurationFile() {
		return "nothing";
	}

	// Code modified from this tutorial: https://www.youtube.com/watch?v=MZenB6qYqc0
	public static int[][] generateMatrix(int m, int n) {
		int[][] result = new int[m][n];

		for (int row = 0; row < m; row++) {
			for (int column = 0; column < n; column++) {
				result[row][column] += Math.floor(Math.random() * 10);
			}
		}
		return result;

	}

	// from Allie's producer class
	private static void outputMatrixMultiplication(int[][] m) {
		String output = "Result: [";
		for (int row = 0; row < m.length; row++) {
			if (row != 0) {
				output += String.format("%9s", "[");
			}
			for (int column = 0; column < m[0].length; column++) {
				output += " " + m[row][column] + " ";
			}
			output += "]\n";
		}
		System.out.println(output);
	}

	//Again, Allie's solution from Consumer class.
	public static int[][] calculateMatrixMultiplication(int[][] mA, int[][] mB) {

		int[][] result = new int[mA.length][mB[0].length];

		// Multiplication logic
		for (int row = 0; row < mA.length; row++) {
			for (int column = 0; column < mB[0].length; column++) {
				for (int i = 0; i < mA[0].length; i++) {
					result[row][column] += mA[row][i] * mB[i][column];
				}
			}
		}
		return result;
	}

	public void incrementFullBufferCount() {

	}

	public void incrementEmptyBufferCount() {

	}

}
