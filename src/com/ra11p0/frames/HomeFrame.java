package com.ra11p0.frames;

import com.ra11p0.frames.Overview.OverviewFrame;
import com.ra11p0.frames.ReceiptsManager.ReceiptsManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HomeFrame extends JFrame {
    public HomeFrame(String title, int width, int height) throws Exception{
        //*****
        new OverviewFrame(title);
        dispose();
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
