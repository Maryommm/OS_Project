package OS_Project;

public class RoundRobin extends Scheduler {

    private static final int QUANTUM = 5;

    public RoundRobin(ReadyQueue readyQueue, JobLoaderThread jobLoader, int totalProcesses) {
        super(readyQueue, jobLoader, totalProcesses);
    }

    @Override
    public void schedule() {
        int currentTime = 0;

        while (finishedProcesses.size() < totalProcesses) {
            PCB process = readyQueue.removeProcess(); // يسحب أول عملية

            if (process == null) {
                currentTime++;
                try { Thread.sleep(5); } catch (Exception e) {}
                continue;
            }

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
                readyQueue.addProcess(process); 
            } else {
                finalizeProcess(process, currentTime); 
            }
        }
    }
}