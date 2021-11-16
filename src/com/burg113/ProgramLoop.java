package com.burg113;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

// the class housing the program loop
public class ProgramLoop {
    static boolean interrupt;
    static float frameRate = 60;

    public static ArrayList<Executable> executables = new ArrayList<>();

    public static void loop() throws InterruptedException {
        while(!interrupt) {
            for (Executable e : executables) {
                e.execute();
            }
            TimeUnit.NANOSECONDS.sleep((int) (1000000000 / frameRate));
        }
    }

}
