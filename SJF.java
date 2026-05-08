
import java.util.ArrayList;
import java.util.Comparator;

public class SJF extends Scheduler {

    public SJF(ReadyQueue readyQueue, JobLoaderThread jobLoader, int totalProcesses) {
        super(readyQueue, jobLoader, totalProcesses);
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
                    .comparingInt(PCB::getBurstTime)
                    .thenComparingInt(PCB::getProcessID));

            PCB selectedProcess = availableProcesses.get(0);
            readyQueue.removeProcess(selectedProcess); 
            if (selectedProcess.getStartTime() == -1) {
                selectedProcess.setStartTime(currentTime);
            }
            selectedProcess.setState("RUNNING");

            int startBurst = selectedProcess.getBurstTime();
            int endTime = currentTime + selectedProcess.getBurstTime();

            addGanttEntry(selectedProcess, currentTime, endTime, startBurst, 0);

            currentTime = endTime;
            finalizeProcess(selectedProcess, currentTime);
        }
    }
}