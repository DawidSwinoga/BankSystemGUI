package banksystem;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import banksystem.model.Account;
import banksystem.model.Address;
import banksystem.view.AccountOverviewController;
import banksystem.view.CreateAccountDialogController;
import java.io.IOException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Dawid
 */
public class MainApp extends Application {
    private Stage primaryStage;
    private BorderPane rootLayout;
    //private Database database = new Database("database");
    private ObservableList<Account> accounts = FXCollections.observableArrayList();

    public MainApp() {
        accounts.add(new Account(0, "Dawid", "Swinoga", new Address("Bedzelin", "Grzybowa 14", "95-040"), "12312312"));
    }
    
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("BankSystem");
        
        initRootLayout();
        ShowAccountOverview();
        //this.primaryStage.setOnCloseRequest(e -> database.save());
    }
    
    
    public void initRootLayout() {
       try {
           FXMLLoader loader = new FXMLLoader();
           loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
           rootLayout = (BorderPane) loader.load();
           
           Scene scene = new Scene(rootLayout);
           primaryStage.setScene(scene);
           primaryStage.show();
       } catch (IOException e) {
           e.printStackTrace();
       }
    }
    
    
    public void ShowAccountOverview() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/AccountOverview.fxml"));
            AnchorPane accountOverview = (AnchorPane) loader.load();
            rootLayout.setCenter(accountOverview);
            
            AccountOverviewController controler = loader.getController();
            controler.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    public Account showCreateAccountDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/CreateAccountDialog.fxml"));
            AnchorPane page = (AnchorPane)loader.load();
            
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Nowe konto");
            dialogStage.initOwner(primaryStage);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            
            CreateAccountDialogController controler = loader.getController();
            controler.setDialogStage(dialogStage);
            
            dialogStage.showAndWait();
            
            return controler.getAccount();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    
    public Stage getPrimatyStage() {
        return primaryStage;
    }
    
    public ObservableList<Account> getAccounts() {
        return accounts;
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
