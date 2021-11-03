package com.ra11p0.frames.Overview.Frames;

import com.ra11p0.frames.Overview.Panels.OverviewPanel;
import com.ra11p0.frames.ReceiptsManager.ReceiptsManager;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.function.Function;

public class OverviewFrame extends JFrame {
    JPanel mainPanel = new JPanel();
    JFrame current = this;
    public OverviewFrame (String title) throws Exception{
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                current.setVisible(false);
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
                    if((int)choice != 1) {
                        File changedReceipts = new File("res/receipts");
                        if(changedReceipts.exists()) {
                            try {
                                FileUtils.cleanDirectory(changedReceipts);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                            changedReceipts.delete();
                        }
                        File notChangedReceipts = new File("res/.temp/receipts");
                        try {
                            FileUtils.copyDirectoryToDirectory(notChangedReceipts, changedReceipts.getParentFile());
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(null, "Failed to process receipts!", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                    removeTemp.apply(null);
                    current.dispose();
                    filesProcessing.dispose();
                }).start();
            }
        });
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        //*****
        setSize(800, 375);
        setResizable(false);
        setTitle(title);
        mainPanel.setLayout(new GridLayout(1, 1));
        mainPanel.add(new OverviewPanel());
        add(mainPanel);
        setVisible(true);
    }
}
