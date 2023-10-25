// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft;

import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import magnus.console.OutputConsole;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class MinecraftLauncher
{
    private static final long MIN_HEAP = 511L;
    private static final long RECOMMENDED_HEAP = 1024L;
    private static boolean debugMode;
    
    public static void main(final String[] args) throws Exception {
        if (args.length > 0 && args[0].contains("debug")) {
            MinecraftLauncher.debugMode = true;
        }
        final long heapSizeMegs = Runtime.getRuntime().maxMemory() / 1024L / 1024L;
        if (heapSizeMegs > 511L) {
            LauncherFrame.main(args);
        }
        else {
            final ArrayList params = new ArrayList();
            final String pathToJar = MinecraftLauncher.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            params.add("javaw");
            params.add("-Xms512m");
            params.add("-Xmx1024m");
            params.add("-Dsun.java2d.noddraw=true");
            params.add("-Dsun.java2d.d3d=false");
            params.add("-Dsun.java2d.opengl=false");
            params.add("-Dsun.java2d.pmoffscreen=false");
            params.add("-classpath");
            params.add(pathToJar);
            params.add("net.minecraft.LauncherFrame");
            if (!MinecraftLauncher.debugMode) {
                try {
                    final ProcessBuilder pb = new ProcessBuilder(params);
                    final Process process = pb.start();
                    if (process == null) {
                        throw new Exception("!");
                    }
                    System.exit(0);
                }
                catch (IOException ex) {}
            }
            try {
                params.set(0, "java");
                final ProcessBuilder pb = new ProcessBuilder(params);
                final Process process = pb.start();
                if (process == null) {
                    throw new IOException("!");
                }
                if (MinecraftLauncher.debugMode) {
                    final OutputConsole console = new OutputConsole();
                    final BufferedReader reader1 = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                    final BufferedReader reader2 = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    final Thread errorViewer = new Thread() {
                        BufferedReader reader = reader1;
                        
                        @Override
                        public void run() {
                            boolean terminated = false;
                            boolean noerrors = true;
                            String output = "";
                            console.acquire();
                            while (!terminated && output != null) {
                                int exitvalue = Integer.MIN_VALUE;
                                try {
                                    exitvalue = process.exitValue();
                                }
                                catch (IllegalThreadStateException ex) {
                                    try {
                                        output = this.reader.readLine();
                                        System.err.println(output);
                                        console.appendText("\nError: " + output);
                                    }
                                    catch (IOException ex2) {
                                        output = null;
                                    }
                                }
                                if (exitvalue != Integer.MIN_VALUE) {
                                    terminated = true;
                                    if (exitvalue == 0) {
                                        continue;
                                    }
                                    noerrors = false;
                                }
                            }
                            if (noerrors) {
                                console.release();
                            }
                        }
                    };
                    final Thread outViewer = new Thread() {
                        BufferedReader reader = reader2;
                        
                        @Override
                        public void run() {
                            boolean terminated = false;
                            boolean noerrors = true;
                            String output = "";
                            console.acquire();
                            while (!terminated && output != null) {
                                int exitvalue = Integer.MIN_VALUE;
                                try {
                                    exitvalue = process.exitValue();
                                }
                                catch (IllegalThreadStateException ex) {
                                    try {
                                        output = this.reader.readLine();
                                        System.out.println(output);
                                        console.appendText("\nOutput: " + output);
                                    }
                                    catch (IOException ex2) {
                                        output = null;
                                    }
                                }
                                if (exitvalue != Integer.MIN_VALUE) {
                                    terminated = true;
                                    if (exitvalue == 0) {
                                        continue;
                                    }
                                    noerrors = false;
                                }
                            }
                            if (noerrors) {
                                console.release();
                            }
                        }
                    };
                    errorViewer.start();
                    outViewer.start();
                }
                if (!MinecraftLauncher.debugMode) {
                    System.exit(0);
                }
            }
            catch (IOException e) {
                System.out.println("Java couldn't figure out a way to get more memory.\nIf the game crashes, run to the hills!");
                LauncherFrame.main(args);
            }
        }
    }
    
    static {
        MinecraftLauncher.debugMode = false;
    }
}
