// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft;

import java.util.Enumeration;
import java.security.cert.Certificate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.net.JarURLConnection;
import java.io.OutputStream;
import java.util.jar.JarOutputStream;
import SevenZip.LzmaAlone;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.applet.Applet;
import java.lang.reflect.Field;
import java.util.Vector;
import java.lang.reflect.Method;
import java.io.FilePermission;
import java.security.Permission;
import java.net.SocketPermission;
import java.security.SecureClassLoader;
import java.security.PermissionCollection;
import java.security.CodeSource;
import java.net.URLClassLoader;
import java.security.AccessControlException;
import java.security.AccessController;
import java.io.File;
import java.security.PrivilegedExceptionAction;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.jar.Pack200;
import java.net.URLConnection;
import java.io.InputStream;
import java.net.URL;

public class GameUpdater implements Runnable
{
    public static final int STATE_INIT = 1;
    public static final int STATE_DETERMINING_PACKAGES = 2;
    public static final int STATE_CHECKING_CACHE = 3;
    public static final int STATE_DOWNLOADING = 4;
    public static final int STATE_EXTRACTING_PACKAGES = 5;
    public static final int STATE_UPDATING_CLASSPATH = 6;
    public static final int STATE_SWITCHING_APPLET = 7;
    public static final int STATE_INITIALIZE_REAL_APPLET = 8;
    public static final int STATE_START_REAL_APPLET = 9;
    public static final int STATE_DONE = 10;
    public int percentage;
    public int currentSizeDownload;
    public int totalSizeDownload;
    public int currentSizeExtract;
    public int totalSizeExtract;
    protected URL[] urlList;
    private static ClassLoader classLoader;
    protected Thread loaderThread;
    protected Thread animationThread;
    public boolean fatalError;
    public String fatalErrorDescription;
    protected String subtaskMessage;
    protected int state;
    protected boolean lzmaSupported;
    protected boolean pack200Supported;
    protected String[] genericErrorMessage;
    protected boolean certificateRefused;
    protected String[] certificateRefusedMessage;
    protected static boolean natives_loaded;
    public boolean forceUpdate;
    public static final String[] gameFiles;
    InputStream[] isp;
    URLConnection urlconnectionp;
    
    public GameUpdater() {
        this.subtaskMessage = "";
        this.state = 1;
        this.lzmaSupported = false;
        this.pack200Supported = false;
        this.genericErrorMessage = new String[] { "An error occured while loading the applet.", "Please contact support to resolve this issue.", "<placeholder for error message>" };
        this.certificateRefusedMessage = new String[] { "Permissions for Applet Refused.", "Please accept the permissions dialog to allow", "the applet to continue the loading process." };
        this.forceUpdate = false;
    }
    
    public void init() {
        this.state = 1;
        try {
            Class.forName("LZMA.LzmaInputStream");
            this.lzmaSupported = true;
        }
        catch (Throwable t) {}
        try {
            Pack200.class.getSimpleName();
            this.pack200Supported = true;
        }
        catch (Throwable t2) {}
    }
    
    private String generateStacktrace(final Exception exception) {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        exception.printStackTrace(printWriter);
        return result.toString();
    }
    
    protected String getDescriptionForState() {
        switch (this.state) {
            case 1: {
                return "Initializing loader";
            }
            case 2: {
                return "Determining packages to load";
            }
            case 3: {
                return "Checking cache for existing files";
            }
            case 4: {
                return "Downloading packages";
            }
            case 5: {
                return "Extracting downloaded packages";
            }
            case 6: {
                return "Updating classpath";
            }
            case 7: {
                return "Switching applet";
            }
            case 8: {
                return "Initializing real applet";
            }
            case 9: {
                return "Starting real applet";
            }
            case 10: {
                return "Done loading";
            }
            default: {
                return "unknown state";
            }
        }
    }
    
