/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class LauncherFrame.1
extends WindowAdapter {
    LauncherFrame.1() {
    }

    public void windowClosing(WindowEvent arg0) {
        new Thread(){

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
        }.start();
        if (LauncherFrame.this.launcher != null) {
            LauncherFrame.this.launcher.stop();
            LauncherFrame.this.launcher.destroy();
        }
        System.exit(0);
    }
}
