package com.ra11p0.frames;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.ra11p0.frames.Overview.Frames.Overview;
import com.ra11p0.frames.ReceiptsManager.ReceiptsManager;
import com.sun.xml.internal.ws.api.ResourceLoader;
import jdk.nashorn.internal.scripts.JO;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.nio.file.Files;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

public class Init {
    private final String DEFAULT_RECEIPT_PATH = "res\\receipts.json";
    public static ResourceBundle localeBundle;
    public static Properties settingsProp=null;
    private static String _title;
    private static Overview overview;
    public static Boolean reloading = false;
    public Init(String title) throws Exception{
        loadSettings();
        if(settingsProp==null) {
            settingsProp = new Properties();
            settingsProp.loadFromXML(ResourceLoader.class.getResourceAsStream("/defaultSettings.xml"));
        }
        //Check if settings file is right, if not, loading default settings.
        if(settingsProp.getProperty("language") == null ||
                settingsProp.getProperty("currency") == null
        ){
            JOptionPane.showMessageDialog(null, "Settings file is corrupted! Loading default settings.", "Error!", JOptionPane.ERROR_MESSAGE);
            settingsProp = new Properties();
            settingsProp.loadFromXML(ResourceLoader.class.getResourceAsStream("/defaultSettings.xml"));
        }
        _title = title;
        Locale locale = new Locale(settingsProp.getProperty("language"));
        localeBundle = ResourceBundle.getBundle("lang", locale);
        UIManager.setLookAndFeel( new FlatDarculaLaf());
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
            if(temp.exists()) {
                try {
                    FileUtils.cleanDirectory(temp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                temp.delete();
            }
            //TODO - check all files using one method
            File source = new File("res/receipts.json");
            File res = new File("res");
            if(!res.exists()) res.mkdir();
            if(!source.exists()) {
                try {
                    byte[] strToBytes = "".getBytes();
                    Files.write(source.toPath(), strToBytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
            overview = new Overview(_title);
            overview.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    try {
                        settingsProp.storeToXML(new FileOutputStream(new File("settings.xml")), "");
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
    public static void loadSettings() throws IOException {
        if(!new File("settings.xml").exists()) return;
        settingsProp = new Properties();
        InputStream is = new FileInputStream("settings.xml");
        settingsProp.loadFromXML(is);
        is.close();
    }
}