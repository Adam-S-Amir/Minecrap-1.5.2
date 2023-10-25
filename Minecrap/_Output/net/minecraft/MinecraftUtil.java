// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.File;

public class MinecraftUtil
{
    private static File workDir;
    private static File binDir;
    private static File resourcesDis;
    private static File optionsFile;
    private static File lastloginFile;
    private static File savesDir;
    private static File tempFolder;
    private static File nativesFolder;
    
    public static File getWorkingDirectory() {
        if (MinecraftUtil.workDir == null) {
            MinecraftUtil.workDir = getWorkingDirectory("minecrap");
        }
        return MinecraftUtil.workDir;
    }
    
    public static File getBinFolder() {
        if (MinecraftUtil.binDir == null) {
            MinecraftUtil.binDir = new File(getWorkingDirectory(), "bin");
        }
        return MinecraftUtil.binDir;
    }
    
    public static File getResourcesFolder() {
        if (MinecraftUtil.resourcesDis == null) {
            MinecraftUtil.resourcesDis = new File(getWorkingDirectory(), "resources");
        }
        return MinecraftUtil.resourcesDis;
    }
    
    public static File getOptionsFile() {
        if (MinecraftUtil.optionsFile == null) {
            MinecraftUtil.optionsFile = new File(getWorkingDirectory(), "options.txt");
        }
        return MinecraftUtil.optionsFile;
    }
    
    public static File getLoginFile() {
        if (MinecraftUtil.lastloginFile == null) {
            MinecraftUtil.lastloginFile = new File(getWorkingDirectory(), "lastlogin");
        }
        return MinecraftUtil.lastloginFile;
    }
    
    public static File getSavesFolder() {
        if (MinecraftUtil.savesDir == null) {
            MinecraftUtil.savesDir = new File(getWorkingDirectory(), "saves");
        }
        return MinecraftUtil.savesDir;
    }
    
    public static File getNativesFolder() {
        if (MinecraftUtil.nativesFolder == null) {
            MinecraftUtil.nativesFolder = new File(getBinFolder(), "natives");
        }
        return MinecraftUtil.nativesFolder;
    }
    
    public static File getTempFolder() {
        if (MinecraftUtil.tempFolder == null) {
            MinecraftUtil.tempFolder = new File(System.getProperties().getProperty("java.io.tmpdir"), "MCBKPMNGR");
        }
        if (!MinecraftUtil.tempFolder.exists()) {
            MinecraftUtil.tempFolder.mkdirs();
        }
        return MinecraftUtil.tempFolder;
    }
    
    public static File getWorkingDirectory(final String applicationName) {
        final String userHome = System.getProperty("user.home", ".");
        File workingDirectory = null;
        switch (getPlatform().ordinal()) {
            case 0:
            case 1: {
                workingDirectory = new File(userHome, '.' + applicationName + '/');
                break;
            }
            case 2: {
                final String applicationData = System.getenv("APPDATA");
                if (applicationData != null) {
                    workingDirectory = new File(applicationData, "." + applicationName + '/');
                    break;
                }
                workingDirectory = new File(userHome, '.' + applicationName + '/');
                break;
            }
            case 3: {
                workingDirectory = new File(userHome, "Library/Application Support/" + applicationName);
                break;
            }
            default: {
                workingDirectory = new File(userHome, applicationName + '/');
                break;
            }
        }
        if (!workingDirectory.exists() && !workingDirectory.mkdirs()) {
            throw new RuntimeException("The working directory could not be created: " + workingDirectory);
        }
        return workingDirectory;
    }
    
    private static OS getPlatform() {
        final String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            return OS.windows;
        }
        if (osName.contains("mac")) {
            return OS.macos;
        }
        if (osName.contains("solaris")) {
            return OS.solaris;
        }
        if (osName.contains("sunos")) {
            return OS.solaris;
        }
        if (osName.contains("linux")) {
            return OS.linux;
        }
        if (osName.contains("unix")) {
            return OS.linux;
        }
        return OS.unknown;
    }
    
    public static String excutePost(final String targetURL, final String urlParameters) {
        HttpURLConnection connection = null;
        try {
            final URL url = new URL(targetURL);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            final DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
            final InputStream is = connection.getInputStream();
            final BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            final StringBuffer response = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            final String str1 = response.toString();
            return str1;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
    
    public static void resetVersion() {
        DataOutputStream dos = null;
        try {
            final File dir = new File(getWorkingDirectory() + File.separator + "bin" + File.separator);
            final File versionFile = new File(dir, "version");
            dos = new DataOutputStream(new FileOutputStream(versionFile));
            dos.writeUTF("0");
        }
        catch (FileNotFoundException ex3) {}
        catch (IOException ex) {
            Logger.getLogger(MinecraftUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            try {
                dos.close();
            }
            catch (IOException ex2) {
                Logger.getLogger(MinecraftUtil.class.getName()).log(Level.SEVERE, null, ex2);
            }
        }
    }
    
    public static String getFakeLatestVersion() {
        try {
            final File dir = new File(getWorkingDirectory() + File.separator + "bin" + File.separator);
            final File file = new File(dir, "version");
            final DataInputStream dis = new DataInputStream(new FileInputStream(file));
            final String version = dis.readUTF();
            dis.close();
            if (version.equals("0")) {
                return "1285241960000";
            }
            return version;
        }
        catch (IOException ex) {
            return "1285241960000";
        }
    }
    
    static {
        MinecraftUtil.workDir = null;
        MinecraftUtil.binDir = null;
        MinecraftUtil.resourcesDis = null;
        MinecraftUtil.optionsFile = null;
        MinecraftUtil.lastloginFile = null;
        MinecraftUtil.savesDir = null;
        MinecraftUtil.tempFolder = null;
        MinecraftUtil.nativesFolder = null;
    }
    
    private enum OS
    {
        linux, 
        solaris, 
        windows, 
        macos, 
        unknown;
    }
}
