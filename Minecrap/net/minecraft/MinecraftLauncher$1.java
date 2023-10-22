/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft;

import magnus.console.OutputConsole;
import java.io.BufferedReader;
import java.io.IOException;

static final class MinecraftLauncher.1
extends Thread {
    BufferedReader reader;
    final /* synthetic */ BufferedReader val$reader1;
    final /* synthetic */ OutputConsole val$console;
    final /* synthetic */ Process val$process;

    MinecraftLauncher.1(BufferedReader bufferedReader, OutputConsole outputConsole, Process process) {
        this.val$reader1 = bufferedReader;
        this.val$console = outputConsole;
        this.val$process = process;
        this.reader = this.val$reader1;
    }

    public void run() {
        boolean terminated = false;
        boolean noerrors = true;
        String output = "";
        this.val$console.acquire();
        while (!terminated && output != null) {
            int exitvalue = Integer.MIN_VALUE;
            try {
                exitvalue = this.val$process.exitValue();
            }
            catch (IllegalThreadStateException ex) {
                try {
                    output = this.reader.readLine();
                    System.err.println(output);
                    this.val$console.appendText("\nError: " + output);
                }
                catch (IOException ex1) {
                    output = null;
                }
            }
            if (exitvalue == Integer.MIN_VALUE) continue;
            terminated = true;
            if (exitvalue == 0) continue;
            noerrors = false;
        }
        if (noerrors) {
            this.val$console.release();
        }
    }
}
