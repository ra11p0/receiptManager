package com.ra11p0.frames.Overview.Frames;

import com.google.gson.Gson;
import com.ra11p0.frames.Overview.Panels.OverviewPanel;
import com.ra11p0.frames.ReceiptsManager.ReceiptsManager;
import com.ra11p0.structures.*;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.function.Function;

public class OverviewFrame extends JFrame {
    private final OverviewPanel overviewPanel;
    public OverviewFrame (String title) {
        JPanel mainPanel = new JPanel();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                quitAction();
            }
        });
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem importReceipts = new JMenuItem("Import receipts.");
        JMenuItem exportReceipts = new JMenuItem("Export receipts.");
        JMenuItem save = new JMenuItem("Save.");
        JMenuItem quit = new JMenuItem("Quit.");
        save.addActionListener(e -> save());
        importReceipts.addActionListener(e -> importReceipts());
        exportReceipts.addActionListener(e -> exportReceipts());
        quit.addActionListener(e -> quitAction());
        fileMenu.add(save);
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
        overviewPanel = new OverviewPanel();
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
        filesProcessing.add(new JLabel("Wait until all files are processed..."), BorderLayout.CENTER);
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
                "Do you want to save the changes?" , "",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, new Object[]{"NO", "YES"},
                "NO");
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
                    JOptionPane.showMessageDialog(null, "Failed to process receipts!", "Error", JOptionPane.ERROR_MESSAGE);
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
        new File("res/receipts.json").delete();
        Gson gson = new Gson();
        FileWriter fw = null;
        try {
            fw = new FileWriter("res/receipts.json");
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
        JOptionPane.showMessageDialog(null, "Saved!");
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
                JLabel importStateLabel = new JLabel("Imported " + counter[0] + " out of " + Objects.requireNonNull(receiptsFolder.list()).length + ".");
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
                                importStateLabel.setText("Imported " + counter[0] + " out of " + Objects.requireNonNull(receiptsFolder.list()).length + ".");
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    overviewPanel.generateOverviewPanel();
                    fileChooserFrame.dispose();
                    JOptionPane.showMessageDialog(null, "Imported " + counter[0] + " receipts.");
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
                JLabel exportStateLabel = new JLabel("Exported " + counter[0] + " out of " + ReceiptsManager.getReceipts().size() + ".");
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
                        exportStateLabel.setText("Exported " + counter[0] + " out of " + ReceiptsManager.getReceipts().size() + ".");
                    }
                    overviewPanel.generateOverviewPanel();
                    fileChooserFrame.dispose();
                    JOptionPane.showMessageDialog(null, "Exported " + counter[0] + " receipts.");
                }).start();
            }
            else fileChooserFrame.dispose();
        });
    }
}
