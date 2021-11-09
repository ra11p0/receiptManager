package com.ra11p0.frames.ReceiptsEditor;

import com.ra11p0.frames.HomeFrame;
import com.ra11p0.structures.Receipt;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;

public class EditDate {
    private final static ResourceBundle locale = HomeFrame.localeBundle;
    public static void showDialog(Receipt receipt, ReceiptEditor receiptEditor){
        JFrame datePickerFrame = new JFrame(locale.getString("datePicker") + ".");
        UtilDateModel model = new UtilDateModel();
        JDatePanelImpl datePanel = new JDatePanelImpl(model);
        //JDatePickerImpl datePicker = new JDatePickerImpl(datePanel);
        JButton confirm = new JButton(locale.getString("confirm"));
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
        datePickerFrame.add(new Label(locale.getString("pickTheDate") + ": "));
        datePickerFrame.add(datePanel);
        datePickerFrame.add(confirm);
        datePickerFrame.setVisible(true);
    }
}
