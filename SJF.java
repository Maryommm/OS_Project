import java.util.ArrayList;
import java.util.Comparator;

/
public class SJF extends Scheduler {

    // Constructor:
    public SJF(ReadyQueue readyQueue, JobLoaderThread jobLoader, int totalProcesses) {
        super(readyQueue, jobLoader, totalProcesses);
    }

    @Override
    public void schedule() {
        // Current simulation time
        int currentTime = 0;

        // Until all processes finished
        while (finishedProcesses.size() < totalProcesses) {

            // Snapshot copy of all processes currently in the ready queue
            ArrayList<PCB> availableProcesses = new ArrayList<>(readyQueue.getQueueSnapshot());

            // If there are no ready processes
            if (availableProcesses.isEmpty()) {
                currentTime++;
                try {
                    Thread.sleep(5);
                } catch (Exception e) {
                    // Ignoring exception 
                }
                continue;
            }

            // Sort processes by shortest burst time first then by process ID if burst times are equal
            availableProcesses.sort(Comparator
                    .comparingInt(PCB::getBurstTime)
                    .thenComparingInt(PCB::getProcessID));

            // Select the first process in the sorted list 
            PCB selectedProcess = availableProcesses.get(0);

            // Remove the selected process from the ready queue
            readyQueue.removeProcess(selectedProcess);

            // If process first time to execute, record its start time
            if (selectedProcess.getStartTime() == -1) {
                selectedProcess.setStartTime(currentTime);
            }

            // Change process state to running
            selectedProcess.setState("RUNNING");

            // Save the burst time before execution for Gantt chart
            int startBurst = selectedProcess.getBurstTime();

            // Run until completion
            int endTime = currentTime + selectedProcess.getBurstTime();

            // Add the execution to the Gantt chart
            addGanttEntry(selectedProcess, currentTime, endTime, startBurst, 0);

            // Move time forward 
            currentTime = endTime;

            // Finalize the process
            finalizeProcess(selectedProcess, currentTime);
        }
    }
}