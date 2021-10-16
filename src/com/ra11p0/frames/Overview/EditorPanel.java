package com.ra11p0.frames.Overview;

import com.ra11p0.frames.ReceiptsEditor.ReceiptEditor;
import com.ra11p0.structures.Item;
import com.ra11p0.structures.Receipt;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class EditorPanel extends JPanel {
    public EditorPanel(Receipt receipt, ArrayList<Item> items){
        ReceiptEditor editor = new ReceiptEditor(receipt, items, true);
        setLayout(new GridLayout(1, 2));
        add(editor.get_management());
        add(editor.get_receiptView());
    }
}
