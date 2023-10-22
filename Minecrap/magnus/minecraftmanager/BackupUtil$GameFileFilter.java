/*
 * Decompiled with CFR 0.152.
 */
package magnus.minecraftmanager;

import magnus.minecraftmanager.BackupUtil;
import java.io.File;
import javax.swing.filechooser.FileFilter;

public static class BackupUtil.GameFileFilter
extends FileFilter {
    public boolean accept(File f) {
        if (f == null) {
            return false;
        }
        if (f.isDirectory()) {
            return true;
        }
        String ext = BackupUtil.getExtension(f);
        return ext != null && ext.equalsIgnoreCase(BackupUtil.GAME_BACKUP_EXTENSION);
    }

    public String getDescription() {
        return "Minecraft Game files";
    }
}
