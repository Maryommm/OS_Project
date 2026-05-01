package OS_Project;

import java.util.ArrayList;
import java.util.Comparator;

public class SJF extends Scheduler {

    public SJF(ReadyQueue readyQueue) {
        super(readyQueue);
    }

    @Override
    public void schedule() {
        ArrayList<PCB> processes = new ArrayList<>(readyQueue.getQueueSnapshot());

        processes.sort(Comparator
                .comparingInt(PCB::getBurstTime)
                .thenComparingInt(PCB::getProcessID));

        int currentTime = 0;

        for (PCB process : processes) {
            process.setState("RUNNING");
            process.setStartTime(currentTime);

            int startBurst = process.getBurstTime();
            int endTime = currentTime + process.getBurstTime();

            addGanttEntry(process, currentTime, endTime, startBurst, 0);

            process.setTerminationTime(endTime);
            finalizeProcess(process, endTime);

            currentTime = endTime;
        }
    }
}