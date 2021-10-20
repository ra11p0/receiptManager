package com.ra11p0.frames;

import com.ra11p0.frames.Overview.OverviewFrame;
import com.ra11p0.frames.ReceiptsManager.ReceiptsManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

public class HomeFrame extends JFrame {
    public HomeFrame(String title, int width, int height){
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
        add.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ReceiptsManager.showDialog();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        overview.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new OverviewFrame("");
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        add(add);
        add(overview);
        add(stats);
        setVisible(true);
    }
}
