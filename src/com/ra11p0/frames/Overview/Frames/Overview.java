package com.ra11p0.frames.Overview.Frames;

import com.google.gson.Gson;
import com.ra11p0.frames.HomeFrame;
import com.ra11p0.frames.Overview.Panels.OverviewPanel;
import com.ra11p0.frames.ReceiptsManager.ReceiptsManager;
import com.ra11p0.structures.*;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Function;

public class Overview extends JFrame {
    private final OverviewPanel overviewPanel;
    private final static ResourceBundle locale = HomeFrame.localeBundle;
    public Overview(String title) {
        JPanel mainPanel = new JPanel();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                quitAction();
            }
        });
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu(locale.getString("file"));
        JMenuItem importReceipts = new JMenuItem(locale.getString("importReceipts"));
        JMenuItem exportReceipts = new JMenuItem(locale.getString("exportReceipts"));
        JMenuItem save = new JMenuItem(locale.getString("save"));
        JMenuItem quit = new JMenuItem(locale.getString("quit"));
        JMenuItem saveAs = new JMenuItem(locale.getString("saveAs"));
        JMenuItem load = new JMenuItem(locale.getString("open"));
        save.addActionListener(e -> save());
        importReceipts.addActionListener(e -> importReceipts());
        exportReceipts.addActionListener(e -> exportReceipts());
        quit.addActionListener(e -> quitAction());
        saveAs.addActionListener(e -> saveAs());
        load.addActionListener(e-> load());
        fileMenu.add(load);
        fileMenu.add(save);
        fileMenu.add(saveAs);
        fileMenu.add(importReceipts);
        fileMenu.add(exportReceipts);
        fileMenu.add(quit);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
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
        filesProcessing.add(new JLabel(locale.getString("waitUntilAllFilesAreProcessed")), BorderLayout.CENTER);
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
                locale.getString("doYouWantToSaveTheChanges") + "?" , "",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, new Object[]{locale.getString("no"), locale.getString("yes")},
                locale.getString("no"));
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
                    JOptionPane.showMessageDialog(null, locale.getString("failedToProcessReceipts") + "!", locale.getString("error"), JOptionPane.ERROR_MESSAGE);
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
        JOptionPane.showMessageDialog(null, locale.getString("saved") + "!");
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
                JLabel importStateLabel = new JLabel(locale.getString("imported") + " " + counter[0] + " " + locale.getString("outOf")+ " " + Objects.requireNonNull(receiptsFolder.list()).length + ".");
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
                                importStateLabel.setText(locale.getString("imported") + " " + counter[0] + " " + locale.getString("outOf")+ " " + Objects.requireNonNull(receiptsFolder.list()).length + ".");
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    overviewPanel.generateOverviewPanel();
                    fileChooserFrame.dispose();
                    JOptionPane.showMessageDialog(null, locale.getString("imported") + " " + counter[0]);
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
                JLabel exportStateLabel = new JLabel(locale.getString("exported") + " " + counter[0] + " " + counter[0] + " " + locale.getString("outOf")+ " " + ReceiptsManager.getReceipts().size() + ".");
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
                        exportStateLabel.setText(locale.getString("exported") + " " + counter[0] + " " + counter[0] + " " + locale.getString("outOf")+ " " + ReceiptsManager.getReceipts().size() + ".");
                    }
                    overviewPanel.generateOverviewPanel();
                    fileChooserFrame.dispose();
                    JOptionPane.showMessageDialog(null, locale.getString("exported") + " " + counter[0]);
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
                JLabel exportStateLabel = new JLabel(locale.getString("saving"));
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
                    JOptionPane.showMessageDialog(null, locale.getString("saved") + "!");
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
                JLabel exportStateLabel = new JLabel(locale.getString("loading"));
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
                    JOptionPane.showMessageDialog(null, locale.getString("loaded") + "!");
                }).start();
            }
            else fileChooserFrame.dispose();
        });
    }
}
