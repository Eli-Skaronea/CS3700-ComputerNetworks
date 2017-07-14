package com.company;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        int numOfRouters = promptUser();
        ArrayList<String> routerList = new ArrayList<>();


    }

    public static int promptUser(){
        int numOfRouters = 0;
        Scanner input = new Scanner(System.in);
        while(numOfRouters < 2){
            System.out.println("Input the number of routers (must be >2): ");
            numOfRouters = input.nextInt();
        }

        return numOfRouters;
    }
}
