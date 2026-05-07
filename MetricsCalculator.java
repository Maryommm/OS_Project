package OS_Project;

import java.util.ArrayList;

//calculating Waiting Time, Turnaround Time,Averages// no scheduling, work after sch done
public class MetricsCalculator {
    
//we already have from the file burst and term time but not wait and turn, need computing
    public void calculateProcessMetrics(ArrayList<PCB> processes) {//takes array of p and calculate metrics for each
        for (PCB process : processes) {//process i from class pcb
            int turnaroundTime = process.getTerminationTime();
             //total time from start to finish  //get from pcb class and the setting from scheduler//since all start from 0 just the term time but if not -> Turnaround = Termination Time − Arrival Time 
        
            int waitingTime = turnaroundTime - process.getBurstTime();
               //time spent not running= total time- exe time -----ex P1=25 to run, total time was 58 so 33 waiting 
               
            //save results inside each calculated values
            process.setTurnaroundTime(turnaroundTime);
            process.setWaitingTime(waitingTime);
        }
    }
    
// calcs avg waiting and turn by summing the waiting /turning  of all pros and div by number of p.
    public double calculateAverageWaitingTime(ArrayList<PCB> processes) {
        int total = 0;

        for (PCB process : processes) {
            total += process.getWaitingTime();
        }

        return (double) total / processes.size();//processes is name of the arr
    }

    public double calculateAverageTurnaroundTime(ArrayList<PCB> processes) {
        int total = 0;

        for (PCB process : processes) {
            total += process.getTurnaroundTime();
        }

        return (double) total / processes.size();
    }
}
