package com.example.phamh.mtracking;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by phamh on 11/2/2017.
 */

public class MyFile {
    public MyFile() {

    }

    public String readFileText(String link) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(link));
            StringBuilder sb = new StringBuilder();
            String line;
            try {
                line = br.readLine();
                while (line != null) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                    line = br.readLine();
                }
                return sb.toString();
            } catch (IOException e) {
                return null;
            }
        } catch (FileNotFoundException e) {
            return null;
        }
    }
}
