/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft;

class LauncherFrame.1
extends Thread {
    LauncherFrame.1() {
    }

    public void run() {
        try {
            Thread.sleep(30000L);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("FORCING EXIT!");
        System.exit(0);
    }
}
