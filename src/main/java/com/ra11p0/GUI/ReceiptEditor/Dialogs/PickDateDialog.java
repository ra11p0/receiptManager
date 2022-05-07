package com.ra11p0.GUI.ReceiptEditor.Dialogs;

import com.ra11p0.LocaleBundle;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class PickDateDialog {
    public static Date ShowInputDialog(){
        UtilDateModel model = new UtilDateModel();
        JDatePanelImpl datePanel = new JDatePanelImpl(model);
        datePanel.setPreferredSize(new Dimension(datePanel.getPreferredSize().width+50, datePanel.getPreferredSize().height+35));
        int response = JOptionPane.showConfirmDialog(null, datePanel, LocaleBundle.get("datePicker") + ".", JOptionPane.OK_CANCEL_OPTION);
        if(response == JOptionPane.OK_OPTION){
            return model.getValue();
        }
        else{
            return null;
        }
    }
}
