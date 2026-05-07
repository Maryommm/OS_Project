package OS_Project;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class PriorityScheduler extends Scheduler {

    private HashMap<Integer, Integer> timeInReadyQueue;

    public PriorityScheduler(ReadyQueue readyQueue, JobLoaderThread jobLoader, int totalProcesses) {
        super(readyQueue, jobLoader, totalProcesses);
        timeInReadyQueue = new HashMap<>();
    }

    @Override
    public void schedule() {
        int currentTime = 0;

        while (finishedProcesses.size() < totalProcesses) {
            ArrayList<PCB> availableProcesses = new ArrayList<>(readyQueue.getQueueSnapshot());

            if (availableProcesses.isEmpty()) {
                currentTime++;
                try { Thread.sleep(5); } catch (Exception e) {}
                continue;
            }

            availableProcesses.sort(Comparator
                    .comparingInt(PCB::getPriority)
                    .thenComparingInt(PCB::getProcessID));

            PCB selectedProcess = availableProcesses.get(0);
            readyQueue.removeProcess(selectedProcess);

            if (selectedProcess.getStartTime() == -1) {
                selectedProcess.setStartTime(currentTime);
            }
            selectedProcess.setState("RUNNING");

            int burst = selectedProcess.getBurstTime();
            int startBurst = burst;
            int endTime = currentTime + burst;

            addGanttEntry(selectedProcess, currentTime, endTime, startBurst, 0);
            
            ArrayList<PCB> stillWaiting = new ArrayList<>(readyQueue.getQueueSnapshot());
            int dynamicStarvationLimit = stillWaiting.size() * 5; 

            for (PCB p : stillWaiting) {
                 int currentWait = timeInReadyQueue.getOrDefault(p.getProcessID(), 0);
                 int newWait = currentWait + burst; 
                 int oldAgingCycles = currentWait / 4;
                 int newAgingCycles = newWait / 4;
                 int priorityDecreases = newAgingCycles - oldAgingCycles;
                 
                 
                 if (priorityDecreases > 0) {
                     int newPri = p.getPriority() - priorityDecreases;
                     if (newPri < 1) newPri = 1;
                     p.setPriority(newPri);
                 }
                 
                 if (newWait > dynamicStarvationLimit && !starvedProcesses.contains(p)) {
                     starvedProcesses.add(p);
                 }
                 
                 timeInReadyQueue.put(p.getProcessID(), newWait);
            }

            currentTime = endTime;
            finalizeProcess(selectedProcess, currentTime);
        }
    }
}