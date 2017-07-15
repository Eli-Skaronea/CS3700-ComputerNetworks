/**
 * Eli Skaronea
 * Assignment 7 - Routing Algorithm
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        int numOfRouters = promptUser();
        int matrix[][] = readFile(numOfRouters);

        int dVector[] = new int[numOfRouters];
        for (int i = 0; i < numOfRouters; i++) {
            dVector[i] = matrix[0][i];
        }

        int pVector[] = new int[numOfRouters];
        for (int i = 0; i < numOfRouters; i++) {
            int numCheck = matrix[0][i];
            if (numCheck == 0 || numCheck == 1000000) {
                pVector[i] = -1;
            } else {
                pVector[i] = 0;
            }
        }

        List<String> nPrime = new ArrayList<>();
        nPrime.add("V0");
        List<Integer> checkedIndexes = new ArrayList<>();
        checkedIndexes.add(0);

        List<String> yPrime = new ArrayList<>();

        int numToCheck = dVector.length - 1;
        int count = 1;

        while (numToCheck > 0) {
            int smallestFound = -1;


            int index = 0;
            for (int i = 1; i < numOfRouters; i++) {

                if (!checkedIndexes.contains(i)) {
                    int cost = dVector[i];
                    if ((cost < smallestFound && cost != -1) || smallestFound == -1) {
                        smallestFound = cost;
                        index = i;
                    }
                }
            }
            numToCheck--;
            dVector[index] = smallestFound;
            //pVector[index] = index;
            nPrime.add(", V" + index);
            checkedIndexes.add(index);
            //YPRIME UPDATED
            for (int i = 0; i < numOfRouters; i++) {
                int newLinkCost = smallestFound + matrix[index][i];
                if (newLinkCost < dVector[i] && i != index) {
                    dVector[i] = newLinkCost;
                    pVector[i] = index;
                }
            }

            System.out.println("ITERATION: " + count);
            outputInfo(numOfRouters, dVector, pVector, nPrime, yPrime);
            count++;
        }


    }

    public static void outputInfo(int numOfRouters, int dVector[], int pVector[], List<String> nPrime, List<String> yPrime) {
        System.out.print("D Vector: ");
        for (int i = 0; i < numOfRouters; i++) {
            if (dVector[i] == 1000000) {
                System.out.print("Infinity ");
            } else {
                System.out.print(dVector[i] + " ");
            }
        }
        System.out.println("");

        System.out.print("P Vector: ");
        for (int i = 0; i < numOfRouters; i++) {
            if (pVector[i] == -1) {
                System.out.print("- ");
            } else {
                System.out.print(pVector[i] + " ");
            }

        }
        System.out.println("");

        System.out.print("N Prime: ");
        for (int i = 0; i < nPrime.size(); i++) {
            System.out.print(nPrime.get(i));
        }
        System.out.println("");

        System.out.print("Y Prime: ");
        for (int i = 0; i < yPrime.size(); i++) {
            System.out.print(yPrime.get(i) + ", ");
        }
        System.out.println("");

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

    public static int[][] readFile(int numOfRouters) throws FileNotFoundException {
        int matrix[][] = new int[numOfRouters][numOfRouters];
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
            for (int i = 0; i < numOfRouters; i++) {
                for (int j = 0; j < numOfRouters; j++) {
                    if (i == j) {
                        matrix[i][j] = 0;
                    } else if (matrix[i][j] == 0) {
                        matrix[i][j] = 1000000;
                    }
                }
            }


        }

//        for(int i = 0; i < numOfRouters; i++)
//        {
//            for(int j = 0; j < numOfRouters; j++)
//            {
//                System.out.printf("%5d ", matrix[i][j]);
//            }
//            System.out.println();
//        }

        return matrix;
    }
}
