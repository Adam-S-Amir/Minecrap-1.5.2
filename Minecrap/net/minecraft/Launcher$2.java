/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft;

class Launcher.2
extends Thread {
    Launcher.2() {
    }

    public void run() {
        while (Launcher.this.applet == null) {
            Launcher.this.repaint();
            try {
                Thread.sleep(10L);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
