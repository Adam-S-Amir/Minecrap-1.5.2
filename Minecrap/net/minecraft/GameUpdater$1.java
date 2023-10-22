/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft;

import java.io.File;
import java.security.PrivilegedExceptionAction;
import net.minecraft.MinecraftUtil;

class GameUpdater.1
implements PrivilegedExceptionAction {
    GameUpdater.1() {
    }

    public Object run() throws Exception {
        return MinecraftUtil.getWorkingDirectory() + File.separator + "bin" + File.separator;
    }
}