    protected void loadJarURLs() throws Exception {
        this.state = 2;
        this.urlList = new URL[GameUpdater.gameFiles.length + 1];
        final URL path = new URL("http://s3.amazonaws.com/MinecraftDownload/");
        for (int i = 0; i < GameUpdater.gameFiles.length; ++i) {
            this.urlList[i] = new URL(path, GameUpdater.gameFiles[i]);
        }
        final String osName = System.getProperty("os.name");
        String nativeJar = null;
        if (osName.startsWith("Win")) {
            nativeJar = "windows_natives.jar.lzma";
        }
        else if (osName.startsWith("Linux")) {
            nativeJar = "linux_natives.jar.lzma";
        }
        else if (osName.startsWith("Mac")) {
            nativeJar = "macosx_natives.jar.lzma";
        }
        else if (osName.startsWith("Solaris") || osName.startsWith("SunOS")) {
            nativeJar = "solaris_natives.jar.lzma";
        }
        else {
            this.fatalErrorOccured("OS (" + osName + ") not supported", null);
        }
        if (nativeJar == null) {
            this.fatalErrorOccured("no lwjgl natives files found", null);
        }
        else {
            this.urlList[this.urlList.length - 1] = new URL(path, nativeJar);
        }
    }
    
    public void run() {
        this.init();
        this.state = 3;
        this.percentage = 5;
        try {
            this.loadJarURLs();
            final String path = AccessController.doPrivileged((PrivilegedExceptionAction<String>)new PrivilegedExceptionAction() {
                public Object run() throws Exception {
                    return MinecraftUtil.getWorkingDirectory() + File.separator + "bin" + File.separator;
                }
            });
            final File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            final int before = this.percentage;
            boolean cacheAvailable = false;
            if (this.canPlayOffline()) {
                cacheAvailable = true;
                this.percentage = 90;
            }
            if (this.forceUpdate || !cacheAvailable) {
                if (this.percentage != before) {
                    this.percentage = before;
                }
                System.out.println("Path: " + path);
                this.downloadJars(path);
                this.extractJars(path);
                this.extractNatives(path);
                this.percentage = 90;
            }
            this.updateClassPath(dir);
            this.state = 10;
        }
        catch (AccessControlException ace) {
            this.fatalErrorOccured(ace.getMessage(), ace);
            this.certificateRefused = true;
        }
        catch (Exception e) {
            this.fatalErrorOccured(e.getMessage(), e);
        }
        finally {
            this.loaderThread = null;
        }
    }
    
