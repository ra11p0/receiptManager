module com.ra11p0 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires swingx;
    requires glazedlists;
    requires jdatepicker;
    requires com.google.gson;
    requires org.hildan.fxgson;


    opens com.ra11p0.GUI.ReceiptsManager.Panes.ReceiptField to javafx.fxml;
    opens com.ra11p0.GUI.ReceiptsManager.Panes to javafx.fxml;
    opens com.ra11p0.GUI.ReceiptEditor.Panes to javafx.fxml;
    opens com.ra11p0.GUI.SearchItemsResult.Panes to javafx.fxml;
    opens com.ra11p0.GUI.SearchItemsResult.Dialogs.EditItemDialog to javafx.fxml;
    opens com.ra11p0.GUI.SearchItemsResult.Dialogs.SelectItemsDialog to javafx.fxml;
    opens com.ra11p0.Utils to com.google.gson;
    opens com.ra11p0.Classes to com.google.gson, javafx.fxml;
    opens com.ra11p0.Classes.Models to com.google.gson, javafx.fxml;
    opens com.ra11p0.GUI.ReceiptsManager.Dialogs.SelectStoreDialog to javafx.fxml;
    opens com.ra11p0.GUI.ReceiptEditor.Dialogs.AddItemDialog to javafx.fxml;
    opens com.ra11p0.GUI.ReceiptEditor.Dialogs.PickDateDialog to javafx.fxml;
    opens com.ra11p0.GUI.ReceiptEditor.Dialogs.RemoveItemDialog to javafx.fxml;
    opens com.ra11p0.GUI.ReceiptEditor.Dialogs.CreateItemDialog to javafx.fxml;

    exports com.ra11p0;
    exports com.ra11p0.Classes.Models;
    exports com.ra11p0.Classes;
    exports com.ra11p0.Classes.Interfaces;
    exports com.ra11p0.Utils;
}