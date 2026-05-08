package OS_Project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        String fileName = "job.txt";
        int totalProcesses = 0;
//read file
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            while (br.readLine() != null) {
                totalProcesses++;
            }
        } catch (IOException e) {
            System.out.println("Error: Could not read " + fileName + ". Make sure the file exists.");
            return;
        }

        if (totalProcesses == 0) {
            System.out.println("The job file is empty. Exiting...");
            return;
        }

        JobQueue jobQueue = new JobQueue();
        ReadyQueue readyQueue = new ReadyQueue();
        MemoryManager memoryManager = new MemoryManager();

        Scanner input = new Scanner(System.in);
        System.out.println("---------------------------------");
        System.out.println("    CPU Scheduling Simulator");
        System.out.println("----------------------------------");
        System.out.println("Choose Scheduling Algorithm:");
        System.out.println("1. Shortest Job First (SJF)");
        System.out.println("2. Round Robin (RR)");
        System.out.println("3. Priority Scheduling");
        System.out.print("Enter your choice: ");

        int choice = input.nextInt();
        boolean isPriority = false;//switch to T if select case3 the priority

        
        jobReaderThread readerThread = new jobReaderThread(jobQueue, fileName);//read from job txt and turn into pcb obj and add to jobQueue
        JobLoaderThread loaderThread = new JobLoaderThread(jobQueue, readyQueue, memoryManager, totalProcesses);//from job to ready, if enough mem

        readerThread.start();
        loaderThread.start();

        try {
            Thread.sleep(50);//main wait so that Ps can enter ready queue
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

         Scheduler scheduler = null;//variable that can later hold: 1 2 or 3

        switch (choice) {
            case 1:
                System.out.println("\nExecuting Shortest Job First (SJF)...");
                scheduler = new SJF(readyQueue, loaderThread, totalProcesses);
                break;
            case 2:
                System.out.println("\nExecuting Round Robin (RR)...");
                scheduler = new RoundRobin(readyQueue, loaderThread, totalProcesses);
                break;
            case 3:
                System.out.println("\nExecuting Priority Scheduling...");
                scheduler = new PriorityScheduler(readyQueue, loaderThread, totalProcesses);
                isPriority = true;
                break;
            default:
                System.out.println("\nInvalid choice. Exiting...");
                System.exit(0);
        }
          //only create obj but actuall running from scheduler
        scheduler.schedule();

        try {
            loaderThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Sends the final results to output manager
        
        OutputManager outputManager = new OutputManager();
        outputManager.printFinalOutput(
                scheduler.getFinishedProcesses(),
                scheduler.getGanttChart(),
                scheduler.getStarvedProcesses(),
                isPriority
        );

        input.close();
    }
}