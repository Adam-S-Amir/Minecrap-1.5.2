/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import net.minecraft.LoginForm;

class LoginForm.1
implements ActionListener {
    LoginForm.1() {
    }

    public void actionPerformed(ActionEvent ae) {
        if (LoginForm.this.forceUpdateBox.getState()) {
            ((LoginForm)LoginForm.this).launcherFrame.forceUpdate = true;
        }
        LoginForm.this.launcherFrame.login(LoginForm.this.userName.getText());
    }
}
