package com.ra11p0.frames.ReceiptsEditor;

import com.ra11p0.structures.Item;
import com.ra11p0.structures.Receipt;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class EditDate {
    public static void showDialog(ArrayList<Item> items, Receipt receipt, JPanel panel, JPanel tablePanel, ReceiptEditor receiptEditor){

        JFrame datePickerFrame = new JFrame("Date picker.");
        UtilDateModel model = new UtilDateModel();
        JDatePanelImpl datePanel = new JDatePanelImpl(model);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel);
        Button confirm = new Button("Confirm.");
        confirm.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                receipt.setNewId(model.getValue());
                datePickerFrame.setVisible(false);
                receiptEditor.repaintFrame(panel, tablePanel, receipt, items);
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
        datePickerFrame.setLayout(new GridLayout(3, 1));
        datePickerFrame.setSize(200, 200);
        datePickerFrame.add(new Label("Pick the date: "));
        datePickerFrame.add(datePicker);
        datePickerFrame.add(confirm);
        datePickerFrame.setVisible(true);
    }
}
