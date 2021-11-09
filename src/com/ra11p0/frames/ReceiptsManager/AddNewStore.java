package com.ra11p0.frames.ReceiptsManager;

import com.ra11p0.frames.HomeFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AddNewStore extends JFrame {
    private final static ResourceBundle locale = HomeFrame.localeBundle;
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
        JButton confirm = new JButton(locale.getString("confirm"));
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
        addNewStoreFrame.setTitle(locale.getString("addNewStore"));
        addNewStoreFrame.add(new Label(locale.getString("nameTheNewStore") + ":"));
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
