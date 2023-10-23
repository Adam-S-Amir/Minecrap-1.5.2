// 
// Decompiled by Procyon v0.5.36
// 

package magnus.minecraftmanager;

import javax.swing.filechooser.FileFilter;
import java.util.ArrayList;
import java.io.File;
import net.minecraft.MinecraftUtil;

public class BackupUtil
{
    public static final String WORLD_BACKUP_EXTENSION = "mcworld";
    public static final String WORLD_BACKUP_GEN_NAME = "world_backup";
    public static final String GAME_BACKUP_EXTENSION = "mcgame";
    public static final String GAME_BACKUP_GEN_NAME = "minecraft_backup";
    public static final String DATE_TIME_FORMAT = "%1$tY-%1$tm-%1$td_%1$tH-%1$tM-%1$tS";
    
    public static void uninstallGame(final boolean includeSaves) {
        deleteFileDir(MinecraftUtil.getBinFolder());
        deleteFileDir(MinecraftUtil.getLoginFile());
        deleteFileDir(MinecraftUtil.getResourcesFolder());
        deleteFileDir(MinecraftUtil.getOptionsFile());
        if (includeSaves) {
            deleteFileDir(MinecraftUtil.getSavesFolder());
        }
    }
    
    public static void backupGame(final File zipDestiny, final boolean wholegame) {
        File[] source;
        if (!wholegame) {
            final ArrayList<File> contents = new ArrayList<File>();
            File f = MinecraftUtil.getBinFolder();
            if (f.exists()) {
                contents.add(f);
            }
            f = MinecraftUtil.getResourcesFolder();
            if (f.exists()) {
                contents.add(f);
            }
            f = MinecraftUtil.getLoginFile();
            if (f.exists()) {
                contents.add(f);
            }
            f = MinecraftUtil.getOptionsFile();
            if (f.exists()) {
                contents.add(f);
            }
            source = contents.toArray(new File[contents.size()]);
        }
        else {
            source = MinecraftUtil.getWorkingDirectory().listFiles();
        }
        backupContents(source, zipDestiny, "minecraft_backup", "mcgame");
    }
    
    public static void restoreGame(final File zipSource) {
        final File destiny = MinecraftUtil.getWorkingDirectory();
        restoreContents(zipSource, destiny, "minecraft_backup");
    }
    
    public static File getWorldNFolder(final int n) {
        final File source = new File(MinecraftUtil.getSavesFolder(), "World" + n);
        return source;
    }
    
    public static void backupWorld(final int n, final File destZip) {
        final File source = getWorldNFolder(n);
        backupFile(source, destZip, "world_backup", "mcworld");
    }
    
    public static void restoreWorld(final int n, final File zipSource) {
        final File destiny = getWorldNFolder(n);
        restoreFile(zipSource, destiny, "world_backup");
    }
    
    public static void backupFile(final File source, File zipDestiny, final String genericName, final String extension) {
        final File generic = new File(MinecraftUtil.getTempFolder(), genericName);
        if (!source.exists()) {
            throw new IllegalArgumentException("Source file does not exist: " + source.getName());
        }
        if (!zipDestiny.getName().endsWith("." + extension)) {
            zipDestiny = new File(zipDestiny.getPath() + "." + extension);
        }
        if (generic.exists()) {
            deleteFileDir(generic);
        }
        source.renameTo(generic);
        Zipper.zipFolder(generic, zipDestiny);
        generic.renameTo(source);
    }
    
    public static String getExtension(final File f) {
        String ext = null;
        final String s = f.getName();
        final int i = s.lastIndexOf(46);
        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }
    
    public static boolean deleteFileDir(final File dir) {
        if (!dir.exists()) {
            return false;
        }
        if (dir.isFile()) {
            return dir.delete();
        }
        if (dir.isDirectory()) {
            for (final File f : dir.listFiles()) {
                deleteFileDir(f);
            }
            return dir.delete();
        }
        return dir.delete();
    }
    
    public static void restoreFile(final File zipSource, final File destiny, final String genericName) {
        final File generic = new File(MinecraftUtil.getTempFolder(), genericName);
        if (generic.exists()) {
            deleteFileDir(generic);
        }
        Zipper.unzipFolder(zipSource, MinecraftUtil.getTempFolder());
        if (!generic.exists()) {
            throw new IllegalStateException("Wrong content in zip file -> not found: " + generic.getName());
        }
        if (destiny.exists()) {
            deleteFileDir(destiny);
        }
        if (destiny.getParentFile() != null && !destiny.getParentFile().exists()) {
            destiny.getParentFile().mkdirs();
        }
        generic.renameTo(destiny);
    }
    
    public static void backupContents(final File[] folderContents, File zipDestiny, final String genericName, final String extension) {
        for (final File content : folderContents) {
            if (!content.exists()) {
                throw new IllegalArgumentException("You sent me a folder content that doesnt exist : " + content.getName());
            }
        }
        if (!zipDestiny.getName().endsWith("." + extension)) {
            zipDestiny = new File(zipDestiny.getPath() + "." + extension);
        }
        Zipper.zipFolders(folderContents, zipDestiny, genericName);
    }
    
    public static void restoreContents(final File zipSource, final File folderDestiny, final String genericName) {
        final File genericFolder = new File(MinecraftUtil.getTempFolder(), genericName);
        if (genericFolder.exists()) {
            deleteFileDir(genericFolder);
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
        final File[] arr$;
        final File[] generics = arr$ = genericFolder.listFiles();
        for (final File generic : arr$) {
            final File destiny = new File(folderDestiny, generic.getName());
            if (destiny.exists()) {
                deleteFileDir(destiny);
            }
            generic.renameTo(destiny);
        }
    }
    
    public static class WorldFileFilter extends FileFilter
    {
        @Override
        public boolean accept(final File f) {
            if (f == null) {
                return false;
            }
            if (f.isDirectory()) {
                return true;
            }
            final String ext = BackupUtil.getExtension(f);
            return ext != null && ext.equalsIgnoreCase("mcworld");
        }
        
        @Override
        public String getDescription() {
            return "Minecraft World files";
        }
    }
    
    public static class GameFileFilter extends FileFilter
    {
        @Override
        public boolean accept(final File f) {
            if (f == null) {
                return false;
            }
            if (f.isDirectory()) {
                return true;
            }
            final String ext = BackupUtil.getExtension(f);
            return ext != null && ext.equalsIgnoreCase("mcgame");
        }
        
        @Override
        public String getDescription() {
            return "Minecraft Game files";
        }
    }
}
