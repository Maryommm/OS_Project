/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package os;

/**
 *
 * @author janaa
 */
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        System.out.println("=================================");
        System.out.println(" CPU Scheduling Simulator");
        System.out.println("=================================");

        System.out.println("Choose Scheduling Algorithm:");
        System.out.println("1. Shortest Job First (SJF)");
        System.out.println("2. Round Robin (RR)");
        System.out.println("3. Priority Scheduling");
        System.out.print("Enter your choice: ");

        int choice = input.nextInt();

        switch (choice) {
            case 1:
                System.out.println("\nYou selected Shortest Job First.");
                break;

            case 2:
                System.out.println("\nYou selected Round Robin.");
                break;

            case 3:
                System.out.println("\nYou selected Priority Scheduling.");
                break;

            default:
                System.out.println("\nInvalid choice.");
                return;
        }

        /*
         لاحقًا هنا بنربط:
         - JobReaderThread
         - JobLoaderThread
         - Scheduler
         - OutputManager
        */

        input.close();
    }
}