package OS_Project;

import java.util.ArrayList;

public abstract class Scheduler {
    protected ReadyQueue readyQueue;
    protected JobLoaderThread jobLoader;
    protected int totalProcesses;
    
    protected ArrayList<PCB> finishedProcesses;
    protected ArrayList<OutputManager.GanttEntry> ganttChart;
    protected ArrayList<PCB> starvedProcesses;

    public Scheduler(ReadyQueue readyQueue, JobLoaderThread jobLoader, int totalProcesses) {
        this.readyQueue = readyQueue;
        this.jobLoader = jobLoader;
        this.totalProcesses = totalProcesses;
        this.finishedProcesses = new ArrayList<>();
        this.ganttChart = new ArrayList<>();
        this.starvedProcesses = new ArrayList<>();
    }

    public abstract void schedule();

    public ArrayList<PCB> getFinishedProcesses() { return finishedProcesses; }
    public ArrayList<OutputManager.GanttEntry> getGanttChart() { return ganttChart; }
    public ArrayList<PCB> getStarvedProcesses() { return starvedProcesses; }

    protected void addGanttEntry(PCB process, int startTime, int endTime, int startBurst, int stopBurst) {
        ganttChart.add(new OutputManager.GanttEntry(process.getProcessID(), startTime, endTime, startBurst, stopBurst));
    }

    protected void finalizeProcess(PCB process, int currentTime) {
        process.setTerminationTime(currentTime);
        process.setState("TERMINATED");
        finishedProcesses.add(process);
        
        jobLoader.processCompleted(process);
    }

    protected int calculateWaitingTime(PCB process) {
        return process.getTerminationTime() - process.getBurstTime();
    }

    protected boolean hasFinishedProcesses() {
        return !finishedProcesses.isEmpty();
    }
}