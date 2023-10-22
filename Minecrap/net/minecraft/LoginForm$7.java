/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft;

import magnus.minecraftmanager.MinecraftBackupManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class LoginForm.7
implements ActionListener {
    LoginForm.7() {
    }

    public void actionPerformed(ActionEvent e) {
        new MinecraftBackupManager().setVisible(true);
    }
}
