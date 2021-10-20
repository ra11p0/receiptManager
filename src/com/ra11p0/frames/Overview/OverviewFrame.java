package com.ra11p0.frames.Overview;

import javax.swing.*;

public class OverviewFrame extends JFrame {
    public OverviewFrame (String title) throws Exception{
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        //*****
        setSize(800, 375);
        setResizable(false);
        setTitle(title);
        add(new OverviewPanel());
        setVisible(true);
    }
}
