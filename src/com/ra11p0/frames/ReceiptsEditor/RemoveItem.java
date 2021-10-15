package com.ra11p0.frames.ReceiptsEditor;

import com.ra11p0.structures.Item;
import com.ra11p0.structures.Receipt;
import com.ra11p0.structures.ReceiptItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class RemoveItem {
    public static void showDialog(ArrayList<Item> items, Receipt receipt, JPanel panel, JPanel tablePanel, ReceiptEditor receiptEditor){
        JFrame removeItemFrame = new JFrame("Remove item.");
        JComboBox<ReceiptItem> itemsList = new JComboBox<>();
        for(ReceiptItem receiptItem:receipt.get_items()) itemsList.addItem(receiptItem);
        Button removeButton = new Button("Remove.");
        removeButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                receipt.removeItem((ReceiptItem) itemsList.getSelectedItem());
                receiptEditor.repaintFrame(panel, tablePanel, receipt, items);
                removeItemFrame.setVisible(false);
                removeItemFrame.dispose();
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
        removeItemFrame.setLayout(new GridLayout(3, 1));
        removeItemFrame.setSize(200, 200);
        removeItemFrame.add(new Label("Select item to remove: "));
        removeItemFrame.add(itemsList);
        removeItemFrame.add(removeButton);
        removeItemFrame.setVisible(true);
    }
}
