package com.ra11p0.GUI.ReceiptEditor.Dialogs;

import com.ra11p0.LocaleBundle;
import com.ra11p0.Classes.ReceiptItem;

import javax.swing.*;
import java.util.ArrayList;

public class SelectItemDialog {
    public static ReceiptItem ShowInputDialog(ArrayList<ReceiptItem> items){
        JComboBox<ReceiptItem> itemsList = new JComboBox<>();
        for(ReceiptItem receiptItem : items) itemsList.addItem(receiptItem);
        int response = JOptionPane.showConfirmDialog(null, itemsList, LocaleBundle.get("removeItem") + ".", JOptionPane.OK_CANCEL_OPTION);
        if(response == JOptionPane.OK_OPTION){
            return (ReceiptItem) itemsList.getSelectedItem();
        }
        else{
            return null;
        }
    }
}
