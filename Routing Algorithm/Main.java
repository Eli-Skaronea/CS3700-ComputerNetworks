/**
 * Eli Skaronea
 * Assignment 7 - Routing Algortithm
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        int numOfRouters = promptUser();
        int matrix[][] = new int[numOfRouters][numOfRouters];
        int nPrime[] = new int[numOfRouters];
        nPrime[0] = 0;
        int yPrime[] = new int[numOfRouters];

        Scanner scan = new Scanner(new File("C:\\Users\\Eli S\\IdeaProjects\\RoutingAlgorithm\\src\\topo.txt"));
        int row = 0;
        while (scan.hasNextLine()) {
            row++;
            int firstValue = scan.nextInt();
            int secondValue = scan.nextInt();
            int cost = scan.nextInt();
            if (firstValue > numOfRouters - 1 || secondValue > numOfRouters - 1) {
                System.out.println("There is an invalid router number on row " + row);
                System.err.println("File has data on router not specified by user");
                break;
            } else {
                matrix[firstValue][secondValue] = cost;
                matrix[secondValue][firstValue] = cost;
            }


        }

        for(int i = 0; i < numOfRouters; i++)
        {
            for(int j = 0; j < numOfRouters; j++)
            {
                System.out.printf("%5d ", matrix[i][j]);
            }
            System.out.println();
        }


    }

    public static int promptUser() {
        int numOfRouters = 0;
        Scanner input = new Scanner(System.in);
        while (numOfRouters < 2) {
            System.out.print("Input the number of routers (must be > 2): ");
            numOfRouters = input.nextInt();
        }

        return numOfRouters;
    }
}
