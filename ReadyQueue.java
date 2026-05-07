package OS_Project;

import java.util.LinkedList;
import java.util.Queue;

public class ReadyQueue {

    private Queue<PCB> queue;

    public ReadyQueue() {
        queue = new LinkedList<>();
    }

    public synchronized void addProcess(PCB process) {
        process.setState("READY");
        queue.add(process);
    }

    public synchronized PCB removeProcess() {
        return queue.poll();
    }

    public synchronized boolean removeProcess(PCB process) {
        return queue.remove(process);
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }

    public synchronized int size() {
        return queue.size();
    }

    public synchronized Queue<PCB> getQueueSnapshot() {
        return new LinkedList<>(queue);
    }
}