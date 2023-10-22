/*
 * Decompiled with CFR 0.152.
 */
package magnus.minecraftmanager;

import magnus.minecraftmanager.BackupUtil;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.LayoutStyle;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import net.minecraft.MinecraftUtil;

public class MinecraftBackupManager
extends JFrame {
    private JButton backupgame;
    private JCheckBox fullgamebackup;
    private JButton jButton1;
    private JButton jButton2;
    private JButton jButton5;
    private JLabel jLabel1;
    private JLabel jLabel10;
    private JLabel jLabel12;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JLabel jLabel7;
    private JLabel jLabel8;
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JPanel jPanel3;
    private JPanel jPanel4;
    private JSeparator jSeparator1;
    private JTabbedPane jTabbedPane1;
    private JCheckBox saveuninstall;
    private JButton uninstall;
    private JRadioButton world1;
    private JRadioButton world2;
    private JRadioButton world3;
    private JRadioButton world4;
    private JRadioButton world5;
    private ButtonGroup worldgroup;

    public MinecraftBackupManager() {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        }
        catch (ClassNotFoundException ex) {
        }
        catch (InstantiationException ex) {
        }
        catch (IllegalAccessException ex) {
        }
        catch (UnsupportedLookAndFeelException unsupportedLookAndFeelException) {
            // empty catch block
        }
        this.initComponents();
        this.refreshButtons();
    }

    private void initComponents() {
        this.worldgroup = new ButtonGroup();
        this.jTabbedPane1 = new JTabbedPane();
        this.jPanel1 = new JPanel();
        this.jPanel3 = new JPanel();
        this.world1 = new JRadioButton();
        this.world2 = new JRadioButton();
        this.world3 = new JRadioButton();
        this.world4 = new JRadioButton();
        this.world5 = new JRadioButton();
        this.jPanel4 = new JPanel();
        this.jButton1 = new JButton();
        this.jButton2 = new JButton();
        this.jLabel3 = new JLabel();
        this.jLabel4 = new JLabel();
        this.jLabel5 = new JLabel();
        this.jLabel6 = new JLabel();
        this.jLabel10 = new JLabel();
        this.jPanel2 = new JPanel();
        this.uninstall = new JButton();
        this.saveuninstall = new JCheckBox();
        this.backupgame = new JButton();
        this.jButton5 = new JButton();
        this.jLabel1 = new JLabel();
        this.jLabel2 = new JLabel();
        this.jLabel7 = new JLabel();
        this.fullgamebackup = new JCheckBox();
        this.jSeparator1 = new JSeparator();
        this.jLabel8 = new JLabel();
        this.jLabel12 = new JLabel();
        this.setDefaultCloseOperation(2);
        this.setTitle("Minecraft Backup Manager (by magnus)");
        this.worldgroup.add(this.world1);
        this.world1.setForeground(new Color(255, 0, 0));
        this.world1.setText("World 1");
        this.worldgroup.add(this.world2);
        this.world2.setForeground(new Color(255, 0, 0));
        this.world2.setText("World 2");
        this.worldgroup.add(this.world3);
        this.world3.setForeground(new Color(255, 0, 0));
        this.world3.setText("World 3");
        this.worldgroup.add(this.world4);
        this.world4.setForeground(new Color(255, 0, 0));
        this.world4.setText("World 4");
        this.worldgroup.add(this.world5);
        this.world5.setForeground(new Color(255, 0, 0));
        this.world5.setText("World 5");
        GroupLayout jPanel3Layout = new GroupLayout(this.jPanel3);
        this.jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addContainerGap().addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.world1).addComponent(this.world2).addComponent(this.world3).addComponent(this.world4).addComponent(this.world5)).addContainerGap(-1, Short.MAX_VALUE)));
        jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addContainerGap().addComponent(this.world1).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.world2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.world3).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.world4).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.world5).addContainerGap(-1, Short.MAX_VALUE)));
        this.jButton1.setText("Do Backup");
        this.jButton1.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent evt) {
                MinecraftBackupManager.this.jButton1ActionPerformed(evt);
            }
        });
        this.jButton2.setText("Restore Backup");
        this.jButton2.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent evt) {
                MinecraftBackupManager.this.jButton2ActionPerformed(evt);
            }
        });
        GroupLayout jPanel4Layout = new GroupLayout(this.jPanel4);
        this.jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup().addContainerGap().addGroup(jPanel4Layout.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(this.jButton2, GroupLayout.Alignment.LEADING, -1, -1, Short.MAX_VALUE).addComponent(this.jButton1, GroupLayout.Alignment.LEADING, -1, 118, Short.MAX_VALUE)).addContainerGap()));
        jPanel4Layout.setVerticalGroup(jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addContainerGap().addComponent(this.jButton1).addGap(18, 18, 18).addComponent(this.jButton2).addContainerGap(-1, Short.MAX_VALUE)));
        this.jLabel3.setText("Everything will be stored in a Zip format.");
        this.jLabel4.setText("But I will use .mcworld extension,  cuz that makes me happy.");
        this.jLabel5.setText("You can restore any previous world number, to any world number you desire.");
        this.jLabel6.setText("Ex: You create a backup of World 2, but then you can restore it to World 1.");
        this.jLabel10.setForeground(new Color(255, 0, 0));
        this.jLabel10.setText("RED color marks the Worlds you don't have!");
        GroupLayout jPanel1Layout = new GroupLayout(this.jPanel1);
        this.jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addComponent(this.jPanel3, -2, -1, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGap(6, 6, 6).addComponent(this.jPanel4, -2, -1, -2)).addComponent(this.jLabel10).addComponent(this.jLabel3))).addComponent(this.jLabel4).addComponent(this.jLabel5).addComponent(this.jLabel6)).addContainerGap(109, Short.MAX_VALUE)));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addComponent(this.jPanel3, -2, -1, -2).addGroup(jPanel1Layout.createSequentialGroup().addComponent(this.jLabel10).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jPanel4, -2, -1, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, -1, Short.MAX_VALUE).addComponent(this.jLabel3))).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jLabel4).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jLabel5).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jLabel6).addContainerGap()));
        this.jTabbedPane1.addTab("Game Saves", this.jPanel1);
        this.uninstall.setText("Uninstall Game");
        this.uninstall.setEnabled(false);
        this.uninstall.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent evt) {
                MinecraftBackupManager.this.uninstallActionPerformed(evt);
            }
        });
        this.saveuninstall.setText("Complete Uninstall (Include Game Saves/Worlds)");
        this.saveuninstall.setEnabled(false);
        this.backupgame.setText("Backup Game (Binaries)");
        this.backupgame.setEnabled(false);
        this.backupgame.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent evt) {
                MinecraftBackupManager.this.backupgameActionPerformed(evt);
            }
        });
        this.jButton5.setText("Restore Game");
        this.jButton5.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent evt) {
                MinecraftBackupManager.this.jButton5ActionPerformed(evt);
            }
        });
        this.jLabel1.setText("You might want to backup the whole game at least once after it's installed.");
        this.jLabel2.setText("Never know when there is an update out there that breaks everything.");
        this.jLabel7.setText("Zip format again, but .mcgame extension. It really makes me happy.");
        this.fullgamebackup.setText("Entire Folder (Inc. Saves or any other file there)");
        this.fullgamebackup.setEnabled(false);
        this.fullgamebackup.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent evt) {
                MinecraftBackupManager.this.fullgamebackupActionPerformed(evt);
            }
        });
        GroupLayout jPanel2Layout = new GroupLayout(this.jPanel2);
        this.jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGap(6, 6, 6).addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jLabel2).addComponent(this.jLabel7).addComponent(this.jLabel1))).addComponent(this.jSeparator1, -1, 551, Short.MAX_VALUE).addGroup(jPanel2Layout.createSequentialGroup().addComponent(this.uninstall).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.saveuninstall)).addGroup(jPanel2Layout.createSequentialGroup().addComponent(this.backupgame).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.fullgamebackup)).addComponent(this.jButton5, -2, 239, -2)).addContainerGap()));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.uninstall).addComponent(this.saveuninstall)).addGap(3, 3, 3).addComponent(this.jSeparator1, -2, 10, -2).addGap(18, 18, 18).addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.backupgame).addComponent(this.fullgamebackup)).addGap(18, 18, 18).addComponent(this.jButton5).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jLabel1).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jLabel2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jLabel7).addContainerGap(-1, Short.MAX_VALUE)));
        this.jTabbedPane1.addTab("Game Installation", this.jPanel2);
        this.jLabel8.setText("by magnus - v1.1");
        this.jLabel12.setText("Backup/Restore take some seconds, wait for \"Done!\".");
        GroupLayout layout = new GroupLayout(this.getContentPane());
        this.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(this.jTabbedPane1, GroupLayout.Alignment.LEADING, -1, 563, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addComponent(this.jLabel12).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 137, Short.MAX_VALUE).addComponent(this.jLabel8))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addComponent(this.jTabbedPane1, -1, 234, Short.MAX_VALUE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jLabel8).addComponent(this.jLabel12)).addContainerGap()));
        this.pack();
    }

    private void uninstallActionPerformed(ActionEvent evt) {
        int result = JOptionPane.showConfirmDialog(this, "Are you sure that you want to uninstall Minecraft?\nCan't Undo It! (unless you have backup, hehe)", "Are you sure? (Uninstallation)", 0, 2);
        if (result == 1 || result == -1) {
            return;
        }
        BackupUtil.uninstallGame(this.saveuninstall.isSelected());
        JOptionPane.showMessageDialog(this, "Done!", "Uninstallation", -1);
        this.refreshButtons();
    }

    private void backupgameActionPerformed(ActionEvent evt) {
        JFileChooser save = new JFileChooser();
        Calendar now = GregorianCalendar.getInstance();
        save.setSelectedFile(new File(String.format("MCGame_" + (this.fullgamebackup.isSelected() ? "Complete_" : "") + "%1$tY-%1$tm-%1$td_%1$tH-%1$tM-%1$tS" + "_Backup." + "mcgame", now)));
        save.setFileSelectionMode(0);
        save.setFileFilter(new BackupUtil.GameFileFilter());
        int result = save.showSaveDialog(this);
        if (result != 0) {
            return;
        }
        File f = save.getSelectedFile();
        if (f == null) {
            return;
        }
        BackupUtil.backupGame(f, this.fullgamebackup.isSelected());
        JOptionPane.showMessageDialog(this, "Done!", "Game Backup", -1);
    }

    private void jButton5ActionPerformed(ActionEvent evt) {
        int result = JOptionPane.showConfirmDialog(this, "Are you sure that you want to restore WHOLE Minecraft?\nIT MIGHT OVERWRITE ALL YOUR DATA\n(if is a complete backup, you'll lose all your actual worlds/saves!)\nMake sure you have your most recent save games backed up before this!", "Are you sure? (Full Game Restoration)", 0, 2);
        if (result == 1 || result == -1) {
            return;
        }
        JFileChooser save = new JFileChooser();
        save.setFileSelectionMode(0);
        save.setFileFilter(new BackupUtil.GameFileFilter());
        save.showOpenDialog(this);
        File f = save.getSelectedFile();
        if (f == null || !f.exists()) {
            return;
        }
        try {
            BackupUtil.restoreGame(f);
        }
        catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, "Failed!\nInvalid Zip Contents!\nthe game folder inside must have 'minecraft_backup' as name.", "Game Restoration", 0);
            return;
        }
        JOptionPane.showMessageDialog(this, "Done!", "Game Restoration", -1);
        this.refreshButtons();
    }

    private void jButton1ActionPerformed(ActionEvent evt) {
        int world = this.getWorldSelected();
        if (world < 0) {
            JOptionPane.showMessageDialog(this, "Error. Please Select a World to Backup first!", "World Backup", 0);
            return;
        }
        if (!BackupUtil.getWorldNFolder(world).exists()) {
            JOptionPane.showMessageDialog(this, "Sorry, but this world doesn't Exist!", "World Backup", 0);
            return;
        }
        JFileChooser save = new JFileChooser();
        Calendar now = GregorianCalendar.getInstance();
        save.setFileSelectionMode(0);
        save.setSelectedFile(new File(String.format("MCWorld" + world + "_" + "%1$tY-%1$tm-%1$td_%1$tH-%1$tM-%1$tS" + "_Backup." + "mcworld", now)));
        save.setFileFilter(new BackupUtil.WorldFileFilter());
        int result = save.showSaveDialog(this);
        if (result != 0) {
            return;
        }
        File f = save.getSelectedFile();
        if (f == null) {
            return;
        }
        BackupUtil.backupWorld(world, f);
        JOptionPane.showMessageDialog(this, "Done!", "World Backup", -1);
    }

    private void jButton2ActionPerformed(ActionEvent evt) {
        int result;
        int world = this.getWorldSelected();
        if (world < 0) {
            JOptionPane.showMessageDialog(this, "Error. Please Select a World to Restore first!", "World Restoration", 0);
            return;
        }
        if (BackupUtil.getWorldNFolder(world).exists() && ((result = JOptionPane.showConfirmDialog(this, "Are you sure that you want to overwrite World " + world + "?\n" + "Can't Undo It! (unless you have backup, hehe)", "Are you sure? (World Restoration)", 0, 2)) == 1 || result == -1)) {
            return;
        }
        JFileChooser save = new JFileChooser();
        save.setFileSelectionMode(0);
        save.setFileFilter(new BackupUtil.WorldFileFilter());
        save.showOpenDialog(this);
        File f = save.getSelectedFile();
        if (f == null) {
            return;
        }
        try {
            BackupUtil.restoreWorld(world, f);
        }
        catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, "Failed!\nInvalid Zip Contents!\nthe world folder inside must have 'world_backup' as name.", "World Restoration", 0);
            return;
        }
        JOptionPane.showMessageDialog(this, "Done!", "World Restoration", -1);
        this.refreshButtons();
    }

    private void fullgamebackupActionPerformed(ActionEvent evt) {
        if (this.fullgamebackup.isSelected()) {
            this.backupgame.setText("Backup Game (Complete)");
        } else {
            this.backupgame.setText("Backup Game (Binaries)");
        }
    }

    public int getWorldSelected() {
        if (this.world1.isSelected()) {
            return 1;
        }
        if (this.world2.isSelected()) {
            return 2;
        }
        if (this.world3.isSelected()) {
            return 3;
        }
        if (this.world4.isSelected()) {
            return 4;
        }
        if (this.world5.isSelected()) {
            return 5;
        }
        return -1;
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable(){

            public void run() {
                new MinecraftBackupManager().setVisible(true);
            }
        });
    }

    private void refreshButtons() {
        if (BackupUtil.getWorldNFolder(1).exists()) {
            this.world1.setForeground(Color.BLACK);
        } else {
            this.world1.setForeground(Color.RED);
        }
        if (BackupUtil.getWorldNFolder(2).exists()) {
            this.world2.setForeground(Color.BLACK);
        } else {
            this.world2.setForeground(Color.RED);
        }
        if (BackupUtil.getWorldNFolder(3).exists()) {
            this.world3.setForeground(Color.BLACK);
        } else {
            this.world3.setForeground(Color.RED);
        }
        if (BackupUtil.getWorldNFolder(4).exists()) {
            this.world4.setForeground(Color.BLACK);
        } else {
            this.world4.setForeground(Color.RED);
        }
        if (BackupUtil.getWorldNFolder(5).exists()) {
            this.world5.setForeground(Color.BLACK);
        } else {
            this.world5.setForeground(Color.RED);
        }
        if (MinecraftUtil.getBinFolder().exists() || MinecraftUtil.getLoginFile().exists() || MinecraftUtil.getOptionsFile().exists() || MinecraftUtil.getResourcesFolder().exists() || MinecraftUtil.getSavesFolder().exists()) {
            this.uninstall.setEnabled(true);
        } else {
            this.uninstall.setEnabled(false);
        }
        if (MinecraftUtil.getSavesFolder().exists()) {
            this.saveuninstall.setEnabled(true);
        } else {
            this.saveuninstall.setEnabled(false);
        }
        if (MinecraftUtil.getBinFolder().exists()) {
            this.backupgame.setEnabled(true);
            this.fullgamebackup.setEnabled(true);
        } else {
            this.backupgame.setEnabled(false);
            this.fullgamebackup.setEnabled(false);
        }
    }
}
