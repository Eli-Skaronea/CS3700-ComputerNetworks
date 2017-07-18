
//Eli Skaronea
//Assignment 7 - Routing Algorithm

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        /*
          numOfRouters - number of routers in the map, input by user
          matrix - a matrix created from .txt document
          dVector - vector containing the shortest cost to each router from V0 - initialize them with paths from V0
          pVector - Vector containing which router to connect to to create the shortest path -2 means no possible connection,
                    -1 means it is connecting to itself
         */
        int numOfRouters = promptUser();
        int matrix[][] = readFile(numOfRouters);
        int dVector[] = new int[numOfRouters];
        System.arraycopy(matrix[0], 0, dVector, 0, numOfRouters);

        int pVector[] = new int[numOfRouters];
        for (int i = 0; i < numOfRouters; i++) {
            int numCheck = matrix[0][i];
            if (numCheck == 0) {
                pVector[i] = -1;
            } else if (numCheck == 1000000) {
                pVector[i] = -2;
            } else {
                pVector[i] = 0;
            }
        }
        /*
          nPrime - a list of strings containing the names of the nodes where the shortest path has been found, initialized with VO the source node
          yPrime - a list of strings containing the names of the node that connects to the final node at the same index of nPrime
          checkedIndexes - a list of integers containing the index's of nPrime that have been checked, initialized with 0 for index 0.
         */
        List<String> nPrime = new ArrayList<>();
        nPrime.add("V0");

        List<String> yPrime = new ArrayList<>();

        findShortestPath(numOfRouters, dVector, pVector, nPrime, yPrime, matrix);
        createLinkTable(numOfRouters, pVector);

    }

    /**
     * Outputs the relevant information after every iteration of the loop - a new nPrime will be added, along with it's shortest path found.
     *
     * @param numOfRouters - number of routers in the map
     * @param dVector      - current smallest cost to that index's router from V0
     * @param pVector      - current best router to get to before hitting the destination router of nPrime with same index
     * @param nPrime       - All the routers where shortest path has been found
     * @param yPrime       - The second to last, and last node to connect to the destination
     */
    private static void outputInfo(int numOfRouters, int dVector[], int pVector[], List<String> nPrime, List<String> yPrime) {
        System.out.print("D(i): ");
        for (int i = 0; i < numOfRouters; i++) {
            if (dVector[i] == 1000000) {
                System.out.print("Infinity ");
            } else {
                System.out.print(dVector[i] + " ");
            }
        }
        System.out.println("");

        System.out.print("P(i): ");
        for (int i = 0; i < numOfRouters; i++) {
            if (pVector[i] == -1 || pVector[i] == -2) {
                System.out.print("- ");
            } else {
                System.out.print(pVector[i] + " ");
            }

        }
        System.out.println("");

        System.out.print("N': ");
        for (String outNPrime : nPrime) {
            System.out.print(outNPrime);
        }
        System.out.println("");

        System.out.print("Y': ");
        for (String outYPrime : yPrime) {
            System.out.print(outYPrime);
        }
        System.out.println("");

    }

    /**
     * Ask the user for the number of routers in the map
     *
     * @return number of users in the map
     */
    private static int promptUser() {
        int numOfRouters = 0;
        Scanner input = new Scanner(System.in);
        while (numOfRouters < 2) {
            System.out.print("Input the number of routers (must be > 2): ");
            numOfRouters = input.nextInt();
        }

        return numOfRouters;
    }

    /**
     * Read the file given and create a matrix with the router to router cost data. It is assumed, the first two integers
     * on a line are router id's and the third number is the cost to link them
     *
     * @param numOfRouters - number of routers in the map
     * @return a matrix containing all router to router cost info, the data will always be a square matrix
     * @throws FileNotFoundException - Thrown if file is not found
     */
    private static int[][] readFile(int numOfRouters) throws FileNotFoundException {
        int matrix[][] = new int[numOfRouters][numOfRouters];
        try {
            File file = new File("topo.txt");
            Scanner scan = new Scanner(file);
            int row = 0;
            while (scan.hasNextLine()) {
                row++;
                int firstValue = scan.nextInt();
                int secondValue = scan.nextInt();
                int cost = scan.nextInt();
                //If the router number is greater than number of routers specified, throw an error asking to edit or submit correct data file
                if (firstValue > numOfRouters - 1 || secondValue > numOfRouters - 1) {
                    System.out.println("There is an invalid data on row " + row);
                    System.err.println("File has data on router not specified by user");
                    scan.close();
                    String filename = "";
                    Scanner input = new Scanner(System.in);
                    while (filename.isEmpty()) {
                        System.out.print("Please enter the name of the correct file or make changes and re-enter name of file: ");
                        filename = input.nextLine();
                        file = new File(filename);
                        scan = new Scanner(file);
                        row = 0;
                    }

                } else {
                    matrix[firstValue][secondValue] = cost;
                    matrix[secondValue][firstValue] = cost;
                }
                //If done reading the file, fill in empty spaces with a 0, and 0's with infinity (meaning not conencted)
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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return matrix;
    }

    /**
     * Create a table that displays the destination and path to route to the destination
     *
     * @param numberOfRouters - number of routers in the map
     * @param pVector         - previous node to connect to final destination
     */
    private static void createLinkTable(int numberOfRouters, int pVector[]) {
        System.out.println("Destination               Link");
        for (int i = 1; i < numberOfRouters; i++) {
            List<String> path = new ArrayList<>();
            path.clear();
            if (pVector[i] == -1 || pVector[i] == 0) {
                path.add("V0");
            } else if (pVector[i] == -2) {
                path.add("No connections to the node found");
            } else {
                int start = pVector[i];
                while (start != 0) {

                    path.add("V" + start);
                    start = pVector[start];
                    if (start == 0) {
                        path.add("V" + start);
                    }


                }

            }
            String finalPath = stringListToString(path);
            System.out.println("      V" + i + "                  " + finalPath);
        }
    }

    /**
     * Formats the link output by reversing the input, which outputs the sequential order to connect to
     *
     * @param string - a list of strings to reverse
     * @return a reversed string concattenated from the list of strings inputted.
     */
    private static String stringListToString(List<String> string) {
        StringBuilder finalString = new StringBuilder("(");
        for (int i = string.size(); i > 0; i--) {
            finalString.append(string.get(i - 1)).append(", ");
        }
        finalString = new StringBuilder(finalString.toString().replaceAll(", $", ""));
        return finalString + ")";
    }

    /**
     * Given the data input by the user, find the shortest path to each node from the source V0. Uses Djikstra's algorithm
     *
     * @param numOfRouters - number of routers in the map
     * @param dVector      - the vector containing the smallest cost to each router corresponding to that index.
     * @param pVector      - the last node with the best cost to connect to before reaching the destination
     * @param nPrime       - List of the nodes where the shortest path has been found
     * @param yPrime       - List of the path to reach the destination corresponding to nPrime's same index.
     * @param matrix       - Matrix given by the user showing router to router cost.
     */
    private static void findShortestPath(int numOfRouters, int dVector[], int pVector[], List<String> nPrime, List<String> yPrime, int matrix[][]) {
        //checkedIndexes - List of index's that have already been checked from dVector.
        List<Integer> checkedIndexes = new ArrayList<>();
        checkedIndexes.add(0);
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
            if (numToCheck > -1 && numToCheck != numOfRouters - 2) {
                yPrime.add(", ");
            }
            dVector[index] = smallestFound;
            nPrime.add(", V" + index);
            checkedIndexes.add(index);

            for (int i = 0; i < numOfRouters; i++) {
                int newLinkCost = smallestFound + matrix[index][i];
                if (newLinkCost < dVector[i] && i != index) {
                    dVector[i] = newLinkCost;
                    pVector[i] = index;
                }
            }
            if (pVector[index] != -2) {
                yPrime.add("(V" + pVector[index] + ", V" + index + ")");
            }
            else{
                yPrime.add("(No Connection)");
            }
            System.out.println("ITERATION: " + count);
            outputInfo(numOfRouters, dVector, pVector, nPrime, yPrime);
            count++;
        }
    }

}
