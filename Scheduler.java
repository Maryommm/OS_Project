import java.util.ArrayList;

//Abstract base class for all CPU schedulers. used by SJF, RoundRobin, and PriorityScheduler

public abstract class Scheduler {

    // Ready queue that contains processes ready for CPU execution
    protected ReadyQueue readyQueue;

    // Job loader thread, used to notify it when a process completes its execution
    protected JobLoaderThread jobLoader;

    // Total number of processes
    protected int totalProcesses;

    // All processes that finished execution
    protected ArrayList<PCB> finishedProcesses;

    // Store Gantt charts
    protected ArrayList<OutputManager.GanttEntry> ganttChart;

    // Store processes that suffered from starvation
    protected ArrayList<PCB> starvedProcesses;

    // Constructor
    public Scheduler(ReadyQueue readyQueue, JobLoaderThread jobLoader, int totalProcesses) {
        this.readyQueue = readyQueue;
        this.jobLoader = jobLoader;
        this.totalProcesses = totalProcesses;
        this.finishedProcesses = new ArrayList<>();
        this.ganttChart = new ArrayList<>();
        this.starvedProcesses = new ArrayList<>();
    }

    //Abstract method that must be implemented by each scheduling class
    public abstract void schedule();

    // Return finished processes
    public ArrayList<PCB> getFinishedProcesses() {
        return finishedProcesses;
    }

    // Return Gantt charts
    public ArrayList<OutputManager.GanttEntry> getGanttChart() {
        return ganttChart;
    }

    // Return starved processes
    public ArrayList<PCB> getStarvedProcesses() {
        return starvedProcesses;
    }

    //Adds one execution segment to the Gantt chart
    protected void addGanttEntry(PCB process, int startTime, int endTime, int startBurst, int stopBurst) {
        ganttChart.add(
            new OutputManager.GanttEntry(
                process.getProcessID(),
                startTime,
                endTime,
                startBurst,
                stopBurst
            )
        );
    }

     //Mark process as terminated,  store it in the finished list
    protected void finalizeProcess(PCB process, int currentTime) {
        process.setTerminationTime(currentTime);
        process.setState("TERMINATED");
        finishedProcesses.add(process);

        // Notify job loader thread that this process has finished
        jobLoader.processCompleted(process);
    }

    //Calculates the waiting time for a process with the assumption that processes arrive at time 0
    protected int calculateWaitingTime(PCB process) {
        return process.getTerminationTime() - process.getBurstTime();
    }

    //Checks whether at least one process has already finished
    protected boolean hasFinishedProcesses() {
        return !finishedProcesses.isEmpty();
    }
}