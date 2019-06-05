package me.ztiany.okio.official;

import okio.BufferedSink;
import okio.Okio;
import okio.Sink;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public final class WriteFile {

    public void run() throws Exception {
        writeEnv(new File("env.txt"));
    }

    public void writeEnv(File file) throws IOException {

        try (Sink fileSink = Okio.sink(file); BufferedSink bufferedSink = Okio.buffer(fileSink)) {

            for (Map.Entry<String, String> entry : System.getenv().entrySet()) {
                bufferedSink.writeUtf8(entry.getKey());
                bufferedSink.writeUtf8("=");
                bufferedSink.writeUtf8(entry.getValue());
                bufferedSink.writeUtf8("\n");
            }

        }
    }

    public static void main(String... args) throws Exception {
        new WriteFile().run();
    }
} 