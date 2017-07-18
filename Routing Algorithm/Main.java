
//Eli Skaronea
//Assignment 7 - Routing Algorithm

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        int numOfRouters = promptUser();
        int matrix[][] = readFile(numOfRouters);
        //The vector containing the shortest amount of jumps to each router from V0 - initialize them with paths from V0
        int dVector[] = new int[numOfRouters];
        for (int i = 0; i < numOfRouters; i++) {
            dVector[i] = matrix[0][i];
        }

        //Vector containing which router to connect to to create the shortest path -1 means no possible connection
        int pVector[] = new int[numOfRouters];
        for (int i = 0; i < numOfRouters; i++) {
            int numCheck = matrix[0][i];
            if (numCheck == 0) {
                pVector[i] = -1;
            }
            else if(numCheck == 1000000){
                pVector[i] = -2;
            }
            else {
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
            if(numToCheck > -1 && numToCheck != numOfRouters - 2){
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

            yPrime.add("(V" + pVector[index] + ", V" + index + ")");

            System.out.println("ITERATION: " + count);
            outputInfo(numOfRouters, dVector, pVector, nPrime, yPrime);
            count++;
        }
        createLinkTable(numOfRouters, pVector);

    }

    public static void outputInfo(int numOfRouters, int dVector[], int pVector[], List<String> nPrime, List<String> yPrime) {
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
            if (pVector[i] == -1) {
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

    private static int promptUser() {
        int numOfRouters = 0;
        Scanner input = new Scanner(System.in);
        while (numOfRouters < 2) {
            System.out.print("Input the number of routers (must be > 2): ");
            numOfRouters = input.nextInt();
        }

        return numOfRouters;
    }

    private static int[][] readFile(int numOfRouters) throws FileNotFoundException {
        int matrix[][] = new int[numOfRouters][numOfRouters];
        try{
            File file = new File("topo.txt");
            Scanner scan = new Scanner(file);
            int row = 0;
            while (scan.hasNextLine()) {
                row++;
                int firstValue = scan.nextInt();
                int secondValue = scan.nextInt();
                int cost = scan.nextInt();

                if (firstValue > numOfRouters - 1 || secondValue > numOfRouters - 1) {
                    System.out.println("There is an invalid data on row " + row);
                    System.err.println("File has data on router not specified by user");
                    scan.close();
                    String filename = "";
                    Scanner input = new Scanner(System.in);
                    while(filename.isEmpty()){
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
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }

        return matrix;
    }

    private static void createLinkTable(int numberOfRouters, int pVector[]){
        System.out.println("Destination               Link");
        for(int i = 1; i < numberOfRouters; i++){
            List<String> path = new ArrayList<>();
            path.clear();
            if(pVector[i] == -1 || pVector[i] == 0){
                path.add("V0");
            }
            else if(pVector[i] == -2){
                path.add("No connections to the node found");
            }
            else{
                int start = pVector[i];
                while(start != 0){

                    path.add("V" + start);
                    start = pVector[start];
                    if(start == 0){
                        path.add("V" + start);
                    }


                }

            }
            String finalPath = stringListToString(path);
            System.out.println("      V" + i + "                  " + finalPath);
        }
    }

    private static String stringListToString(List<String> string){
        String finalString = "(";
        for(int i = string.size(); i > 0; i--){
            finalString = finalString + string.get(i - 1) + ", ";
        }
        finalString = finalString.replaceAll(", $", "");
        return finalString + ")";
    }


}
