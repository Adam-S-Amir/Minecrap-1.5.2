/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft;

class GameUpdater.3
extends Thread {
    GameUpdater.3() {
    }

    public void run() {
        try {
            GameUpdater.this.isp[0] = GameUpdater.this.urlconnectionp.getInputStream();
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}
