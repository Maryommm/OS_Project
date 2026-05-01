import java.util.concurrent.atomic.AtomicInteger;

public class PCB {

private int processID;
private int burstTime;
private int priority;
private int memoryRequired;

private int waitingTime;
private int turnaroundTime;

private int startTime;
private int terminationTime;

private String state;

// Remaining burst for RR
private int remainingBurst;

       public PCB(int processID, int burstTime, int priority, int memoryRequired) {

	        this.processID = processID;
	        this.burstTime = burstTime;
	        this.priority = priority;
	        this.memoryRequired = memoryRequired;

	        this.remainingBurst = burstTime;

	        this.waitingTime = 0;
	        this.turnaroundTime = 0;

	        this.startTime = -1;
	        this.terminationTime = -1;

	        this.state = "NEW";
	    }

	    // Getters and Setters

	    public int getProcessID() {
	        return processID;
	    }

	    public int getBurstTime() {
	        return burstTime;
	    }

	    public int getPriority() {
	        return priority;
	    }

	    public int getMemoryRequired() {
	        return memoryRequired;
	    }

	    public int getWaitingTime() {
	        return waitingTime;
	    }

	    public void setWaitingTime(int waitingTime) {
	        this.waitingTime = waitingTime;
	    }

	    public int getTurnaroundTime() {
	        return turnaroundTime;
	    }

	    public void setTurnaroundTime(int turnaroundTime) {
	        this.turnaroundTime = turnaroundTime;
	    }

	    public int getStartTime() {
	        return startTime;
	    }

	    public void setStartTime(int startTime) {
	        this.startTime = startTime;
	    }

	    public int getTerminationTime() {
	        return terminationTime;
	    }

	    public void setTerminationTime(int terminationTime) {
	        this.terminationTime = terminationTime;
	    }

	    public String getState() {
	        return state;
	    }

	    public void setState(String state) {
	        this.state = state;
	    }

	    public int getRemainingBurst() {
	        return remainingBurst;
	    }

	    public void setRemainingBurst(int remainingBurst) {
	        this.remainingBurst = remainingBurst;
	    }

	    public void setPriority(int priority) {
	        this.priority = priority;
	    }
	    
	    @Override
	    public String toString() {
	        return "Process" + processID +
	                " (Burst=" + burstTime +
	                ", Priority=" + priority +
	                ", Memory=" + memoryRequired +
	                "MB)";
	    }
	}
