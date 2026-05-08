//package OS_project;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class PriorityScheduler extends Scheduler {

     //Store how long each process has spent waiting in the ready queue
     // Key: process ID -> Value: total waiting time in ready queue
    private HashMap<Integer, Integer> timeInReadyQueue;

    // Constructor
    public PriorityScheduler(ReadyQueue readyQueue, JobLoaderThread jobLoader, int totalProcesses) {
        super(readyQueue, jobLoader, totalProcesses);
        timeInReadyQueue = new HashMap<>();
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
                //currentTime++;
                try {
                    Thread.sleep(5);
                } catch (Exception e) {
                    // Ignore exception
                }
                continue;
            }

            // Sort processes by priority then smaller process ID if priority the same
            availableProcesses.sort(Comparator
                    .comparingInt(PCB::getPriority)
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

           // Will run until completion
            int burst = selectedProcess.getBurstTime();

            // Save the burst before execution for Gantt chart
            int startBurst = burst;

            // Execution end time
            int endTime = currentTime + burst;

            // Add the execution to the Gantt chart
            addGanttEntry(selectedProcess, currentTime, endTime, startBurst, 0);
            
            // Remaining processes
            ArrayList<PCB> stillWaiting = new ArrayList<>(readyQueue.getQueueSnapshot());

            // Starvation limit = N * 5 ms
            int dynamicStarvationLimit = stillWaiting.size() * 5; 

           // Update waiting time, aging, and starvation
            for (PCB p : stillWaiting) {

                // Current waiting time already recorded
                int currentWait = timeInReadyQueue.getOrDefault(p.getProcessID(), 0);

                // Add the time spent while the selected process was running
                int newWait = currentWait + burst; 

               // Count how many aging cycles happened before and after this wait update
                int oldAgingCycles = currentWait / 4;
                int newAgingCycles = newWait / 4;

                // How many times aging should be applied in this round
                int priorityDecreases = newAgingCycles - oldAgingCycles;
                
                // Apply aging -> decrease the priority number, but never below 1
                if (priorityDecreases > 0) {
                    int newPri = p.getPriority() - priorityDecreases;
                    if (newPri < 1) newPri = 1;
                    p.setPriority(newPri);
                }
                
                // If waiting time is larger than starvation limit, add process to starved list
                if (newWait > dynamicStarvationLimit && !starvedProcesses.contains(p)) {
                    starvedProcesses.add(p);
                }
                
                // Updated waiting time in the map
                timeInReadyQueue.put(p.getProcessID(), newWait);
            }

           // Move time forward
            currentTime = endTime;

            // Finalize the process
            finalizeProcess(selectedProcess, currentTime);
        }
    }
}