

import java.util.LinkedList;
import java.util.Queue;

public class ReadyQueue {

    private Queue<PCB> queue;

    public ReadyQueue() {
        queue = new LinkedList<>();
    }

    // Add process 
    public synchronized void addProcess(PCB process) {
        process.setState("READY");
        queue.add(process);
    }

    // Remove process
    public synchronized PCB removeProcess() {
        return queue.poll();
    }

    // Check empty
    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }

    // Queue size
    public synchronized int size() {
        return queue.size();
    }

    // Return a copy
    public synchronized Queue<PCB> getQueueSnapshot() {
        return new LinkedList<>(queue);
    }
}
