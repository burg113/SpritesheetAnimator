package com.burg113;

/*  todo:
        cleanup code
        test for bugs
        output important exceptions in program
        cleanup UI
 */


public class Main {

    public static void main(String[] args) {
        new Program("test");

        try {
            ProgramLoop.loop();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
