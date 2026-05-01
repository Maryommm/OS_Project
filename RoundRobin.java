package OS_Project;

import java.util.LinkedList;
import java.util.Queue;

public class RoundRobin extends Scheduler {

    private static final int QUANTUM = 5;

    public RoundRobin(ReadyQueue readyQueue) {
        super(readyQueue);
    }

    @Override
    public void schedule() {
        Queue<PCB> queue = new LinkedList<>(readyQueue.getQueueSnapshot());
        int currentTime = 0;

        while (!queue.isEmpty()) {
            PCB process = queue.poll();

            if (process.getStartTime() == -1) {
                process.setStartTime(currentTime);
            }

            process.setState("RUNNING");

            int runTime = Math.min(QUANTUM, process.getRemainingBurst());
            int startBurst = process.getRemainingBurst();
            int stopBurst = startBurst - runTime;
            int endTime = currentTime + runTime;

            addGanttEntry(process, currentTime, endTime, startBurst, stopBurst);

            process.setRemainingBurst(stopBurst);
            currentTime = endTime;

            if (process.getRemainingBurst() > 0) {
                process.setState("READY");
                queue.add(process);
            } else {
                process.setTerminationTime(currentTime);
                process.setState("TERMINATED");
                finishedProcesses.add(process);
            }
        }
    }
}