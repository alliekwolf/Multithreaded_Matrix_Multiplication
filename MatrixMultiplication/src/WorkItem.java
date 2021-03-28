
public class WorkItem {

	int[][] subA;
	int[][] subB;
	int [][] subC;
	int lowA, highA;
	int lowB, highB;
	boolean done;
	
	public WorkItem(int[][]subA, int[][]subB, int[][]subC,
						int lowA, int highA, int lowB, int highB) {
		
		this.subA = subA;
		this.subB = subB;
		this.subC = subC;
		this.lowA = lowA;
		this.lowB = lowB;
		this.highA = highA;
		this.highB = highB;
		
	}
	
	public void setDone() {
		this.done = true;
	}
	
	public boolean getDone() {
		return this.done;
	}
}
