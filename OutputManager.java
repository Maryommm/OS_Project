/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package os;

/**
 *
 * @author janaa
 */
import java.util.ArrayList;

public class OutputManager {

    public static class GanttEntry {
        private int processID;
        private int startTime;
        private int endTime;
        private int startBurst;
        private int stopBurst;

        public GanttEntry(int processID, int startTime, int endTime, int startBurst, int stopBurst) {
            this.processID = processID;
            this.startTime = startTime;
            this.endTime = endTime;
            this.startBurst = startBurst;
            this.stopBurst = stopBurst;
        }

        public int getProcessID() {
            return processID;
        }

        public int getStartTime() {
            return startTime;
        }

        public int getEndTime() {
            return endTime;
        }

        public int getStartBurst() {
            return startBurst;
        }

        public int getStopBurst() {
            return stopBurst;
        }
    }

    public void printGanttChart(ArrayList<GanttEntry> ganttChart) {
        System.out.println("\n========== GANTT CHART ==========");

        if (ganttChart == null || ganttChart.isEmpty()) {
            System.out.println("No Gantt chart data available.");
            return;
        }

        for (GanttEntry entry : ganttChart) {
            System.out.print("| P" + entry.getProcessID() + " ");
        }
        System.out.println("|");

        System.out.print(ganttChart.get(0).getStartTime());

        for (GanttEntry entry : ganttChart) {
            System.out.print("    " + entry.getEndTime());
        }

        System.out.println("\n");

        System.out.println("Detailed Gantt Chart:");
        System.out.println("Process\tStart Time\tEnd Time\tStart Burst\tStop Burst");

        for (GanttEntry entry : ganttChart) {
            System.out.println(
                    "P" + entry.getProcessID() + "\t" +
                    entry.getStartTime() + "\t\t" +
                    entry.getEndTime() + "\t\t" +
                    entry.getStartBurst() + "\t\t" +
                    entry.getStopBurst()
            );
        }
    }

    public void printProcessTable(ArrayList<PCB> processes) {
        System.out.println("\n========== RESULT TABLE ==========");
        System.out.println("PID\tBurst\tStart\tTermination\tWaiting\tTurnaround");

        for (PCB process : processes) {
            System.out.println(
                    "P" + process.getProcessID() + "\t" +
                    process.getBurstTime() + "\t" +
                    process.getStartTime() + "\t" +
                    process.getTerminationTime() + "\t\t" +
                    process.getWaitingTime() + "\t" +
                    process.getTurnaroundTime()
            );
        }
    }

    public void printPerformanceMetrics(double avgWaiting, double avgTurnaround) {
        System.out.println("\n========== PERFORMANCE METRICS ==========");
        System.out.printf("Average Waiting Time: %.2f ms%n", avgWaiting);
        System.out.printf("Average Turnaround Time: %.2f ms%n", avgTurnaround);
    }

    public void printStarvedProcesses(ArrayList<PCB> starvedProcesses) {
        System.out.println("\n========== STARVED PROCESSES ==========");

        if (starvedProcesses == null || starvedProcesses.isEmpty()) {
            System.out.println("No process suffered from starvation.");
            return;
        }

        System.out.print("Processes that suffered from starvation: ");

        for (PCB process : starvedProcesses) {
            System.out.print("P" + process.getProcessID() + " ");
        }

        System.out.println();
    }

    public void printFinalOutput(
            ArrayList<PCB> processes,
            ArrayList<GanttEntry> ganttChart,
            ArrayList<PCB> starvedProcesses,
            boolean isPriority
    ) {
        MetricsCalculator calculator = new MetricsCalculator();

        calculator.calculateProcessMetrics(processes);

        printGanttChart(ganttChart);
        printProcessTable(processes);

        double avgWaiting = calculator.calculateAverageWaitingTime(processes);
        double avgTurnaround = calculator.calculateAverageTurnaroundTime(processes);

        printPerformanceMetrics(avgWaiting, avgTurnaround);

        if (isPriority) {
            printStarvedProcesses(starvedProcesses);
        }
    }
}