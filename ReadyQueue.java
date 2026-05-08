import java.util.LinkedList;
import java.util.Queue;

public class ReadyQueue {

    private Queue<PCB> queue;

    public ReadyQueue() {
        queue = new LinkedList<>();
    }

    // add process to ready queue
    public synchronized void addProcess(PCB process) {
        process.setState("READY");
        queue.add(process);
    }
    
    // remove first process
    public synchronized PCB removeProcess() {
        return queue.poll();
    }
    
    // remove a specific process
    public synchronized boolean removeProcess(PCB process) {
        return queue.remove(process);
    }

    // check if empty
    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
    
    // queue size
    public synchronized int size() {
        return queue.size();
    }

     // return a copy of queue
    public synchronized Queue<PCB> getQueueSnapshot() {
        return new LinkedList<>(queue);
    }
} 
