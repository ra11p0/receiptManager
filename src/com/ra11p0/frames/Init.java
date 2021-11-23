package com.ra11p0.frames;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.ra11p0.Core;
import com.ra11p0.frames.Overview.Frames.Overview;
import com.ra11p0.frames.ReceiptsManager.ReceiptsManager;
import com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel;
import com.sun.xml.internal.ws.api.ResourceLoader;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

public class Init {
    private final String DEFAULT_RECEIPT_PATH = "res\\receipts.json";
    public static ResourceBundle localeBundle;
    public static Properties settingsProp=null;
    private static String _title;
    public static Boolean reloading = false;
    public Init(String title) throws Exception{
        //preparing last stuff before launching
        try {
            loadSettings();
        } catch (Exception ex){
            loadDefaultSettings();
        }
        //Check if settings file is right, if not, loading default settings.
        checkSettings();
        switch(Init.settingsProp.getProperty("appearance")){
            case "dark" : {UIManager.setLookAndFeel(new FlatDarculaLaf()); break;}
            case "light" : {UIManager.setLookAndFeel(new FlatIntelliJLaf()); break;}
            case "windows" : {UIManager.setLookAndFeel(new WindowsClassicLookAndFeel()); break;}
        }
        if(Float.parseFloat(settingsProp.getProperty("version")) != Core.BUILD) settingsProp.setProperty("version", String.valueOf(Core.BUILD));
        Locale locale = new Locale(settingsProp.getProperty("language"));
        _title = title;
        localeBundle = ResourceBundle.getBundle("lang", locale);
        //*****
        JFrame preparingFiles = new JFrame();
        preparingFiles.setResizable(false);
        preparingFiles.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        preparingFiles.setLayout(new BorderLayout());
        preparingFiles.setSize(200, 100);
        preparingFiles.add(new JLabel("Wait until all files are processed..."), BorderLayout.CENTER);
        preparingFiles.setVisible(true);
        new Thread(() -> {
            //loading receipts file from disk
            File temp = new File("res/.temp");
            File source = new File("res/receipts.json");
            try {
                FileUtils.copyFile(source, new File(temp.getPath() + "/receipts.json"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            ReceiptsManager.refreshItemsAndReceipts(DEFAULT_RECEIPT_PATH);
            //*****
            preparingFiles.dispose();
            loadFrame();

        }).start();
    }
    public static void loadFrame(){
        try {
            Overview overview = new Overview(_title);
            overview.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    try {
                        saveSettings();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    if(!Init.reloading) System.exit(0);
                    Init.reloading = false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void loadSettings() throws IOException {
        settingsProp = new Properties();
        InputStream is = new FileInputStream("settings.xml");
        settingsProp.loadFromXML(is);
        is.close();
    }
    private static void loadDefaultSettings() throws IOException {
        JOptionPane.showMessageDialog(null, "Settings file is corrupted or empty! Loading default settings.", "Error!", JOptionPane.ERROR_MESSAGE);
        settingsProp = new Properties();
        settingsProp.loadFromXML(ResourceLoader.class.getResourceAsStream("/defaultSettings.xml"));
    }
    private static void saveSettings() throws IOException {
        settingsProp.storeToXML(new FileOutputStream("settings.xml"), "");
    }
    private static void checkSettings(){
        if(settingsProp.getProperty("version") == null)
            settingsProp.setProperty("version", String.valueOf(Core.BUILD));
        if(settingsProp.getProperty("language") == null)
            settingsProp.setProperty("language", "english");
        if(settingsProp.getProperty("currency") == null)
            settingsProp.setProperty("currency", "USD");
        if(settingsProp.getProperty("appearance") == null)
            settingsProp.setProperty("appearance", "dark");
    }
}