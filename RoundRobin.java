
public class RoundRobin extends Scheduler {

    // Time quantum 
    private static final int QUANTUM = 5;

    // Constructor
    public RoundRobin(ReadyQueue readyQueue, JobLoaderThread jobLoader, int totalProcesses) {
        super(readyQueue, jobLoader, totalProcesses);
    }

    @Override
    public void schedule() {
        // Current simulation time
        int currentTime = 0;

        // Until all processes finished
        while (finishedProcesses.size() < totalProcesses) {

            // Remove the first process from the ready queue
            PCB process = readyQueue.removeProcess(); 

            // If there are no ready processes
            if (process == null) {
                currentTime++;
                try {
                    Thread.sleep(5);
                } catch (Exception e) {
                    // Ignore exception
                }
                continue;
            }

           // If process first time to execute, record its start time
            if (process.getStartTime() == -1) {
                process.setStartTime(currentTime);
            }

           // Change process state to running
            process.setState("RUNNING");

            // Determine how long the process will run
            int runTime = Math.min(QUANTUM, process.getRemainingBurst());

            // Save the burst time before execution for Gantt chart
            int startBurst = process.getRemainingBurst();

            // Remaining burst after execution
            int stopBurst = startBurst - runTime;

            // Execution end time
            int endTime = currentTime + runTime;

            // Add the execution to the Gantt chart
            addGanttEntry(process, currentTime, endTime, startBurst, stopBurst);

            // Update the remaining burst after running 
            process.setRemainingBurst(stopBurst);

            // Move time forward
            currentTime = endTime;

            // If process still has burst left, put it back in the ready queue
            if (process.getRemainingBurst() > 0) {
                readyQueue.addProcess(process); 
            } else {
            	
                // If process has finished completely, finalize the process
                finalizeProcess(process, currentTime); 
            }
        }
    }
}