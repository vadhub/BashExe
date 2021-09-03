package com.vad.bashexe.bashreader;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class Run {

    public static void main(String[] args) {
        System.out.println(runExecute());
    }

    private static String runExecute(){
        Process process;
        String line = "";
        File file = new File("raw/bash_command.sh");

        try {
            process = new ProcessBuilder("/bin/bash", file.getAbsolutePath()).start();
            process.waitFor();
            System.out.println(file.getAbsolutePath());
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            line = reader.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return line;

    }
}
