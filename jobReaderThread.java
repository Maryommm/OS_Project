import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class jobReaderThread extends Thread {

    private JobQueue jobQueue;
    private String fileName;

    public jobReaderThread(JobQueue jobQueue, String fileName) {
        this.jobQueue = jobQueue;
        this.fileName = fileName;
    }

    @Override
    public void run() {

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

            String line;

            while ((line = br.readLine()) != null) {

                String[] firstSplit = line.split(";");

                String[] processData = firstSplit[0].split(":");

                int processID = Integer.parseInt(processData[0]);
                int burstTime = Integer.parseInt(processData[1]);
                int priority = Integer.parseInt(processData[2]);

                int memory = Integer.parseInt(firstSplit[1]);

                // Create PCB
                PCB process = new PCB(
                        processID,
                        burstTime,
                        priority,
                        memory
                );

                process.setState("READY");

                // Add to Job Queue
                jobQueue.addJob(process);

                Thread.sleep(100);
            }

            System.out.println("\nAll processes loaded successfully!");

        } catch (IOException e) {

            System.out.println("Something went wrong while reading the file!");

        } catch (InterruptedException e) {

            System.out.println("!Thread interrupted!");
        }
    }
}
