package com.ra11p0.frames.ReceiptsManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class AddNewStore extends JFrame {
    public static void showDialog(ArrayList<String> storesList, JComboBox<String> stores){
        JFrame addNewStoreFrame = new JFrame();
        JTextField nameOfNewStore = new JTextField();
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
        JButton confirm = new JButton("Confirm");
        //CONFIRM BUTTON
        confirm.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                confirm(storesList, nameOfNewStore, stores);
                addNewStoreFrame.setVisible(false);
                addNewStoreFrame.dispose();
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
    private static void confirm(ArrayList<String> storesList, JTextField nameOfNewStore, JComboBox<String> stores){
        if (!storesList.contains(nameOfNewStore.getText())){
            stores.removeAllItems();
            storesList.add(nameOfNewStore.getText());
            for(String store : storesList) stores.addItem(store);
            stores.setSelectedIndex(stores.getItemCount()-1);
        }
    }
}
