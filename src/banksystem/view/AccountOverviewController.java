package banksystem.view;

import banksystem.MainApp;
import banksystem.model.Account;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 *
 * @author Dawid
 */
public class AccountOverviewController {

    @FXML
    private TableView<Account> accountsTable;
    @FXML
    private TableColumn<Account, String> nameColumn;
    @FXML
    private TableColumn<Account, String> lastNameColumn;
    @FXML
    private TableColumn<Account, Number> clientNumberColumn;
    @FXML
    private TableColumn<Account, String> peselColumn;
    @FXML
    private TableColumn<Account, Number> balanceColumn;
    @FXML
    private TableColumn<Account, String> cityColumn;
    @FXML
    private TableColumn<Account, String> streetColumn;
    @FXML
    private TableColumn<Account, String> postalCodeColumn;
    @FXML 
    TableView<Account> transferAccountTable;
    @FXML
    private TableColumn<Account, Number> clientNumberTransferColumn;
    @FXML
    private TableColumn<Account, String> nameTransferColumn;
    @FXML
    private TableColumn<Account, String> lastNameTransferColumn;
    @FXML
    private TableColumn<Account, Number> balanceTransferColumn;
    @FXML
    private Button removeAccountButton;
    @FXML
    private Button createAccountButton;
    @FXML
    private Button withdrawButton;
    @FXML
    private Button depositButton;
    @FXML
    private Button transferButton;

    private ObservableList<Account> transferAccounts = FXCollections.observableArrayList();
    private MainApp mainApp;

    public AccountOverviewController() {
    }

    private void hideActionAccountButtons(boolean hide) {
        removeAccountButton.setDisable(hide);
        withdrawButton.setDisable(hide);
        depositButton.setDisable(hide);
        transferButton.setDisable(hide);
    }

    @FXML
    private void initialize() {
        clientNumberColumn.setCellValueFactory(cellData -> cellData.getValue().clientNumberProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        peselColumn.setCellValueFactory(cellData -> cellData.getValue().peselProperty());
        cityColumn.setCellValueFactory(cellData -> cellData.getValue().getAdress().cityProperty());
        streetColumn.setCellValueFactory(cellData -> cellData.getValue().getAdress().streetProperty());
        postalCodeColumn.setCellValueFactory(cellData -> cellData.getValue().getAdress().postalCodeProperty());
        balanceColumn.setCellValueFactory(cellData -> cellData.getValue().balanceProperty());
        
        clientNumberTransferColumn.setCellValueFactory(cellData -> cellData.getValue().clientNumberProperty());
        nameTransferColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        lastNameTransferColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        balanceTransferColumn.setCellValueFactory(cellData -> cellData.getValue().balanceProperty());

        hideActionAccountButtons(true);
        accountsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> hideActionAccountButtons(false));
    }

    @FXML
    private void handleRemoveAccount() {
        int selectedIndex = accountsTable.getSelectionModel().getSelectedIndex();
        accountsTable.getItems().remove(selectedIndex);
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        accountsTable.setItems(mainApp.getAccounts());
    }
    
    @FXML
    private void handleCreateAccount() {
        Account account = mainApp.showCreateAccountDialog();
        
        if (account != null) {
            mainApp.getAccounts().add(account);
        }
    }

}
