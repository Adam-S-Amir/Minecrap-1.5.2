// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft;

import magnus.minecraftmanager.MinecraftBackupManager;
import java.awt.event.MouseListener;
import java.net.URL;
import java.awt.Desktop;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.FontMetrics;
import java.awt.Font;
import java.awt.Color;
import java.awt.image.ImageObserver;
import java.awt.Graphics;
import javax.crypto.SecretKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.Key;
import java.security.spec.KeySpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEParameterSpec;
import java.util.Random;
import java.io.DataOutputStream;
import java.io.OutputStream;
import javax.crypto.CipherOutputStream;
import java.io.FileOutputStream;
import javax.crypto.Cipher;
import java.io.DataInputStream;
import java.io.InputStream;
import javax.crypto.CipherInputStream;
import java.io.FileInputStream;
import java.io.File;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.GridBagLayout;
import java.awt.image.VolatileImage;
import java.awt.Label;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.TextField;
import java.awt.Image;
import java.awt.Panel;

public class LoginForm extends Panel
{
    private static final long serialVersionUID = 1L;
    private Image bgImage;
    private TextField userName;
    private Checkbox forceUpdateBox;
    private Button launchButton;
    private Label errorLabel;
    private Label creditsVersion;
    private Button openManager;
    private LauncherFrame launcherFrame;
    private boolean outdated;
    private VolatileImage img;
    
