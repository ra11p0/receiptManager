package com.ra11p0.GUI.ReceiptsManager.Dialogs;

import com.ra11p0.App;
import com.ra11p0.LocaleBundle;
import org.jdesktop.swingx.VerticalLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StoreSelectDialog {
    public static String ShowInputDialog(){
        JPanel panel = new JPanel();
        panel.setLayout(new VerticalLayout());
        JComboBox<String> storesComboBox = new JComboBox<>();
        JButton addNewStoreButton = new JButton(LocaleBundle.get("addNewStore"));
        for(String store : App.dataAccessObject.getStores()) storesComboBox.addItem(store);

        addNewStoreButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String newStoreName = JOptionPane.showInputDialog(null,LocaleBundle.get("nameTheNewStore") + ":" ,LocaleBundle.get("addNewStore") + "." , JOptionPane.QUESTION_MESSAGE);
                if(newStoreName != null | !newStoreName.isBlank()) {
                    storesComboBox.addItem(newStoreName);
                    storesComboBox.setSelectedItem(newStoreName);
                }
            }
        });

        panel.add(new Label( LocaleBundle.get("selectStore")+ ":"));
        panel.add(storesComboBox);
        panel.add(addNewStoreButton);
        int response = JOptionPane.showConfirmDialog(null, panel,LocaleBundle.get("selectStore") + ".", JOptionPane.OK_CANCEL_OPTION);
        if(response == JOptionPane.OK_OPTION){
            return (String) storesComboBox.getSelectedItem();
        }
        else{
            return null;
        }
    }
}
