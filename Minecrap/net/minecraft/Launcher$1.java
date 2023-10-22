/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft;

import net.minecraft.Launcher;

class Launcher.1
extends Thread {
    Launcher.1() {
    }

    public void run() {
        Launcher.this.gameUpdater.run();
        try {
            if (!((Launcher)Launcher.this).gameUpdater.fatalError) {
                Launcher.this.replace(Launcher.this.gameUpdater.createApplet());
            }
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        catch (InstantiationException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
