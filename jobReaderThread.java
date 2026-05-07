import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class jobReaderThread extends Thread {
    private JobQueue jobQueue;
    private String fileName;

    // Constructor
    public jobReaderThread(JobQueue jobQueue, String fileName) {
        this.jobQueue = jobQueue;
        this.fileName = fileName;
    }

    // This method starts when the thread runs
    @Override
    public void run() {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            
            // Read the file line by line until the end
            while ((line = br.readLine()) != null) {
                String[] firstSplit = line.split(";");
                String[] processData = firstSplit[0].split(":");

                // parse the text from the file into numbers
                int processID = Integer.parseInt(processData[0]);
                int burstTime = Integer.parseInt(processData[1]);
                int priority = Integer.parseInt(processData[2]);
                int memory = Integer.parseInt(firstSplit[1]);

                // Create a new process
                PCB process = new PCB(processID, burstTime, priority, memory);
                process.setState("READY");
                
                // Add the new process to the job queue
                jobQueue.addJob(process);
                
            }
            System.out.println("\nAll processes loaded successfully!");
            
        } catch (IOException e) {
            //if file cannot read properly
            System.out.println("Something went wrong while reading the file!");
        }
    }
}
