package com.ra11p0.frames.Overview.Panels;

import com.ra11p0.frames.ReceiptsEditor.ReceiptEditor;
import com.ra11p0.structures.Receipt;

import javax.swing.*;
import java.awt.*;

public class EditorPanel extends JPanel {
    private final ReceiptEditor editor;
    public EditorPanel(Receipt receipt){
        editor = new ReceiptEditor(receipt, true);
        setLayout(new GridLayout(1, 2));
        add(editor.get_editorPanel());
        setVisible(true);
    }
    public JFrame saveOnClose(){
        return editor.saveOnClose();
    }
}
