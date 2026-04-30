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

public class MetricsCalculator {

    public void calculateProcessMetrics(ArrayList<PCB> processes) {
        for (PCB process : processes) {
            int turnaroundTime = process.getTerminationTime();
            int waitingTime = turnaroundTime - process.getBurstTime();

            process.setTurnaroundTime(turnaroundTime);
            process.setWaitingTime(waitingTime);
        }
    }

    public double calculateAverageWaitingTime(ArrayList<PCB> processes) {
        int total = 0;

        for (PCB process : processes) {
            total += process.getWaitingTime();
        }

        return (double) total / processes.size();
    }

    public double calculateAverageTurnaroundTime(ArrayList<PCB> processes) {
        int total = 0;

        for (PCB process : processes) {
            total += process.getTurnaroundTime();
        }

        return (double) total / processes.size();
    }
}
