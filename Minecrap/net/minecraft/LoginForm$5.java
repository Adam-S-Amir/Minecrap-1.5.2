/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Label;

class LoginForm.5
extends Label {
    private static final long serialVersionUID = 0L;

    LoginForm.5(String x0) {
        super(x0);
    }

    public void paint(Graphics g) {
        super.paint(g);
        int x = 0;
        int y = 0;
        FontMetrics fm = g.getFontMetrics();
        int width = fm.stringWidth(this.getText());
        int height = fm.getHeight();
        if (this.getAlignment() == 0) {
            x = 0;
        } else if (this.getAlignment() == 1) {
            x = this.getBounds().width / 2 - width / 2;
        } else if (this.getAlignment() == 2) {
            x = this.getBounds().width - width;
        }
        y = this.getBounds().height / 2 + height / 2 - 1;
        g.drawLine(x + 2, y, x + width - 2, y);
    }

    public void update(Graphics g) {
        this.paint(g);
    }
}
