public class MemoryManager {

    private static final int TOTAL_MEMORY = 2048;
    private int availableMemory;

    public MemoryManager() {
        this.availableMemory = TOTAL_MEMORY;
    }

    // Allocate memory
    public synchronized boolean allocate(int memoryRequired) {
        if (memoryRequired <= availableMemory) {
            availableMemory -= memoryRequired;
            return true;
        }
        return false;
    }

    // Release memory
    public synchronized void release(int memoryRequired) {
        availableMemory = Math.min(availableMemory + memoryRequired, TOTAL_MEMORY);
    }

    public synchronized int getAvailableMemory() {
        return availableMemory;
    }
}
