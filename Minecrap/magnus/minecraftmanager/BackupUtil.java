/*
 * Decompiled with CFR 0.152.
 */
package magnus.minecraftmanager;

import magnus.minecraftmanager.Zipper;
import java.io.File;
import java.util.ArrayList;
import javax.swing.filechooser.FileFilter;
import net.minecraft.MinecraftUtil;

public class BackupUtil {
    public static final String WORLD_BACKUP_EXTENSION = "mcworld";
    public static final String WORLD_BACKUP_GEN_NAME = "world_backup";
    public static final String GAME_BACKUP_EXTENSION = "mcgame";
    public static final String GAME_BACKUP_GEN_NAME = "minecraft_backup";
    public static final String DATE_TIME_FORMAT = "%1$tY-%1$tm-%1$td_%1$tH-%1$tM-%1$tS";

    public static void uninstallGame(boolean includeSaves) {
        BackupUtil.deleteFileDir(MinecraftUtil.getBinFolder());
        BackupUtil.deleteFileDir(MinecraftUtil.getLoginFile());
        BackupUtil.deleteFileDir(MinecraftUtil.getResourcesFolder());
        BackupUtil.deleteFileDir(MinecraftUtil.getOptionsFile());
        if (includeSaves) {
            BackupUtil.deleteFileDir(MinecraftUtil.getSavesFolder());
        }
    }

    public static void backupGame(File zipDestiny, boolean wholegame) {
        File[] source;
        if (!wholegame) {
            ArrayList<File> contents = new ArrayList<File>();
            File f = MinecraftUtil.getBinFolder();
            if (f.exists()) {
                contents.add(f);
            }
            if ((f = MinecraftUtil.getResourcesFolder()).exists()) {
                contents.add(f);
            }
            if ((f = MinecraftUtil.getLoginFile()).exists()) {
                contents.add(f);
            }
            if ((f = MinecraftUtil.getOptionsFile()).exists()) {
                contents.add(f);
            }
            source = contents.toArray(new File[contents.size()]);
        } else {
            source = MinecraftUtil.getWorkingDirectory().listFiles();
        }
        BackupUtil.backupContents(source, zipDestiny, GAME_BACKUP_GEN_NAME, GAME_BACKUP_EXTENSION);
    }

    public static void restoreGame(File zipSource) {
        File destiny = MinecraftUtil.getWorkingDirectory();
        BackupUtil.restoreContents(zipSource, destiny, GAME_BACKUP_GEN_NAME);
    }

    public static File getWorldNFolder(int n) {
        File source = new File(MinecraftUtil.getSavesFolder(), "World" + n);
        return source;
    }

    public static void backupWorld(int n, File destZip) {
        File source = BackupUtil.getWorldNFolder(n);
        BackupUtil.backupFile(source, destZip, WORLD_BACKUP_GEN_NAME, WORLD_BACKUP_EXTENSION);
    }

    public static void restoreWorld(int n, File zipSource) {
        File destiny = BackupUtil.getWorldNFolder(n);
        BackupUtil.restoreFile(zipSource, destiny, WORLD_BACKUP_GEN_NAME);
    }

    public static void backupFile(File source, File zipDestiny, String genericName, String extension) {
        File generic = new File(MinecraftUtil.getTempFolder(), genericName);
        if (!source.exists()) {
            throw new IllegalArgumentException("Source file does not exist: " + source.getName());
        }
        if (!zipDestiny.getName().endsWith("." + extension)) {
            zipDestiny = new File(zipDestiny.getPath() + "." + extension);
        }
        if (generic.exists()) {
            BackupUtil.deleteFileDir(generic);
        }
        source.renameTo(generic);
        Zipper.zipFolder(generic, zipDestiny);
        generic.renameTo(source);
    }

    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf(46);
        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    public static boolean deleteFileDir(File dir) {
        if (!dir.exists()) {
            return false;
        }
        if (dir.isFile()) {
            return dir.delete();
        }
        if (dir.isDirectory()) {
            for (File f : dir.listFiles()) {
                BackupUtil.deleteFileDir(f);
            }
            return dir.delete();
        }
        return dir.delete();
    }

    public static void restoreFile(File zipSource, File destiny, String genericName) {
        File generic = new File(MinecraftUtil.getTempFolder(), genericName);
        if (generic.exists()) {
            BackupUtil.deleteFileDir(generic);
        }
        Zipper.unzipFolder(zipSource, MinecraftUtil.getTempFolder());
        if (!generic.exists()) {
            throw new IllegalStateException("Wrong content in zip file -> not found: " + generic.getName());
        }
        if (destiny.exists()) {
            BackupUtil.deleteFileDir(destiny);
        }
        if (destiny.getParentFile() != null && !destiny.getParentFile().exists()) {
            destiny.getParentFile().mkdirs();
        }
        generic.renameTo(destiny);
    }

    public static void backupContents(File[] folderContents, File zipDestiny, String genericName, String extension) {
        for (File content : folderContents) {
            if (content.exists()) continue;
            throw new IllegalArgumentException("You sent me a folder content that doesnt exist : " + content.getName());
        }
        if (!zipDestiny.getName().endsWith("." + extension)) {
            zipDestiny = new File(zipDestiny.getPath() + "." + extension);
        }
        Zipper.zipFolders(folderContents, zipDestiny, genericName);
    }

    public static void restoreContents(File zipSource, File folderDestiny, String genericName) {
        File[] generics;
        File genericFolder = new File(MinecraftUtil.getTempFolder(), genericName);
        if (genericFolder.exists()) {
            BackupUtil.deleteFileDir(genericFolder);
        }
        if (!folderDestiny.exists()) {
            folderDestiny.mkdirs();
        }
        if (!folderDestiny.isDirectory()) {
            throw new IllegalArgumentException("The destiny folder must be a directory!");
        }
        Zipper.unzipFolder(zipSource, MinecraftUtil.getTempFolder());
        if (!genericFolder.exists()) {
            throw new IllegalStateException("Wrong content in zip file -> not found: " + genericFolder.getName());
        }
        for (File generic : generics = genericFolder.listFiles()) {
            File destiny = new File(folderDestiny, generic.getName());
            if (destiny.exists()) {
                BackupUtil.deleteFileDir(destiny);
            }
            generic.renameTo(destiny);
        }
    }

    public static class GameFileFilter
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

    public static class WorldFileFilter
    extends FileFilter {
        public boolean accept(File f) {
            if (f == null) {
                return false;
            }
            if (f.isDirectory()) {
                return true;
            }
            String ext = BackupUtil.getExtension(f);
            return ext != null && ext.equalsIgnoreCase(BackupUtil.WORLD_BACKUP_EXTENSION);
        }

        public String getDescription() {
            return "Minecraft World files";
        }
    }
}
