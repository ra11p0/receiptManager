package com.ra11p0.GUI.ReceiptEditor.Dialogs;

import com.ra11p0.App;
import com.ra11p0.Interfaces.IItem;
import com.ra11p0.LocaleBundle;
import com.ra11p0.Classes.Item;
import com.ra11p0.Classes.ReceiptItem;
import com.ra11p0.Models.ItemModel;
import com.ra11p0.Models.ReceiptItemModel;
import org.jdesktop.swingx.VerticalLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

public class AddItemDialog{
    public static ReceiptItem ShowInputDialog(String store){
        ArrayList<ReceiptItem> items = new ArrayList<>( App.dataAccessObject.getReceiptItems());
        items = items.stream().filter(e -> e.store().get().equals(store)).collect(Collectors.toCollection(ArrayList::new));
        JPanel addItemPanel = new JPanel();
        JComboBox<IItem> itemsComboBox = new JComboBox<>();
        items.forEach(itemsComboBox::addItem);
        JTextField qty = new JTextField();
        JTextField searchBar = new JTextField();
        addItemPanel.setLayout(new VerticalLayout());
        //SEARCH BAR BEHAVIOR
        ArrayList<ReceiptItem> finalItems = items;
        searchBar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                int i = 0;
                itemsComboBox.removeAllItems();
                if (searchBar.getText().length()==0){ for(ItemModel item : finalItems) itemsComboBox.addItem(item); i++;}
                else for(ItemModel item : finalItems) if(item.name().get().toUpperCase(Locale.ROOT).contains(searchBar.getText().toUpperCase(Locale.ROOT))) {itemsComboBox.addItem(item); i++;}
                if(i>0) searchBar.setBackground(null);
                else searchBar.setBackground(Color.RED);
                itemsComboBox.setPopupVisible(false);
                itemsComboBox.showPopup();
                itemsComboBox.setPopupVisible(true);
            }
        });
        //QTY PARSE CHECK
        qty.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                qty.setText(qty.getText().replace(",", "."));
                try{
                    Float.parseFloat(qty.getText());
                }catch(Exception ex){
                    qty.setText("");
                }
            }
        });
        JButton newItem = new JButton(LocaleBundle.get("createNewItem"));
        //NEW ITEM DIALOG
        newItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Item item = CreateItemDialog.ShowInputDialog();
                if(item != null) {
                    item.store().set(store);
                    itemsComboBox.addItem(item);
                    itemsComboBox.setSelectedItem(item);
                }
            }
        });
        addItemPanel.add(new Label(LocaleBundle.get("selectItemOrCreateNewOneThenTypeQty") + ":"));
        addItemPanel.add(new Label(LocaleBundle.get("search") + ":"));
        addItemPanel.add(searchBar);
        addItemPanel.add(itemsComboBox);
        addItemPanel.add(new Label(LocaleBundle.get("qty") + ":"));
        addItemPanel.add(qty);
        addItemPanel.add(newItem);
        int response = JOptionPane.showConfirmDialog(null, addItemPanel,LocaleBundle.get("addItem") + ".", JOptionPane.OK_CANCEL_OPTION);
        if(response == JOptionPane.OK_OPTION){
            return new ReceiptItem((Item) Objects.requireNonNull(itemsComboBox.getSelectedItem()), Float.parseFloat(qty.getText()));
        }
        else{
            return null;
        }
    }
}
