/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banksystemgui;

import banksystem.Account;
import banksystem.Database;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author Dawid
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private TableView<Account> accountsTable;
    @FXML
    private TableColumn<Account, String> nameColumn;
    @FXML
    private TableColumn<Account, String> lastNameColumn;
    @FXML
    private TableColumn<Account, Integer> idColumn;
    @FXML
    private TableColumn<Account, String> peselColumn;
    @FXML
    private TableColumn<Account, BigDecimal> balanceColumn;
    @FXML
    private TableColumn<Account, String> cityColumn;
    @FXML
    private TableColumn<Account, String> streetColumn;
    @FXML
    private TableColumn<Account, String> postalCodeColumn;
    

    @Override
    @SuppressWarnings("unchecked")
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Database database = new Database("database");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        peselColumn.setCellValueFactory(new PropertyValueFactory<>("pesel"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("clientNumber"));
        balanceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));
        cityColumn.setCellValueFactory((CellDataFeatures<Account, String> account) -> new ReadOnlyObjectWrapper(account.getValue().getAdress().getCity()));
        postalCodeColumn.setCellValueFactory((CellDataFeatures<Account, String> account) -> new ReadOnlyObjectWrapper(account.getValue().getAdress().getPostalCode()));
        streetColumn.setCellValueFactory((CellDataFeatures<Account, String> account) -> new ReadOnlyObjectWrapper(account.getValue().getAdress().getStreet()));

        accountsTable.setItems(FXCollections.observableArrayList(database.getAccounts()));

    }

}