    protected void updateClassPath(final File dir) throws Exception {
        this.state = 6;
        this.percentage = 95;
        final URL[] urls = new URL[this.urlList.length];
        for (int i = 0; i < this.urlList.length; ++i) {
            urls[i] = new File(dir, this.getJarName(this.urlList[i])).toURI().toURL();
            System.out.println("URL: " + urls[i]);
        }
        if (GameUpdater.classLoader == null) {
            GameUpdater.classLoader = new URLClassLoader(urls) {
                @Override
                protected PermissionCollection getPermissions(final CodeSource codesource) {
                    PermissionCollection perms = null;
                    try {
                        final Method method = SecureClassLoader.class.getDeclaredMethod("getPermissions", CodeSource.class);
                        method.setAccessible(true);
                        perms = (PermissionCollection)method.invoke(this.getClass().getClassLoader(), codesource);
                        final String host = "www.minecraft.net";
                        if (host != null && host.length() > 0) {
                            perms.add(new SocketPermission(host, "connect,accept"));
                        }
                        else {
                            codesource.getLocation().getProtocol().equals("file");
                        }
                        perms.add(new FilePermission("<<ALL FILES>>", "read"));
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    return perms;
                }
            };
        }
        String path = dir.getAbsolutePath();
        if (!path.endsWith(File.separator)) {
            path += File.separator;
        }
        this.unloadNatives(path);
        System.setProperty("org.lwjgl.librarypath", path + "natives");
        System.setProperty("net.java.games.input.librarypath", path + "natives");
        GameUpdater.natives_loaded = true;
    }
    
    private void unloadNatives(final String nativePath) {
        if (!GameUpdater.natives_loaded) {
            return;
        }
        try {
            final Field field = ClassLoader.class.getDeclaredField("loadedLibraryNames");
            field.setAccessible(true);
            final Vector libs = (Vector)field.get(this.getClass().getClassLoader());
            final String path = new File(nativePath).getCanonicalPath();
            for (int i = 0; i < libs.size(); ++i) {
                final String s = libs.get(i);
                if (s.startsWith(path)) {
                    libs.remove(i);
                    --i;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public Applet createApplet() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        final Class appletClass = GameUpdater.classLoader.loadClass("net.minecraft.client.MinecraftApplet");
        return appletClass.newInstance();
    }
    
    protected void downloadJars(final String path) throws Exception {
        this.state = 4;
        final int[] fileSizes = new int[this.urlList.length];
        for (int i = 0; i < this.urlList.length; ++i) {
            System.out.println(this.urlList[i]);
            final URLConnection urlconnection = this.urlList[i].openConnection();
            urlconnection.setDefaultUseCaches(false);
            if (urlconnection instanceof HttpURLConnection) {
                ((HttpURLConnection)urlconnection).setRequestMethod("HEAD");
            }
            fileSizes[i] = urlconnection.getContentLength();
            this.totalSizeDownload += fileSizes[i];
        }
        final int percentage = 10;
        this.percentage = percentage;
        final int initialPercentage = percentage;
        final byte[] buffer = new byte[65536];
        for (int j = 0; j < this.urlList.length; ++j) {
            int unsuccessfulAttempts = 0;
            final int maxUnsuccessfulAttempts = 3;
            boolean downloadFile = true;
            while (downloadFile) {
                downloadFile = false;
                final URLConnection urlconnection2 = this.urlList[j].openConnection();
                if (urlconnection2 instanceof HttpURLConnection) {
                    urlconnection2.setRequestProperty("Cache-Control", "no-cache");
                    urlconnection2.connect();
                }
                final String currentFile = this.getFileName(this.urlList[j]);
                final InputStream inputstream = this.getJarInputStream(currentFile, urlconnection2);
                final FileOutputStream fos = new FileOutputStream(path + currentFile);
                long downloadStartTime = System.currentTimeMillis();
                int downloadedAmount = 0;
                int fileSize = 0;
                String downloadSpeedMessage = "";
                int bufferSize;
                while ((bufferSize = inputstream.read(buffer, 0, buffer.length)) != -1) {
                    fos.write(buffer, 0, bufferSize);
                    this.currentSizeDownload += bufferSize;
                    fileSize += bufferSize;
                    this.percentage = initialPercentage + this.currentSizeDownload * 45 / this.totalSizeDownload;
                    this.subtaskMessage = "Retrieving: " + currentFile + " " + this.currentSizeDownload * 100 / this.totalSizeDownload + "%";
                    downloadedAmount += bufferSize;
                    final long timeLapse = System.currentTimeMillis() - downloadStartTime;
                    if (timeLapse >= 1000L) {
                        float downloadSpeed = downloadedAmount / (float)timeLapse;
                        downloadSpeed = (int)(downloadSpeed * 100.0f) / 100.0f;
                        downloadSpeedMessage = " @ " + downloadSpeed + " KB/sec";
                        downloadedAmount = 0;
                        downloadStartTime += 1000L;
                    }
                    this.subtaskMessage += downloadSpeedMessage;
                }
                inputstream.close();
                fos.close();
                if (urlconnection2 instanceof HttpURLConnection) {
                    if (fileSize == fileSizes[j]) {
                        continue;
                    }
                    if (fileSizes[j] <= 0) {
                        continue;
                    }
                    if (++unsuccessfulAttempts >= maxUnsuccessfulAttempts) {
                        throw new Exception("failed to download " + currentFile);
                    }
                    downloadFile = true;
                    this.currentSizeDownload -= fileSize;
                }
            }
        }
        this.subtaskMessage = "";
    }
    
    protected InputStream getJarInputStream(final String currentFile, final URLConnection urlconnection) throws Exception {
        final InputStream[] is = { null };
        this.isp = is;
        this.urlconnectionp = urlconnection;
        for (int j = 0; j < 3 && is[0] == null; ++j) {
            final Thread t = new Thread() {
                @Override
                public void run() {
                    try {
                        GameUpdater.this.isp[0] = GameUpdater.this.urlconnectionp.getInputStream();
                    }
                    catch (Exception ex) {}
                }
            };
            t.setName("JarInputStreamThread");
            t.start();
            int iterationCount = 0;
            while (is[0] == null && iterationCount++ < 5) {
                try {
                    t.join(1000L);
                }
                catch (InterruptedException localInterruptedException) {}
            }
            if (is[0] == null) {
                try {
                    t.interrupt();
                    t.join();
                }
                catch (InterruptedException ex) {}
            }
        }
        if (is[0] != null) {
            return is[0];
        }
        if (currentFile.equals("minecraft.jar")) {
            throw new Exception("Unable to download " + currentFile);
        }
        throw new Exception("Unable to download " + currentFile);
    }
    
    protected void extractLZMA(final String in, final String out) throws Exception {
        final File f = new File(in);
        final File fout = new File(out);
        LzmaAlone.decompress(f, fout);
        f.delete();
    }
    
    protected void extractPack(final String in, final String out) throws Exception {
        final File f = new File(in);
        final FileOutputStream fostream = new FileOutputStream(out);
        final JarOutputStream jostream = new JarOutputStream(fostream);
        final Pack200.Unpacker unpacker = Pack200.newUnpacker();
        unpacker.unpack(f, jostream);
        jostream.close();
        f.delete();
    }
    
    protected void extractJars(final String path) throws Exception {
        this.state = 5;
        final float increment = 10.0f / this.urlList.length;
        for (int i = 0; i < this.urlList.length; ++i) {
            this.percentage = 55 + (int)(increment * (i + 1));
            final String filename = this.getFileName(this.urlList[i]);
            if (filename.endsWith(".pack.lzma")) {
                this.subtaskMessage = "Extracting: " + filename + " to " + filename.replaceAll(".lzma", "");
                this.extractLZMA(path + filename, path + filename.replaceAll(".lzma", ""));
                this.subtaskMessage = "Extracting: " + filename.replaceAll(".lzma", "") + " to " + filename.replaceAll(".pack.lzma", "");
                this.extractPack(path + filename.replaceAll(".lzma", ""), path + filename.replaceAll(".pack.lzma", ""));
            }
            else if (filename.endsWith(".pack")) {
                this.subtaskMessage = "Extracting: " + filename + " to " + filename.replace(".pack", "");
                this.extractPack(path + filename, path + filename.replace(".pack", ""));
            }
            else if (filename.endsWith(".lzma")) {
                this.subtaskMessage = "Extracting: " + filename + " to " + filename.replace(".lzma", "");
                this.extractLZMA(path + filename, path + filename.replace(".lzma", ""));
            }
        }
    }
    
    protected void extractNatives(final String path) throws Exception {
        this.state = 5;
        final int initialPercentage = this.percentage;
        final String nativeJar = this.getJarName(this.urlList[this.urlList.length - 1]);
        Certificate[] certificate = Launcher.class.getProtectionDomain().getCodeSource().getCertificates();
        if (certificate == null) {
            final URL location = Launcher.class.getProtectionDomain().getCodeSource().getLocation();
            final JarURLConnection jurl = (JarURLConnection)new URL("jar:" + location.toString() + "!/net/minecraft/Launcher.class").openConnection();
            jurl.setDefaultUseCaches(true);
            try {
                certificate = jurl.getCertificates();
            }
            catch (Exception ex) {}
        }
        final File nativeFolder = new File(path + "natives");
        if (!nativeFolder.exists()) {
            nativeFolder.mkdir();
        }
        final JarFile jarFile = new JarFile(path + nativeJar, true);
        Enumeration entities = jarFile.entries();
        this.totalSizeExtract = 0;
        while (entities.hasMoreElements()) {
            final JarEntry entry = entities.nextElement();
            if (!entry.isDirectory()) {
                if (entry.getName().indexOf(47) != -1) {
                    continue;
                }
                this.totalSizeExtract += (int)entry.getSize();
            }
        }
        this.currentSizeExtract = 0;
        entities = jarFile.entries();
        while (entities.hasMoreElements()) {
            final JarEntry entry = entities.nextElement();
            if (!entry.isDirectory()) {
                if (entry.getName().indexOf(47) != -1) {
                    continue;
                }
                final File f = new File(path + "natives" + File.separator + entry.getName());
                if (f.exists() && !f.delete()) {
                    continue;
                }
                final InputStream in = jarFile.getInputStream(jarFile.getEntry(entry.getName()));
                final OutputStream out = new FileOutputStream(path + "natives" + File.separator + entry.getName());
                final byte[] buffer = new byte[65536];
                int bufferSize;
                while ((bufferSize = in.read(buffer, 0, buffer.length)) != -1) {
                    out.write(buffer, 0, bufferSize);
                    this.currentSizeExtract += bufferSize;
                    this.percentage = initialPercentage + this.currentSizeExtract * 20 / this.totalSizeExtract;
                    this.subtaskMessage = "Extracting: " + entry.getName() + " " + this.currentSizeExtract * 100 / this.totalSizeExtract + "%";
                }
                validateCertificateChain(certificate, entry.getCertificates());
                in.close();
                out.close();
            }
        }
        this.subtaskMessage = "";
        jarFile.close();
        final File f2 = new File(path + nativeJar);
        f2.delete();
    }
    
    protected static void validateCertificateChain(final Certificate[] ownCerts, final Certificate[] native_certs) throws Exception {
        if (ownCerts == null) {
            return;
        }
        if (native_certs == null) {
            throw new Exception("Unable to validate certificate chain. Native entry did not have a certificate chain at all");
        }
        if (ownCerts.length != native_certs.length) {
            throw new Exception("Unable to validate certificate chain. Chain differs in length [" + ownCerts.length + " vs " + native_certs.length + "]");
        }
        for (int i = 0; i < ownCerts.length; ++i) {
            if (!ownCerts[i].equals(native_certs[i])) {
                throw new Exception("Certificate mismatch: " + ownCerts[i] + " != " + native_certs[i]);
            }
        }
    }
    
    protected String getJarName(final URL url) {
        String fileName = url.getFile();
        if (fileName.contains("?")) {
            fileName = fileName.substring(0, fileName.indexOf("?"));
        }
        if (fileName.endsWith(".pack.lzma")) {
            fileName = fileName.replaceAll(".pack.lzma", "");
        }
        else if (fileName.endsWith(".pack")) {
            fileName = fileName.replaceAll(".pack", "");
        }
        else if (fileName.endsWith(".lzma")) {
            fileName = fileName.replaceAll(".lzma", "");
        }
        return fileName.substring(fileName.lastIndexOf(47) + 1);
    }
    
    protected String getFileName(final URL url) {
        String fileName = url.getFile();
        if (fileName.contains("?")) {
            fileName = fileName.substring(0, fileName.indexOf("?"));
        }
        return fileName.substring(fileName.lastIndexOf(47) + 1);
    }
    
    protected void fatalErrorOccured(final String error, final Exception e) {
        e.printStackTrace();
        this.fatalError = true;
        this.fatalErrorDescription = "Fatal error occured (" + this.state + "): " + error;
        System.out.println(this.fatalErrorDescription);
        if (e != null) {
            System.out.println(this.generateStacktrace(e));
        }
    }
    
    public boolean canPlayOffline() {
        if (!MinecraftUtil.getBinFolder().exists() || !MinecraftUtil.getBinFolder().isDirectory()) {
            return false;
        }
        if (!MinecraftUtil.getNativesFolder().exists() || !MinecraftUtil.getNativesFolder().isDirectory()) {
            return false;
        }
        if (MinecraftUtil.getBinFolder().list().length < GameUpdater.gameFiles.length + 1) {
            return false;
        }
        if (MinecraftUtil.getNativesFolder().list().length < 1) {
            return false;
        }
        final String[] bins = MinecraftUtil.getBinFolder().list();
        for (final String necessary : GameUpdater.gameFiles) {
            boolean isThere = false;
            for (final String found : bins) {
                if (necessary.equalsIgnoreCase(found)) {
                    isThere = true;
                    break;
                }
            }
            if (!isThere) {
                return false;
            }
        }
        return true;
    }
    
    static {
        GameUpdater.natives_loaded = false;
        gameFiles = new String[] { "lwjgl.jar", "jinput.jar", "lwjgl_util.jar", "minecraft.jar" };
    }
}
