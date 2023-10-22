/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft;

import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

class LoginForm.6
extends MouseAdapter {
    LoginForm.6() {
    }

    public void mousePressed(MouseEvent arg0) {
        try {
            Desktop.getDesktop().browse(new URL("http://www.minecraft.net/register.jsp").toURI());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
