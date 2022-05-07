package com.ra11p0.GUI.ReceiptEditor.Dialogs;

import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.swing.AutoCompleteSupport;
import com.ra11p0.App;
import com.ra11p0.Interfaces.IItem;
import com.ra11p0.LocaleBundle;
import com.ra11p0.Classes.Item;
import org.jdesktop.swingx.VerticalLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Objects;

public class CreateItemDialog {
    public static Item ShowInputDialog() throws NullPointerException{
        JPanel createItemDialogPanel = new JPanel();
        createItemDialogPanel.setLayout(new VerticalLayout());
        ArrayList<String> namesOfProductsArray = new ArrayList<>(App.dataAccessObject.getNamesOfItems());
        JComboBox<Object> nameComboBox = new JComboBox<>();
        JComboBox<Float> taxComboBox = new JComboBox<>();
        JTextField priceTextField = new JTextField();

        AutoCompleteSupport.install(nameComboBox,
                GlazedLists.eventListOf(namesOfProductsArray.toArray()));

        nameComboBox.addItemListener(e -> {
            if(nameComboBox.getSelectedItem() == null) return;
            for(IItem _item : App.dataAccessObject.getReceiptItems())
                if(_item.name().get().equals(nameComboBox.getSelectedItem())){
                    taxComboBox.setSelectedItem(_item.tax().get());
                    break;
                }
        });
        //TAX RATE BEHAVIOR
        taxComboBox.addItem(0.23F);
        taxComboBox.addItem(0.08F);
        taxComboBox.addItem(0.05F);
        taxComboBox.addItem(0F);

        taxComboBox.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
            DefaultListCellRenderer renderer = new DefaultListCellRenderer();
            Component component = renderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            renderer.setText(String.format("%.0f", value*100) + "%");
            return component;
        });
        //PRICE PARSE CHECK
        priceTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                priceTextField.setText(priceTextField.getText().replace(",", "."));
                try{
                    Float.parseFloat(priceTextField.getText());
                }catch(Exception ex){
                    priceTextField.setText("");
                }
            }
        });
        //*****
        createItemDialogPanel.add(new Label(LocaleBundle.get("nameOfNewItem") + ":"));
        createItemDialogPanel.add(nameComboBox);
        createItemDialogPanel.add(new Label(LocaleBundle.get("costOfOneUnit") + ":"));
        createItemDialogPanel.add(priceTextField);
        createItemDialogPanel.add(new Label(LocaleBundle.get("taxRate") + ":"));
        createItemDialogPanel.add(taxComboBox);
        int response = JOptionPane.showConfirmDialog(null, createItemDialogPanel,LocaleBundle.get("createNewItem") + ".", JOptionPane.OK_CANCEL_OPTION);
        if(response == JOptionPane.OK_OPTION){
            return new Item(
                    Objects.requireNonNull(nameComboBox.getSelectedItem()).toString(),
                    Objects.requireNonNull((Float)taxComboBox.getSelectedItem()),
                    Float.parseFloat(priceTextField.getText())
            );
        }
        else{
            return null;
        }
    }
}
