package com.ra11p0.frames.ReceiptsEditor;

import com.ra11p0.structures.Receipt;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class EditDate {
    public static void showDialog(Receipt receipt, ReceiptEditor receiptEditor){

        JFrame datePickerFrame = new JFrame("Date picker.");
        UtilDateModel model = new UtilDateModel();
        JDatePanelImpl datePanel = new JDatePanelImpl(model);
        //JDatePickerImpl datePicker = new JDatePickerImpl(datePanel);
        JButton confirm = new JButton("Confirm.");
        confirm.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                receipt.setNewId(model.getValue());
                datePickerFrame.setVisible(false);
                datePickerFrame.dispose();
                receiptEditor.repaintFrame();
            }
        });
        datePickerFrame.setLayout(new FlowLayout());
        datePanel.setPreferredSize(new Dimension(200, 185));
        datePickerFrame.setSize(250, 300);
        datePickerFrame.add(new Label("Pick the date: "));
        datePickerFrame.add(datePanel);
        datePickerFrame.add(confirm);
        datePickerFrame.setVisible(true);
    }
}
