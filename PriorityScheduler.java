package OS_Project;

import java.util.ArrayList;
import java.util.Comparator;

public class PriorityScheduler extends Scheduler {

    private ArrayList<Integer> arrivalTimes;
    private ArrayList<Integer> lastAgingTimes;
    private ArrayList<Boolean> completed;
    private ArrayList<Integer> dispatchTimes;

    public PriorityScheduler(ReadyQueue readyQueue) {
        super(readyQueue);
        this.arrivalTimes = new ArrayList<>();
        this.lastAgingTimes = new ArrayList<>();
        this.completed = new ArrayList<>();
        this.dispatchTimes = new ArrayList<>();
    }

    @Override
    public void schedule() {
        ArrayList<PCB> processes = new ArrayList<>(readyQueue.getQueueSnapshot());
        int n = processes.size();

        for (int i = 0; i < n; i++) {
            arrivalTimes.add(0);
            lastAgingTimes.add(0);
            completed.add(false);
            dispatchTimes.add(-1);
        }

        int currentTime = 0;
        int finishedCount = 0;

        while (finishedCount < n) {
            ArrayList<Integer> readyIndices = getReadyIndices(processes);

            if (readyIndices.isEmpty()) {
                currentTime++;
                continue;
            }

            applyAging(processes, readyIndices, currentTime);

            readyIndices = getReadyIndices(processes);

            int selectedIndex = selectHighestPriority(processes, readyIndices);

            if (selectedIndex == -1) {
                currentTime++;
                continue;
            }

            PCB selected = processes.get(selectedIndex);

            if (selected.getStartTime() == -1) {
                selected.setStartTime(currentTime);
            }

            if (dispatchTimes.get(selectedIndex) == -1) {
                dispatchTimes.set(selectedIndex, currentTime);
            }

            selected.setState("RUNNING");

            int burst = selected.getBurstTime();
            int startBurst = burst;
            int endTime = currentTime + burst;

            addGanttEntry(selected, currentTime, endTime, startBurst, 0);

            selected.setTerminationTime(endTime);
            selected.setState("TERMINATED");
            finishedProcesses.add(selected);

            markStarvedProcesses(processes, readyIndices, selectedIndex, currentTime);

            completed.set(selectedIndex, true);
            finishedCount++;
            currentTime = endTime;
        }

        for (int i = 0; i < n; i++) {
            PCB p = processes.get(i);
            if (p.getStartTime() == -1) {
                p.setStartTime(arrivalTimes.get(i));
            }
        }
    }

    private ArrayList<Integer> getReadyIndices(ArrayList<PCB> processes) {
        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = 0; i < processes.size(); i++) {
            if (!completed.get(i)) {
                indices.add(i);
            }
        }
        return indices;
    }

    private void applyAging(ArrayList<PCB> processes, ArrayList<Integer> readyIndices, int currentTime) {
        for (int index : readyIndices) {
            PCB p = processes.get(index);

            if (p.getStartTime() == -1) {
                int waitingTime = currentTime - arrivalTimes.get(index);
                int elapsedSinceLastAging = currentTime - lastAgingTimes.get(index);

                if (waitingTime > 0 && elapsedSinceLastAging >= 4) {
                    int agingSteps = elapsedSinceLastAging / 4;
                    if (agingSteps > 0) {
                        int newPriority = p.getPriority() - agingSteps;
                        if (newPriority < 1) {
                            newPriority = 1;
                        }
                        p.setPriority(newPriority);
                        lastAgingTimes.set(index, currentTime);
                    }
                }
            }
        }
    }

    private int selectHighestPriority(ArrayList<PCB> processes, ArrayList<Integer> readyIndices) {
        return readyIndices.stream()
                .min(Comparator
                        .comparingInt((Integer i) -> processes.get(i).getPriority())
                        .thenComparingInt(i -> processes.get(i).getProcessID()))
                .orElse(-1);
    }

    private void markStarvedProcesses(ArrayList<PCB> processes,
                                      ArrayList<Integer> readyIndices,
                                      int selectedIndex,
                                      int currentTime) {
        int n = processes.size();
        int starvationLimit = n * 5;

        for (int index : readyIndices) {
            if (index == selectedIndex) {
                continue;
            }

            PCB p = processes.get(index);

            if (p.getStartTime() == -1) {
                int waitingTime = currentTime - arrivalTimes.get(index);
                if (waitingTime > starvationLimit && !starvedProcesses.contains(p)) {
                    starvedProcesses.add(p);
                }
            }
        }
    }
}