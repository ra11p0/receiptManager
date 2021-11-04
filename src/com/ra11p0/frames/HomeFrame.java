package com.ra11p0.frames;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.ra11p0.frames.Overview.Frames.OverviewFrame;
import com.ra11p0.frames.ReceiptsManager.ReceiptsManager;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class HomeFrame extends JFrame {
    private final String DEFAULT_RECEIPT_PATH = "res\\receipts.json";
    public HomeFrame(String title, int width, int height) throws Exception{
        UIManager.setLookAndFeel( new FlatDarculaLaf());
        JFrame preparingFiles = new JFrame();
        preparingFiles.setResizable(false);
        preparingFiles.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        preparingFiles.setLayout(new BorderLayout());
        preparingFiles.setSize(200, 100);
        preparingFiles.add(new JLabel("Wait until all files are processed..."), BorderLayout.CENTER);
        preparingFiles.setVisible(true);
        new Thread(() -> {
            File temp = new File("res/.temp");
            if(temp.exists()) {
                try {
                    FileUtils.cleanDirectory(temp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                temp.delete();
            }
            File source = new File("res/receipts.json");
            if(!source.exists()) {
                byte[] strToBytes = "".getBytes();
                try {
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
            try {
                OverviewFrame overviewFrame = new OverviewFrame(title);
                overviewFrame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        System.exit(0);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            dispose();
        }).start();
    }
}