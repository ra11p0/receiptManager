module com.ra11p0 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires org.apache.commons.io;
    requires swingx;
    requires com.formdev.flatlaf;
    requires glazedlists;
    requires jdatepicker;
    requires filters;
    requires com.google.gson;
    requires org.hildan.fxgson;


    opens com.ra11p0.GUI.ReceiptsManager.Panes.ReceiptField to javafx.fxml;
    opens com.ra11p0.GUI.ReceiptsManager.Panes to javafx.fxml;
    opens com.ra11p0.GUI.ReceiptEditor.Panes to javafx.fxml;
    opens com.ra11p0.GUI.SearchItemsResult.Panes to javafx.fxml;
    opens com.ra11p0.GUI.SearchItemsResult.Dialogs.EditItemDialog to javafx.fxml;
    opens com.ra11p0.GUI.SearchItemsResult.Dialogs.SelectItemsDialog to javafx.fxml;
    opens com.ra11p0.SharedTypes to com.google.gson;

    exports com.ra11p0;
    exports com.ra11p0.SharedTypes;
    exports com.ra11p0.Models;
    exports com.ra11p0.Classes;
    exports com.ra11p0.Interfaces;
    opens com.ra11p0.Classes to com.google.gson;
    opens com.ra11p0.Models to com.google.gson, javafx.fxml;
}