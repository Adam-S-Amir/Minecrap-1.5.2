/*
 * Decompiled with CFR 0.152.
 */
package magnus.console;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

class OutputConsole.1
implements AdjustmentListener {
    OutputConsole.1() {
    }

    public void adjustmentValueChanged(AdjustmentEvent e) {
        e.getAdjustable().setValue(e.getAdjustable().getMaximum());
    }
}
