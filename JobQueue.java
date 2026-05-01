

import java.util.LinkedList;
import java.util.Queue;

public class JobQueue {

    private Queue<PCB> queue;

    public JobQueue() {
        queue = new LinkedList<>();
    }
    
    // Add process
    public synchronized void addJob(PCB process) {
        queue.add(process);
        System.out.println(process + " added to Job Queue");
    }

    // Remove process
    public synchronized PCB removeJob() {
        return queue.poll();
    }

     // Check without removing
    public synchronized PCB peekJob() {
        return queue.peek();
    }

    // Check empty
    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }

    // Queue size
    public synchronized int size() {
        return queue.size();
    }
}
