
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
		
		// maximum size of the shared buffer
		int maxBuffSize = 5;
		
		// the parameter to split rows of A into multiple sub-rows and
		// the columns of B into multiple sub-columns
		int SplitSize = 3; 
		
		// the number of consumer threads
		NumConsumer = 2;
		
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
		
	
		
	}

}
