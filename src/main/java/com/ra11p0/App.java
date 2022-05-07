package com.ra11p0;

import com.ra11p0.Classes.ReceiptsDAO;
import com.ra11p0.GUI.ReceiptsManager.Panes.ReceiptsManagerController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class App extends Application {
    public static Settings settings;
    public static ReceiptsDAO dataAccessObject;
    public static BorderPane root = new BorderPane();
    public static final String DEFAULT_RECEIPT_PATH = "res\\receipts.json";
    public static void main(String[] args) throws Exception{
        launch();
    }

    @Override
    public void init() throws Exception {
        super.init();
        dataAccessObject = new ReceiptsDAO();
        dataAccessObject.load(DEFAULT_RECEIPT_PATH);
        LocaleBundle.setLanguage("polish");
        settings = new Settings();
        settings.currency = "PLN";
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        
        FXMLLoader loader = new FXMLLoader(ReceiptsManagerController.class.getResource("ReceiptsManagerComponent.fxml"));
        loader.setResources(LocaleBundle.languageBundle);
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu();
        menu.setText(LocaleBundle.get("file"));
        MenuItem save = new MenuItem();
        save.setText(LocaleBundle.get("save"));
        save.setOnAction(e-> App.dataAccessObject.save());
        menu.getItems().add(save);
        menuBar.getMenus().add(menu);
        App.root.setTop(menuBar);
        App.root.setCenter(loader.load());
        Scene scene = new Scene(App.root, 800, 375);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
