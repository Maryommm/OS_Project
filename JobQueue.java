import java.util.LinkedList;
import java.util.Queue;

public class JobQueue {

    private Queue<PCB> queue;

    // make an empty queue
    public JobQueue() {
        queue = new LinkedList<>();
    }
    
    // Add a new process to w.queue
    public synchronized void addJob(PCB process) {
        queue.add(process);
        System.out.println(process + " added to Job Queue");
    }

    // Take out the first process from the queue
    public synchronized PCB removeJob() {
        return queue.poll();
    }

    // Look at the first process without taking it out
    public synchronized PCB peekJob() {
        return queue.peek();
    }

    // Check if queue empty
    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }

    // Count how many jobs in queue
    public synchronized int size() {
        return queue.size();
    }
}
