package com.ra11p0.GUI.ReceiptEditor.Dialogs.PickDateDialog;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class PickDateDialogController extends PickDateDialogModel{
    @FXML
    VBox root;
    @FXML
    BorderPane datePickerContainer;

    @FXML
    void initialize(){
        DatePicker datePicker = new DatePicker(LocalDate.now());
        DatePickerSkin datePickerSkin = new DatePickerSkin(datePicker);
        Node popupContent = datePickerSkin.getPopupContent();
        datePickerContainer.setCenter(popupContent);
        getDialogPane().setContent(root);
        getDialogPane().getButtonTypes().addAll(ButtonType.APPLY, ButtonType.CLOSE);
        setResultConverter(buttonType -> {
            if(buttonType.equals(ButtonType.APPLY))
                return Date.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            else return null;
        });
    }
}
