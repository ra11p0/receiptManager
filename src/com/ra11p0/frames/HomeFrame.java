package com.ra11p0.frames;

import com.ra11p0.dialogs.CreateOrEditReceiptDialog;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class HomeFrame extends JFrame {
    public HomeFrame(String title, int width, int height){
        GridLayout layout = new GridLayout(3,1);
        setTitle(title);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(width, height);
        setLayout(layout);
    }
    public void build(){
        Button add = new Button("Receipt manager.");
        add.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new CreateOrEditReceiptDialog();
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
        Button overview = new Button("Show overview.");
        Button stats = new Button("Show stats.");
        add(add);
        add(overview);
        add(stats);
        setVisible(true);
    }
}
