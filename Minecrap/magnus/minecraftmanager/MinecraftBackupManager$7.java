/*
 * Decompiled with CFR 0.152.
 */
package magnus.minecraftmanager;

import magnus.minecraftmanager.MinecraftBackupManager;

static final class MinecraftBackupManager.7
implements Runnable {
    MinecraftBackupManager.7() {
    }

    public void run() {
        new MinecraftBackupManager().setVisible(true);
    }
}
