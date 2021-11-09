package com.ra11p0.frames.ReceiptsEditor;

import com.ra11p0.frames.HomeFrame;
import com.ra11p0.structures.Receipt;
import com.ra11p0.structures.ReceiptItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;

public class RemoveItem {
    private static final ResourceBundle locale = HomeFrame.localeBundle;
    public static void showDialog(Receipt receipt, ReceiptEditor receiptEditor){
        JFrame removeItemFrame = new JFrame(locale.getString("removeItem"));
        JComboBox<ReceiptItem> itemsList = new JComboBox<>();
        JButton removeJButton = new JButton(locale.getString("remove"));
        for(ReceiptItem receiptItem:receipt.get_items()) itemsList.addItem(receiptItem);
        removeJButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                receipt.removeItem((ReceiptItem) itemsList.getSelectedItem());
                receiptEditor.repaintFrame();
                removeItemFrame.setVisible(false);
                removeItemFrame.dispose();
            }
        });
        //*****
        removeItemFrame.setLayout(new GridLayout(3, 1));
        removeItemFrame.setSize(200, 200);
        removeItemFrame.add(new Label(locale.getString("selectItemToRemove") + ": "));
        removeItemFrame.add(itemsList);
        removeItemFrame.add(removeJButton);
        removeItemFrame.setVisible(true);
    }
}
