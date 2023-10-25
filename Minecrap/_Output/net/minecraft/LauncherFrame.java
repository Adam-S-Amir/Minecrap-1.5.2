// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft;

import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.io.IOException;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;

public class LauncherFrame extends Frame
{
    public static final int VERSION = 12;
    private static final long serialVersionUID = 1L;
    private Launcher launcher;
    private LoginForm loginForm;
    public boolean forceUpdate;
    
    public LauncherFrame() {
        super("MineCrap Launcher");
        this.forceUpdate = false;
        System.out.println("Hello!");
        this.setBackground(Color.BLACK);
        this.loginForm = new LoginForm(this);
        this.setLayout(new BorderLayout());
        this.add(this.loginForm, "Center");
        this.loginForm.setPreferredSize(new Dimension(854, 480));
        this.pack();
        this.setLocationRelativeTo(null);
        try {
            this.setIconImage(ImageIO.read(LauncherFrame.class.getResource("favicon.png")));
        }
        catch (IOException e1) {
            e1.printStackTrace();
        }
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent arg0) {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(30000L);
                        }
                        catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("FORCING EXIT!");
                        System.exit(0);
                    }
                }.start();
                if (LauncherFrame.this.launcher != null) {
                    LauncherFrame.this.launcher.stop();
                    LauncherFrame.this.launcher.destroy();
                }
                System.exit(0);
            }
        });
    }
    
    public String getFakeResult(final String userName) {
        return MinecraftUtil.getFakeLatestVersion() + ":35b9fd01865fda9d70b157e244cf801c:" + userName + ":12345:";
    }
    
    public void login(final String userName) {
        final String result = this.getFakeResult(userName);
        final String[] values = result.split(":");
        this.launcher = new Launcher();
        this.launcher.forceUpdate = this.forceUpdate;
        this.launcher.customParameters.put("userName", values[2].trim());
        this.launcher.customParameters.put("sessionId", values[3].trim());
        this.launcher.init();
        this.removeAll();
        this.add(this.launcher, "Center");
        this.validate();
        this.launcher.start();
        this.loginForm.loginOk();
        this.loginForm = null;
        this.setTitle("MineCrap");
    }
    
    private void showError(final String error) {
        this.removeAll();
        this.add(this.loginForm);
        this.loginForm.setError(error);
        this.validate();
    }
    
    public boolean canPlayOffline(final String userName) {
        final Launcher launcher2 = new Launcher();
        launcher2.init(userName, "12345");
        return launcher2.canPlayOffline();
    }
    
    public static void main(final String[] args) {
        final LauncherFrame launcherFrame = new LauncherFrame();
        launcherFrame.setVisible(true);
    }
}
