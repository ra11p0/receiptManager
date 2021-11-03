package com.ra11p0.frames;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.ra11p0.frames.Overview.Frames.OverviewFrame;
import com.ra11p0.frames.ReceiptsManager.ReceiptsManager;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class HomeFrame extends JFrame {
    public HomeFrame(String title, int width, int height) throws Exception{
        UIManager.setLookAndFeel( new FlatDarculaLaf());
        JFrame preparingFiles = new JFrame();
        preparingFiles.setLayout(new BorderLayout());
        preparingFiles.setSize(200, 100);
        preparingFiles.add(new JLabel("Wait until all files are processed..."), BorderLayout.CENTER);
        preparingFiles.setVisible(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                File temp = new File("res/.temp");
                if(temp.exists()) {
                    try {
                        FileUtils.cleanDirectory(temp);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    temp.delete();
                }
                File source = new File("res/receipts");
                try {
                    FileUtils.copyDirectoryToDirectory(source, temp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ReceiptsManager.refreshItemsAndReceipts();
                //*****
                preparingFiles.dispose();
                try {
                    new OverviewFrame(title);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dispose();
            }
        }).start();
        //***** DISABLED FRAME
        /*
        File file = new File("res");
        boolean mkdirStatus = true;
        if (!file.exists())
            mkdirStatus = file.mkdir();
        file = new File("res/receipts");
        if (!file.exists())
            mkdirStatus  = file.mkdir();
        if(!mkdirStatus) JOptionPane.showMessageDialog(null,
                "Error!","Error while creating necessary catalogs.",
                JOptionPane.ERROR_MESSAGE);
        setTitle(title);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(width, height);
        setLayout(new GridLayout(3,1));
        */
    }
    public void build(){
        Button add = new Button("Receipt manager.");
        Button overview = new Button("Show overview.");
        Button stats = new Button("Show stats.");
        add.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ReceiptsManager.showDialog();
            }
        });
        overview.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    new OverviewFrame("");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        add(add);
        add(overview);
        add(stats);
        setVisible(true);
    }
}