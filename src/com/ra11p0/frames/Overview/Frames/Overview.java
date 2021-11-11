package com.ra11p0.frames.Overview.Frames;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.google.gson.Gson;
import com.ra11p0.frames.Init;
import com.ra11p0.frames.Overview.Panels.OverviewPanel;
import com.ra11p0.frames.ReceiptsManager.ReceiptsManager;
import com.ra11p0.structures.*;
import com.ra11p0.utils.LangResource;
import com.sun.java.swing.plaf.motif.MotifLookAndFeel;
import com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Function;

public class Overview extends JFrame {
    private OverviewPanel overviewPanel = null;
    public Overview(String title) {
        JPanel mainPanel = new JPanel();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                quitAction();
            }
        });
        setJMenuBar(menuBar());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        //*****
        setSize(800, 375);
        setResizable(false);
        setTitle(title);
        mainPanel.setLayout(new GridLayout(1, 1));
        overviewPanel = new OverviewPanel(this);
        mainPanel.add(overviewPanel);
        add(mainPanel);
        setVisible(true);
    }
    private void quitAction(){
        JFrame current = this;
        setVisible(false);
        JFrame filesProcessing = new JFrame();
        filesProcessing.setLayout(new BorderLayout());
        filesProcessing.setSize(200, 100);
        filesProcessing.add(new JLabel(LangResource.get("waitUntilAllFilesAreProcessed")), BorderLayout.CENTER);
        Function<Object, Object> removeTemp = o -> {
            File temp = new File("res/.temp/");
            if(temp.exists()) {
                try {
                    FileUtils.cleanDirectory(temp);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                temp.delete();
            }
            return null;
        };
        if(!ReceiptsManager.checkIfChangesMade()) {
            removeTemp.apply(null);
            current.dispose();
            return;
        }
        Object choice = JOptionPane.showOptionDialog(null,
                LangResource.get("doYouWantToSaveTheChanges") + "?" , "",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, new Object[]{LangResource.get("no"), LangResource.get("yes")},
                LangResource.get("no"));
        filesProcessing.setVisible(true);
        new Thread(() -> {
            File changedReceipts = new File("res/receipts.json");
            if((int)choice != 1) {
                if(changedReceipts.exists()) {
                    changedReceipts.delete();
                }
                File notChangedReceipts = new File("res/.temp/receipts.json");
                try {
                    FileUtils.copyFile(notChangedReceipts, changedReceipts);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, LangResource.get("failedToProcessReceipts") + "!", LangResource.get("error"), JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            else {
                save();
            }
            removeTemp.apply(null);
            filesProcessing.dispose();
            current.dispose();
            System.exit(0);
        }).start();
    }
    private void save(){
        ReceiptsPacket receiptsPacket = new ReceiptsPacket(ReceiptsManager.getReceipts());
        new File(ReceiptsManager.getReceiptPath()).delete();
        Gson gson = new Gson();
        FileWriter fw = null;
        try {
            fw = new FileWriter(ReceiptsManager.getReceiptPath());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        gson.toJson(receiptsPacket, fw);
        try {
            assert fw != null;
            fw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        ReceiptsManager.changesMade = false;
        overviewPanel.generateOverviewPanel();
        JOptionPane.showMessageDialog(null, LangResource.get("saved") + "!");
    }
    private void importReceipts(){
        JFrame fileChooserFrame = new JFrame();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooserFrame.add(fileChooser);
        fileChooserFrame.pack();
        fileChooserFrame.setResizable(false);
        fileChooserFrame.setVisible(true);
        WindowAdapter closeWindowAdapter = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                fileChooserFrame.dispose();
            }
        };
        fileChooserFrame.addWindowListener(closeWindowAdapter);
        fileChooser.addActionListener(e -> {
            final int[] counter = {0};
            if(e.getActionCommand().equals("ApproveSelection")) {
                File receiptsFolder = fileChooser.getSelectedFile();
                JLabel importStateLabel = new JLabel(LangResource.get("imported") + " " + counter[0] + " " + LangResource.get("outOf")+ " " + Objects.requireNonNull(receiptsFolder.list()).length + ".");
                fileChooserFrame.getRootPane().setVisible(false);
                fileChooserFrame.remove(fileChooser);
                fileChooserFrame.add(importStateLabel);
                fileChooserFrame.getRootPane().setVisible(true);
                fileChooserFrame.setSize(200, 75);
                fileChooserFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                fileChooserFrame.removeWindowListener(closeWindowAdapter);
                new Thread(() -> {
                    String[] receiptFiles = receiptsFolder.list();
                    assert receiptFiles != null;
                    for(String file : receiptFiles){
                        Gson gson = new Gson();
                        try {
                            if (file.charAt(0) != '.') {
                                FileReader gsonFileReader = new FileReader(receiptsFolder + "/" + file);
                                Receipt originReceipt = gson.fromJson(gsonFileReader, Receipt.class);
                                gsonFileReader.close();
                                Receipt processedReceipt = new Receipt(originReceipt.get_store(), originReceipt.get_date());
                                for(ReceiptItem receiptItem : originReceipt.get_items())
                                    processedReceipt.addItem(receiptItem);
                                ReceiptsManager.saveReceipt(processedReceipt);
                                counter[0]++;
                                importStateLabel.setText(LangResource.get("imported") + " " + counter[0] + " " + LangResource.get("outOf")+ " " + Objects.requireNonNull(receiptsFolder.list()).length + ".");
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    overviewPanel.generateOverviewPanel();
                    fileChooserFrame.dispose();
                    JOptionPane.showMessageDialog(null, LangResource.get("imported") + " " + counter[0]);
                }).start();
            }
            else fileChooserFrame.dispose();
        });
    }
    private void exportReceipts(){
        JFrame fileChooserFrame = new JFrame();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooserFrame.add(fileChooser);
        fileChooserFrame.pack();
        fileChooserFrame.setResizable(false);
        fileChooserFrame.setVisible(true);
        WindowAdapter closeWindowAdapter = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                fileChooserFrame.dispose();
            }
        };
        fileChooserFrame.addWindowListener(closeWindowAdapter);
        fileChooser.addActionListener(e -> {
            final int[] counter = {0};
            if(e.getActionCommand().equals("ApproveSelection")) {
                JLabel exportStateLabel = new JLabel(LangResource.get("exported") + " " + counter[0] + " " + counter[0] + " " + LangResource.get("outOf")+ " " + ReceiptsManager.getReceipts().size() + ".");
                fileChooserFrame.getRootPane().setVisible(false);
                fileChooserFrame.remove(fileChooser);
                fileChooserFrame.add(exportStateLabel);
                fileChooserFrame.getRootPane().setVisible(true);
                fileChooserFrame.setSize(200, 75);
                fileChooserFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                fileChooserFrame.removeWindowListener(closeWindowAdapter);
                new Thread(() -> {
                    File receiptsFolder = fileChooser.getSelectedFile();
                    Gson gson = new Gson();
                    for (Receipt receipt : ReceiptsManager.getReceipts()){
                        FileWriter fw;
                        try {
                            fw = new FileWriter(receiptsFolder.getPath() + "/" + receipt.get_ID() + ".json");
                            gson.toJson(receipt, fw);
                            fw.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        counter[0]++;
                        exportStateLabel.setText(LangResource.get("exported") + " " + counter[0] + " " + counter[0] + " " + LangResource.get("outOf")+ " " + ReceiptsManager.getReceipts().size() + ".");
                    }
                    overviewPanel.generateOverviewPanel();
                    fileChooserFrame.dispose();
                    JOptionPane.showMessageDialog(null, LangResource.get("exported") + " " + counter[0]);
                }).start();
            }
            else fileChooserFrame.dispose();
        });
    }
    private void saveAs(){
        JFrame fileChooserFrame = new JFrame();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("*.json", "json"));
        fileChooser.removeChoosableFileFilter(fileChooser.getAcceptAllFileFilter());
        fileChooser.setSelectedFile(
                new File(fileChooser.getCurrentDirectory().getAbsolutePath() +
                        "\\" + "receipts.json"));
        fileChooser.addPropertyChangeListener(JFileChooser.DIRECTORY_CHANGED_PROPERTY,
                evt -> {
                    fileChooser.setSelectedFile(
                            new File(fileChooser.getCurrentDirectory().getAbsolutePath() +
                                    "\\" + "receipts.json"));
                    fileChooser.updateUI();

                });
        fileChooserFrame.add(fileChooser);
        fileChooserFrame.pack();
        fileChooserFrame.setResizable(false);
        fileChooserFrame.setVisible(true);
        WindowAdapter closeWindowAdapter = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                fileChooserFrame.dispose();
            }
        };
        fileChooserFrame.addWindowListener(closeWindowAdapter);
        fileChooser.addActionListener(e -> {
            if(e.getActionCommand().equals("ApproveSelection")) {
                JLabel exportStateLabel = new JLabel(LangResource.get("saving"));
                fileChooserFrame.getRootPane().setVisible(false);
                fileChooserFrame.remove(fileChooser);
                fileChooserFrame.add(exportStateLabel);
                fileChooserFrame.getRootPane().setVisible(true);
                fileChooserFrame.setSize(200, 75);
                fileChooserFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                fileChooserFrame.removeWindowListener(closeWindowAdapter);
                new Thread(() -> {
                    File target = fileChooser.getSelectedFile();
                    Gson gson = new Gson();
                    FileWriter fw;
                    try {
                        fw = new FileWriter(target.getPath());
                        gson.toJson(new ReceiptsPacket(ReceiptsManager.getReceipts()), fw);
                        fw.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    overviewPanel.generateOverviewPanel();
                    fileChooserFrame.dispose();
                    JOptionPane.showMessageDialog(null, LangResource.get("saved") + "!");
                }).start();
            }
            else fileChooserFrame.dispose();
        });
    }
    private void load(){
        JFrame fileChooserFrame = new JFrame();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("*.json", "json"));
        fileChooser.removeChoosableFileFilter(fileChooser.getAcceptAllFileFilter());
        fileChooser.setSelectedFile(
                new File(fileChooser.getCurrentDirectory().getAbsolutePath() +
                        "\\" + "receipts.json"));
        fileChooser.addPropertyChangeListener(JFileChooser.DIRECTORY_CHANGED_PROPERTY,
                evt -> {
                    fileChooser.setSelectedFile(
                            new File(fileChooser.getCurrentDirectory().getAbsolutePath() +
                                    "\\" + "receipts.json"));
                    fileChooser.updateUI();

                });
        fileChooserFrame.add(fileChooser);
        fileChooserFrame.pack();
        fileChooserFrame.setResizable(false);
        fileChooserFrame.setVisible(true);
        WindowAdapter closeWindowAdapter = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                fileChooserFrame.dispose();
            }
        };
        fileChooserFrame.addWindowListener(closeWindowAdapter);
        fileChooser.addActionListener(e -> {
            if(e.getActionCommand().equals("ApproveSelection")) {
                JLabel exportStateLabel = new JLabel(LangResource.get("loading"));
                fileChooserFrame.getRootPane().setVisible(false);
                fileChooserFrame.remove(fileChooser);
                fileChooserFrame.add(exportStateLabel);
                fileChooserFrame.getRootPane().setVisible(true);
                fileChooserFrame.setSize(200, 75);
                fileChooserFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                fileChooserFrame.removeWindowListener(closeWindowAdapter);
                new Thread(() -> {
                    File target = fileChooser.getSelectedFile();
                    ReceiptsManager.refreshItemsAndReceipts(target.getPath());
                    overviewPanel.generateOverviewPanel();
                    fileChooserFrame.dispose();
                    JOptionPane.showMessageDialog(null, LangResource.get("loaded") + "!");
                }).start();
            }
            else fileChooserFrame.dispose();
        });
    }
    private JMenuBar menuBar(){
        JMenuBar menuBar = new JMenuBar();
        //*****
        JMenu fileMenu = new JMenu(LangResource.get("file"));
        JMenu preferences = new JMenu(LangResource.get("preferences"));
        JMenu language = new JMenu(LangResource.get("language"));
        JMenu appearance = new JMenu(LangResource.get("appearance"));
        JMenu currency = new JMenu(LangResource.get("currency"));
        //*****
        JMenuItem importReceipts = new JMenuItem(LangResource.get("importReceipts"));
        JMenuItem exportReceipts = new JMenuItem(LangResource.get("exportReceipts"));
        JMenuItem save = new JMenuItem(LangResource.get("save"));
        JMenuItem quit = new JMenuItem(LangResource.get("quit"));
        JMenuItem saveAs = new JMenuItem(LangResource.get("saveAs"));
        JMenuItem load = new JMenuItem(LangResource.get("open"));
        //*****
        JCheckBoxMenuItem eng = new JCheckBoxMenuItem("English");
        JCheckBoxMenuItem pol = new JCheckBoxMenuItem("Polish");
        JCheckBoxMenuItem usd = new JCheckBoxMenuItem("USD");
        JCheckBoxMenuItem pln = new JCheckBoxMenuItem("PLN");
        //*****
        JMenuItem darkMode = new JMenuItem(LangResource.get("darkMode"));
        JMenuItem lightMode = new JMenuItem(LangResource.get("lightMode"));
        JMenuItem darkModeSec = new JMenuItem(LangResource.get("darkModeSec"));
        JMenuItem intellij = new JMenuItem(LangResource.get("intellij"));
        JMenuItem metal = new JMenuItem(LangResource.get("metal"));
        JMenuItem windowsClassic = new JMenuItem(LangResource.get("windowsClassic"));
        JMenuItem windows = new JMenuItem(LangResource.get("windows"));
        JMenuItem nimbus = new JMenuItem(LangResource.get("nimbus"));
        JMenuItem motif = new JMenuItem(LangResource.get("motif"));
        //*****
        //set selected language and currency
        switch(Init.localeBundle.getLocale().getLanguage()){
            case "english" : {eng.setState(true); break;}
            case "polish" : {pol.setState(true); break;}
        }
        switch(Init.settingsProp.getProperty("currency")){
            case "USD" : {usd.setState(true); break;}
            case "PLN" : {pln.setState(true); break;}
        }
        //*****
        //what happens when currency and language changes
        ActionListener changeCurrency = e -> {
            Init.settingsProp.setProperty("currency", ((JCheckBoxMenuItem)e.getSource()).getText());
            Init.reloading = true;
            dispose();
            Init.loadFrame();
        };
        ActionListener changeLanguage = e -> {
            Locale newLocale = new Locale(((JCheckBoxMenuItem)e.getSource()).getText());
            Init.settingsProp.setProperty("language", ((JCheckBoxMenuItem)e.getSource()).getText());
            Init.localeBundle = ResourceBundle.getBundle("lang", newLocale);
            Init.reloading = true;
            dispose();
            Init.loadFrame();
        };
        //*****
        //TODO - combine to one function (languages and currency)
        //add all listeners
        usd.addActionListener(changeCurrency);
        pln.addActionListener(changeCurrency);
        pol.addActionListener(changeLanguage);
        eng.addActionListener(changeLanguage);
        save.addActionListener(e -> save());
        importReceipts.addActionListener(e -> importReceipts());
        exportReceipts.addActionListener(e -> exportReceipts());
        quit.addActionListener(e -> quitAction());
        saveAs.addActionListener(e -> saveAs());
        load.addActionListener(e-> load());
        darkMode.addActionListener(e->{
            try {
                UIManager.setLookAndFeel( new FlatDarculaLaf());
                Init.reloading = true;
                dispose();
                Init.loadFrame();
            } catch (UnsupportedLookAndFeelException ex) {
                ex.printStackTrace();
            }
        });
        lightMode.addActionListener(e->{
            try {
                UIManager.setLookAndFeel( new FlatLightLaf());
                Init.reloading = true;
                dispose();
                Init.loadFrame();
            } catch (UnsupportedLookAndFeelException ex) {
                ex.printStackTrace();
            }
        });
        darkModeSec.addActionListener(e->{
            try {
                UIManager.setLookAndFeel(new FlatDarkLaf());
                Init.reloading = true;
                dispose();
                Init.loadFrame();
            } catch (UnsupportedLookAndFeelException ex) {
                ex.printStackTrace();
            }
        });
        intellij.addActionListener(e->{
            try {
                UIManager.setLookAndFeel(new FlatIntelliJLaf());
                Init.reloading = true;
                dispose();
                Init.loadFrame();
            } catch (UnsupportedLookAndFeelException ex) {
                ex.printStackTrace();
            }
        });
        metal.addActionListener(e->{
            try {
                UIManager.setLookAndFeel(new MetalLookAndFeel());
                Init.reloading = true;
                dispose();
                Init.loadFrame();
            } catch (UnsupportedLookAndFeelException ex) {
                ex.printStackTrace();
            }
        });
        windowsClassic.addActionListener(e->{
            try {
                UIManager.setLookAndFeel(new WindowsClassicLookAndFeel());
                Init.reloading = true;
                dispose();
                Init.loadFrame();
            } catch (UnsupportedLookAndFeelException ex) {
                ex.printStackTrace();
            }
        });
        windows.addActionListener(e->{
            try {
                UIManager.setLookAndFeel(new WindowsLookAndFeel());
                Init.reloading = true;
                dispose();
                Init.loadFrame();
            } catch (UnsupportedLookAndFeelException ex) {
                ex.printStackTrace();
            }
        });
        nimbus.addActionListener(e->{
            try {
                UIManager.setLookAndFeel(new NimbusLookAndFeel());
                Init.reloading = true;
                dispose();
                Init.loadFrame();
            } catch (UnsupportedLookAndFeelException ex) {
                ex.printStackTrace();
            }
        });
        motif.addActionListener(e->{
            try {
                UIManager.setLookAndFeel(new MotifLookAndFeel());
                Init.reloading = true;
                dispose();
                Init.loadFrame();
            } catch (UnsupportedLookAndFeelException ex) {
                ex.printStackTrace();
            }
        });
        //*****
        fileMenu.add(load);
        fileMenu.add(save);
        fileMenu.add(saveAs);
        fileMenu.add(importReceipts);
        fileMenu.add(exportReceipts);
        fileMenu.add(quit);
        //*****
        language.add(eng);
        language.add(pol);
        //*****
        appearance.add(darkMode);
        appearance.add(darkModeSec);
        appearance.add(lightMode);
        appearance.add(intellij);
        appearance.add(metal);
        appearance.add(windowsClassic);
        appearance.add(windows);
        appearance.add(nimbus);
        appearance.add(motif);
        //*****
        currency.add(usd);
        currency.add(pln);
        //*****
        preferences.add(appearance);
        preferences.add(language);
        preferences.add(currency);
        //*****
        menuBar.add(fileMenu);
        menuBar.add(preferences);
        return menuBar;
    }
}
