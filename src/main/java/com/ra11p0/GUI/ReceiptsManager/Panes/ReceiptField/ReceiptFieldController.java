package com.ra11p0.GUI.ReceiptsManager.Panes.ReceiptField;

import com.ra11p0.Classes.Day;
import com.ra11p0.Classes.Receipt;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;


//Old DayTilePanel
public class ReceiptFieldController extends ReceiptFieldModel {
    @FXML
    Label titleLabel;
    @FXML
    public ListView<Receipt> listListView;
    @FXML
    Label descriptionLabel;

    public ReceiptFieldController(Day day) { super(day); }

    @FXML public void initialize(){
        titleLabel.textProperty().bind(titleLabelText);
        descriptionLabel.textProperty().bind(descriptionLabelText);
        listListView.itemsProperty().bind(receipts);
    }
}