    public LoginForm(final LauncherFrame launcherFrame) {
        this.userName = new TextField(20);
        this.forceUpdateBox = new Checkbox("Force Update");
        this.launchButton = new Button("Enter Game");
        this.errorLabel = new Label("", 1);
        this.creditsVersion = new Label("v12.2");
        this.openManager = new Button("Backup Manager");
        this.outdated = false;
        this.launcherFrame = launcherFrame;
        final GridBagLayout gbl = new GridBagLayout();
        this.setLayout(gbl);
        this.add(this.buildLoginPanel());
        try {
            this.bgImage = ImageIO.read(LoginForm.class.getResource("dirt.png")).getScaledInstance(32, 32, 16);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.readUsername();
        this.launchButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent ae) {
                if (LoginForm.this.forceUpdateBox.getState()) {
                    LoginForm.this.launcherFrame.forceUpdate = true;
                }
                LoginForm.this.launcherFrame.login(LoginForm.this.userName.getText());
            }
        });
    }
    
    private void readUsername() {
        try {
            final File lastLogin = new File(MinecraftUtil.getWorkingDirectory(), "lastlogin");
            final Cipher cipher = this.getCipher(2, "passwordfile");
            DataInputStream dis;
            if (cipher != null) {
                dis = new DataInputStream(new CipherInputStream(new FileInputStream(lastLogin), cipher));
            }
            else {
                dis = new DataInputStream(new FileInputStream(lastLogin));
            }
            this.userName.setText(dis.readUTF());
            dis.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void writeUsername() {
        try {
            final File lastLogin = new File(MinecraftUtil.getWorkingDirectory(), "lastlogin");
            final Cipher cipher = this.getCipher(1, "passwordfile");
            DataOutputStream dos;
            if (cipher != null) {
                dos = new DataOutputStream(new CipherOutputStream(new FileOutputStream(lastLogin), cipher));
            }
            else {
                dos = new DataOutputStream(new FileOutputStream(lastLogin));
            }
            dos.writeUTF(this.userName.getText());
            dos.writeUTF("");
            dos.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private Cipher getCipher(final int mode, final String password) throws Exception {
        final Random random = new Random(43287234L);
        final byte[] salt = new byte[8];
        random.nextBytes(salt);
        final PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, 5);
        final SecretKey pbeKey = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(new PBEKeySpec(password.toCharArray()));
        final Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
        cipher.init(mode, pbeKey, pbeParamSpec);
        return cipher;
    }
    
    @Override
    public void update(final Graphics g) {
        this.paint(g);
    }
    
    @Override
    public void paint(final Graphics g2) {
        final int w = this.getWidth() / 2;
        final int h = this.getHeight() / 2;
        if (this.img == null || this.img.getWidth() != w || this.img.getHeight() != h) {
            this.img = this.createVolatileImage(w, h);
        }
        final Graphics g3 = this.img.getGraphics();
        for (int x = 0; x <= w / 32; ++x) {
            for (int y = 0; y <= h / 32; ++y) {
                g3.drawImage(this.bgImage, x * 32, y * 32, null);
            }
        }
        g3.setColor(Color.LIGHT_GRAY);
        final String msg = "Minecraft Launcher";
        g3.setFont(new Font(null, 1, 20));
        final FontMetrics fm = g3.getFontMetrics();
        g3.drawString(msg, w / 2 - fm.stringWidth(msg) / 2, h / 2 - fm.getHeight() * 2);
        g3.dispose();
        g2.drawImage(this.img, 0, 0, w * 2, h * 2, null);
    }
    
    private Panel buildLoginPanel() {
        final Panel panel = new Panel() {
            private static final long serialVersionUID = 1L;
            private Insets insets = new Insets(12, 24, 16, 32);
            
            @Override
            public Insets getInsets() {
                return this.insets;
            }
            
            @Override
            public void update(final Graphics g) {
                this.paint(g);
            }
            
            @Override
            public void paint(final Graphics g) {
                super.paint(g);
                final int hOffs = 0;
                g.setColor(Color.BLACK);
                g.drawRect(0, 0 + hOffs, this.getWidth() - 1, this.getHeight() - 1 - hOffs);
                g.drawRect(1, 1 + hOffs, this.getWidth() - 3, this.getHeight() - 3 - hOffs);
                g.setColor(Color.WHITE);
                g.drawRect(2, 2 + hOffs, this.getWidth() - 5, this.getHeight() - 5 - hOffs);
            }
        };
        panel.setBackground(Color.GRAY);
        final BorderLayout layout = new BorderLayout();
        layout.setHgap(0);
        layout.setVgap(8);
        panel.setLayout(layout);
        final GridLayout gl1 = new GridLayout(0, 1);
        final GridLayout gl2 = new GridLayout(0, 1);
        gl1.setVgap(2);
        gl2.setVgap(2);
        final Panel titles = new Panel(gl1);
        final Panel values = new Panel(gl2);
        titles.add(new Label("Username:", 2));
        titles.add(new Label("", 2));
        values.add(this.userName);
        values.add(this.forceUpdateBox);
        panel.add(titles, "West");
        panel.add(values, "Center");
        final Panel loginPanel = new Panel(new BorderLayout());
        final Panel registerPanel = new Panel(new BorderLayout());
        try {
            if (this.outdated) {
                final Label accountLink = new Label("You need to update the launcher!") {
                    private static final long serialVersionUID = 0L;
                    
                    @Override
                    public void paint(final Graphics g) {
                        super.paint(g);
                        int x = 0;
                        int y = 0;
                        final FontMetrics fm = g.getFontMetrics();
                        final int width = fm.stringWidth(this.getText());
                        final int height = fm.getHeight();
                        if (this.getAlignment() == 0) {
                            x = 0;
                        }
                        else if (this.getAlignment() == 1) {
                            x = this.getBounds().width / 2 - width / 2;
                        }
                        else if (this.getAlignment() == 2) {
                            x = this.getBounds().width - width;
                        }
                        y = this.getBounds().height / 2 + height / 2 - 1;
                        g.drawLine(x + 2, y, x + width - 2, y);
                    }
                    
                    @Override
                    public void update(final Graphics g) {
                        this.paint(g);
                    }
                };
                accountLink.setCursor(Cursor.getPredefinedCursor(12));
                accountLink.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(final MouseEvent arg0) {
                        try {
                            Desktop.getDesktop().browse(new URL("http://www.minecraft.net/download.jsp").toURI());
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                accountLink.setForeground(Color.BLUE);
                registerPanel.add(accountLink, "West");
                registerPanel.add(new Panel(), "Center");
            }
            else {
                final Label accountLink = new Label("Need account?") {
                    private static final long serialVersionUID = 0L;
                    
                    @Override
                    public void paint(final Graphics g) {
                        super.paint(g);
                        int x = 0;
                        int y = 0;
                        final FontMetrics fm = g.getFontMetrics();
                        final int width = fm.stringWidth(this.getText());
                        final int height = fm.getHeight();
                        if (this.getAlignment() == 0) {
                            x = 0;
                        }
                        else if (this.getAlignment() == 1) {
                            x = this.getBounds().width / 2 - width / 2;
                        }
                        else if (this.getAlignment() == 2) {
                            x = this.getBounds().width - width;
                        }
                        y = this.getBounds().height / 2 + height / 2 - 1;
                        g.drawLine(x + 2, y, x + width - 2, y);
                    }
                    
                    @Override
                    public void update(final Graphics g) {
                        this.paint(g);
                    }
                };
                accountLink.setCursor(Cursor.getPredefinedCursor(12));
                accountLink.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(final MouseEvent arg0) {
                        try {
                            Desktop.getDesktop().browse(new URL("http://www.minecraft.net/register.jsp").toURI());
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                accountLink.setForeground(Color.BLUE);
                registerPanel.add(this.creditsVersion, "West");
                registerPanel.add(new Panel(), "Center");
            }
        }
        catch (Error error) {}
        loginPanel.add(registerPanel, "Center");
        loginPanel.add(this.launchButton, "East");
        final Panel anjoPanel = new Panel();
        this.openManager.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                new MinecraftBackupManager().setVisible(true);
            }
        });
        anjoPanel.add(this.openManager);
        loginPanel.add(anjoPanel, "South");
        panel.add(loginPanel, "South");
        this.errorLabel.setFont(new Font(null, 2, 16));
        this.errorLabel.setForeground(new Color(8388608));
        panel.add(this.errorLabel, "North");
        return panel;
    }
    
    public void setError(final String errorMessage) {
        this.removeAll();
        this.add(this.buildLoginPanel());
        this.errorLabel.setText(errorMessage);
        this.validate();
    }
    
    public void loginOk() {
        this.writeUsername();
    }
}
