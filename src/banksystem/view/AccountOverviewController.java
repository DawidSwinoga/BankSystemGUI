package banksystem.view;

import banksystem.MainApp;
import banksystem.model.Account;
import banksystem.model.Database;
import banksystem.model.Address;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

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
    @FXML
    private TextField transactionAmountTextField;
    @FXML
    private ChoiceBox<String> searchOptionChoiceBox;
    @FXML
    private TextField searchInputField;

    private ObservableList<Account> transferAccounts = FXCollections.observableArrayList();
    private MainApp mainApp;
    private Database database;

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

        searchOptionChoiceBox.getItems().addAll("Id", "Imie", "Nazwisko", "Adres", "Pesel");
        searchOptionChoiceBox.setValue("Id");

        hideActionAccountButtons(true);
        accountsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> hideActionAccountButtons(false));
    }

    @FXML
    private void handleSearch() {
        String option = searchOptionChoiceBox.getValue();

        switch (option) {
            case "Imie":
                accountsTable.setItems(database.findByName(searchInputField.getText()));
                break;
            case "Nazwisko":
                accountsTable.setItems(database.findByLastName(searchInputField.getText()));
                break;
            case "Pesel":
                accountsTable.setItems(FXCollections.observableArrayList(database.findByPesel(searchInputField.getText())));
                break;
            case "Id":
                Integer clientNumber = parseToInteger(searchInputField.getText());
                if (clientNumber != null) {
                    accountsTable.setItems(FXCollections.observableArrayList(database.findByClientNumber(clientNumber)));
                }
                break;
            case "Adres":
                String address = searchInputField.getText();
                String[] addressSplit = address.split(", ");
     
                if (!(addressSplit == null || addressSplit.length < 3)) {
                    accountsTable.setItems(database.findByAdress(new Address(addressSplit[0], addressSplit[1], addressSplit[2])));
                }
        }
    }

    @FXML
    private void handleTransfer() {
        if (transferAccounts.size() == 2) {
            Double amount = parseToDouble(transactionAmountTextField.getText());
            if (transferAccounts.get(0).isEnoughMoney(amount)) {
                transferAccounts.get(0).withdraw(amount);
                transferAccounts.get(1).deposit(amount);
                transferAccounts.clear();
            } else {
                showNotEnoughMoneyToTransactionError();
            }

        } else {
            transferAccounts.add(accountsTable.getSelectionModel().getSelectedItem());
            transferAccountTable.setItems(transferAccounts);
        }
    }

    @FXML
    private void handleWithdraw() {
        Double amount = parseToDouble(transactionAmountTextField.getText());

        if (amount != null) {
            Account account = accountsTable.getSelectionModel().getSelectedItem();
            if (account.isEnoughMoney(amount)) {
                account.withdraw(amount);
            } else {
                showNotEnoughMoneyToTransactionError();
            }
        }
    }

    @FXML
    private void handleDeposit() {
        Double amount = parseToDouble(transactionAmountTextField.getText());

        if (amount != null) {
            Account account = accountsTable.getSelectionModel().getSelectedItem();
            account.deposit(amount);
        }
    }

    @FXML
    private void handleRemoveAccount() {
        int selectedIndex = accountsTable.getSelectionModel().getSelectedIndex();
        accountsTable.getItems().remove(selectedIndex);
    }

    @FXML
    private void handleClearSearchInputField() {
        searchInputField.clear();
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        database = this.mainApp.getDatabase();
        accountsTable.setItems(mainApp.getDatabase().getAccounts());
    }

    @FXML
    private void handleCreateAccount() {
        Account account = mainApp.showCreateAccountDialog();

        if (account != null) {
            account.setClientNumber(mainApp.getDatabase().getNextFreeClientId());
            mainApp.getDatabase().getAccounts().add(account);
        }
    }

    private Double parseToDouble(String str) {
        Double number;
        try {
            number = Double.parseDouble(str);
        } catch (Exception e) {
            Alert invalidDataAlert = new Alert(Alert.AlertType.ERROR, "Wprowadzona kwota nie jest liczbą!", ButtonType.OK);
            invalidDataAlert.showAndWait();
            number = null;
        }
        return number;
    }

    private Integer parseToInteger(String str) {
        Integer number;
        try {
            number = Integer.parseUnsignedInt(str);
        } catch (Exception e) {
            Alert invalidDataAlert = new Alert(Alert.AlertType.ERROR, "Id musi być liczbą całkowitą dodatnią!", ButtonType.OK);
            invalidDataAlert.showAndWait();
            number = null;
        }
        return number;
    }

    private void showNotEnoughMoneyToTransactionError() {
        Alert invalidDataAlert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
        invalidDataAlert.setHeaderText("Za mało gotówki do przeprowadzenia transakcji!");
        invalidDataAlert.showAndWait();
    }
}
