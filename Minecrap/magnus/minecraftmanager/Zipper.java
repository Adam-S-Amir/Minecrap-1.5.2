/*
 * Decompiled with CFR 0.152.
 */
package magnus.minecraftmanager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Zipper {
    static final int BUFFER = 2048;

    public static boolean zipFolders(File[] srcFolders, File destZipFile) {
        return Zipper.zipFolders(srcFolders, destZipFile, "");
    }

    public static boolean zipFolders(File[] srcFolders, File destZipFile, String inFolderName) {
        try {
            Object origin = null;
            FileOutputStream dest = new FileOutputStream(destZipFile);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
            for (File fileFolder : srcFolders) {
                if (Zipper.addToZip(fileFolder.getParentFile().getPath(), inFolderName, fileFolder.getName(), out)) continue;
                return false;
            }
            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    protected static boolean addToZip(String absolutePath, String relativePath, String fileName, ZipOutputStream out) {
        File file = new File(absolutePath + File.separator + fileName);
        System.out.println("Adding \"" + absolutePath + File.separator + fileName + "\" file");
        if (file.isHidden()) {
            return true;
        }
        if (file.isDirectory()) {
            absolutePath = absolutePath + File.separator + file.getName();
            relativePath = relativePath + File.separator + file.getName();
            for (String child : file.list()) {
                if (Zipper.addToZip(absolutePath, relativePath, child, out)) continue;
                return false;
            }
            return true;
        }
        try {
            int count;
            byte[] data = new byte[2048];
            FileInputStream fi = new FileInputStream(file);
            BufferedInputStream origin = new BufferedInputStream(fi, 2048);
            ZipEntry entry = new ZipEntry(relativePath + File.separator + fileName);
            out.putNextEntry(entry);
            while ((count = origin.read(data, 0, 2048)) != -1) {
                out.write(data, 0, count);
            }
            origin.close();
        }
        catch (Exception ex) {
            Logger.getLogger(Zipper.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public static boolean zipFolder(File srcFolder, File destZipFile) {
        File[] ar = new File[]{srcFolder};
        return Zipper.zipFolders(ar, destZipFile);
    }

    public static void unzipFolder(File zipFile, File destFolder) {
        try {
            ZipEntry entry;
            BufferedOutputStream dest = null;
            FileInputStream fis = new FileInputStream(zipFile);
            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
            while ((entry = zis.getNextEntry()) != null) {
                int count;
                System.out.println("Extracting: " + entry);
                byte[] data = new byte[2048];
                File f = new File(destFolder + File.separator + entry.getName());
                if (f.getParentFile() != null && !f.getParentFile().exists()) {
                    f.getParentFile().mkdirs();
                }
                if (!f.exists()) {
                    f.createNewFile();
                    f.getParent();
                }
                FileOutputStream fos = new FileOutputStream(f);
                dest = new BufferedOutputStream(fos, 2048);
                while ((count = zis.read(data, 0, 2048)) != -1) {
                    dest.write(data, 0, count);
                }
                dest.flush();
                dest.close();
            }
            zis.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
