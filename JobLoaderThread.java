public class JobLoaderThread extends Thread {

    private JobQueue jobQueue;
    private ReadyQueue readyQueue;
    private MemoryManager memoryManager;
    private int totalProcesses;
    private volatile int completedProcesses;

    public JobLoaderThread(JobQueue jobQueue, ReadyQueue readyQueue,
                           MemoryManager memoryManager, int totalProcesses) {
        this.jobQueue        = jobQueue;
        this.readyQueue      = readyQueue;
        this.memoryManager   = memoryManager;
        this.totalProcesses  = totalProcesses;
        this.completedProcesses = 0;
    }

    @Override
    public void run() {
        System.out.println("[Thread 2] JobLoaderThread started.");

        //  Running until processes finish
        while (completedProcesses < totalProcesses) {

            if (!jobQueue.isEmpty()) {

                // Check without removing
                PCB process = jobQueue.peekJob();

                if (process != null) {

                    // Allocate memory
                    if (memoryManager.allocate(process.getMemoryRequired())) {

                        // remove and move to ready queue
                        jobQueue.removeJob();
                        process.setState("READY");
                        readyQueue.addProcess(process);

                    } else {

                        //  If not enough memory
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }

            } else {
                //  If queue is empty
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    
    public synchronized void processCompleted(PCB process) {
        memoryManager.release(process.getMemoryRequired());
        completedProcesses++;
    }
}
