package io.github.samera2022.mousemacros.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class ConsoleOutputCapturer {
    private static final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private static final PrintStream originalOut = System.out;
    private static final PrintStream originalErr = System.err;

    public static void start() {
        PrintStream ps = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                baos.write(b);
                originalOut.write(b);
            }
        });
        System.setOut(ps);

        PrintStream es = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                baos.write(b);
                originalErr.write(b);
            }
        });
        System.setErr(es);
    }

    public static String getOutput() {
        return baos.toString();
    }
}
