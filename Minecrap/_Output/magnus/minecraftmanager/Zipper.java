// 
// Decompiled by Procyon v0.5.36
// 

package magnus.minecraftmanager;

import java.util.zip.ZipInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.util.zip.ZipOutputStream;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.File;

public class Zipper
{
    static final int BUFFER = 2048;
    
    public static boolean zipFolders(final File[] srcFolders, final File destZipFile) {
        return zipFolders(srcFolders, destZipFile, "");
    }
    
    public static boolean zipFolders(final File[] srcFolders, final File destZipFile, final String inFolderName) {
        try {
            final BufferedInputStream origin = null;
            final FileOutputStream dest = new FileOutputStream(destZipFile);
            final ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
            for (final File fileFolder : srcFolders) {
                if (!addToZip(fileFolder.getParentFile().getPath(), inFolderName, fileFolder.getName(), out)) {
                    return false;
                }
            }
            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
    
    protected static boolean addToZip(String absolutePath, String relativePath, final String fileName, final ZipOutputStream out) {
        final File file = new File(absolutePath + File.separator + fileName);
        System.out.println("Adding \"" + absolutePath + File.separator + fileName + "\" file");
        if (file.isHidden()) {
            return true;
        }
        if (file.isDirectory()) {
            absolutePath = absolutePath + File.separator + file.getName();
            relativePath = relativePath + File.separator + file.getName();
            for (final String child : file.list()) {
                if (!addToZip(absolutePath, relativePath, child, out)) {
                    return false;
                }
            }
            return true;
        }
        try {
            final byte[] data = new byte[2048];
            final FileInputStream fi = new FileInputStream(file);
            final BufferedInputStream origin = new BufferedInputStream(fi, 2048);
            final ZipEntry entry = new ZipEntry(relativePath + File.separator + fileName);
            out.putNextEntry(entry);
            int count;
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
    
    public static boolean zipFolder(final File srcFolder, final File destZipFile) {
        final File[] ar = { srcFolder };
        return zipFolders(ar, destZipFile);
    }
    
    public static void unzipFolder(final File zipFile, final File destFolder) {
        try {
            BufferedOutputStream dest = null;
            final FileInputStream fis = new FileInputStream(zipFile);
            final ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                System.out.println("Extracting: " + entry);
                final byte[] data = new byte[2048];
                final File f = new File(destFolder + File.separator + entry.getName());
                if (f.getParentFile() != null && !f.getParentFile().exists()) {
                    f.getParentFile().mkdirs();
                }
                if (!f.exists()) {
                    f.createNewFile();
                    f.getParent();
                }
                final FileOutputStream fos = new FileOutputStream(f);
                dest = new BufferedOutputStream(fos, 2048);
                int count;
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
