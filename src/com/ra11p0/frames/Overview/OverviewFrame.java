package com.ra11p0.frames.Overview;

import javax.swing.*;

public class OverviewFrame extends JFrame {
    public OverviewFrame(){
        //setResizable(false);
        JFrame currentFrame = this;
        setSize(800, 450);
        add(new OverviewPanel());
        setVisible(true);
    }
    public void showEditor(){

    }
}
