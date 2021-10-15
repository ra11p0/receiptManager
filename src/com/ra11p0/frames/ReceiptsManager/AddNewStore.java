package com.ra11p0.frames.ReceiptsManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class AddNewStore extends JFrame {
    public static void showDialog(ArrayList<String> storesList, JComboBox<String> stores){
        JFrame addNewStoreFrame = new JFrame();
        TextField nameOfNewStore = new TextField();
        //ENTER -> CONFIRM
        nameOfNewStore.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    confirm(storesList, nameOfNewStore, stores);
                    addNewStoreFrame.setVisible(false);
                    addNewStoreFrame.dispose();
                }
            }
        });
        Button confirm = new Button("Confirm");
        //CONFIRM BUTTON
        confirm.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                confirm(storesList, nameOfNewStore, stores);
                addNewStoreFrame.setVisible(false);
                addNewStoreFrame.dispose();
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
        addNewStoreFrame.setLayout(new GridLayout());
        addNewStoreFrame.setSize(400, 65);
        addNewStoreFrame.setTitle("Add new store.");
        addNewStoreFrame.add(new Label("Name the new store:"));
        addNewStoreFrame.add(nameOfNewStore);
        addNewStoreFrame.add(confirm);
        addNewStoreFrame.setVisible(true);
    }
    private static void confirm(ArrayList<String> storesList, TextField nameOfNewStore, JComboBox<String> stores){
        if (!storesList.contains(nameOfNewStore.getText())){
            stores.removeAllItems();
            storesList.add(nameOfNewStore.getText());
            for(String store : storesList) stores.addItem(store);
            stores.setSelectedIndex(stores.getItemCount()-1);
        }
    }
}
