package com.hackaton.meow.domain.utils;

public class Logger {
    public static void displayInfo(String message){
        final String BLUE = "\u001B[34m";
        final String RESET = "\u001B[0m";
        System.out.println(BLUE + "[Info] --- " + message + RESET);
    }

    public static void displayError(String message){
        final String RED = "\u001B[31m";
        final String RESET = "\u001B[0m";
        System.out.println(RED + "[Error] --- " + message + RESET);
    }
}
