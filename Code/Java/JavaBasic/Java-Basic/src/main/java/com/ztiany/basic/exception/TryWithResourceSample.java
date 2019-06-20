package com.ztiany.basic.exception;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class TryWithResourceSample {

    public static void main(String... args) {
        try (Scanner scanner = new Scanner(new FileInputStream("a.txt"))) {
            String text;
            int line = 0;
            while (scanner.hasNext() ) {
                text = scanner.nextLine();
                System.out.println("line :" + line++ + " " + text);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }



}
