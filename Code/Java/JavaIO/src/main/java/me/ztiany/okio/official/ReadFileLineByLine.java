package me.ztiany.okio.official;

import okio.BufferedSource;
import okio.Okio;
import okio.Source;

import java.io.File;
import java.io.IOException;

public final class ReadFileLineByLine {

    public void run() throws Exception {
        readLines(new File("build.gradle"));
    }

    public void readLines(File file) throws IOException {
        System.out.println(file.getAbsolutePath());

        try (Source fileSource = Okio.source(file); BufferedSource bufferedFileSource = Okio.buffer(fileSource)) {

            while (true) {
                String line = bufferedFileSource.readUtf8Line();
                if (line == null) {
                    break;
                }
                System.out.println(line);
            }

        }
    }

    public static void main(String... args) throws Exception {
        new ReadFileLineByLine().run();
    }
} 