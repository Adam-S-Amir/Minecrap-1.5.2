/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Panel;

class LoginForm.2
extends Panel {
    private static final long serialVersionUID = 1L;
    private Insets insets = new Insets(12, 24, 16, 32);

    LoginForm.2() {
    }

    public Insets getInsets() {
        return this.insets;
    }

    public void update(Graphics g) {
        this.paint(g);
    }

    public void paint(Graphics g) {
        super.paint(g);
        int hOffs = 0;
        g.setColor(Color.BLACK);
        g.drawRect(0, 0 + hOffs, this.getWidth() - 1, this.getHeight() - 1 - hOffs);
        g.drawRect(1, 1 + hOffs, this.getWidth() - 3, this.getHeight() - 3 - hOffs);
        g.setColor(Color.WHITE);
        g.drawRect(2, 2 + hOffs, this.getWidth() - 5, this.getHeight() - 5 - hOffs);
    }
}
