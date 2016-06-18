package banksystem.view;

import banksystem.MainApp;
import banksystem.model.Account;
import banksystem.model.Database;
import banksystem.model.Address;
import banksystem.model.NotEnoughtMoneyToTransactionException;
import java.util.Objects;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

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
    @FXML
    private AnchorPane transferDetailsAnchorPane;

    private ObservableList<Account> transferAccounts = FXCollections.observableArrayList();
    private MainApp mainApp;
    private Database database;
    private ObservableList<Account> accounts = FXCollections.observableArrayList();
    private boolean isTransferInProgres = false;

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
	cityColumn.setCellValueFactory(cellData -> cellData.getValue().getAddress().cityProperty());
	streetColumn.setCellValueFactory(cellData -> cellData.getValue().getAddress().streetProperty());
	postalCodeColumn.setCellValueFactory(cellData -> cellData.getValue().getAddress().postalCodeProperty());
	balanceColumn.setCellValueFactory(cellData -> cellData.getValue().balanceProperty());

	clientNumberTransferColumn.setCellValueFactory(cellData -> cellData.getValue().clientNumberProperty());
	nameTransferColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
	lastNameTransferColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
	balanceTransferColumn.setCellValueFactory(cellData -> cellData.getValue().balanceProperty());

	searchOptionChoiceBox.getItems().addAll("Id", "Imie", "Nazwisko", "Adres", "Pesel");
	searchOptionChoiceBox.setValue("Id");

	transferDetailsAnchorPane.setVisible(false);
	hideActionAccountButtons(true);

	accountsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
	    if (!isTransferInProgres) {
		hideActionAccountButtons(false);
	    }
	    if (newValue == null) {
		hideActionAccountButtons(true);
	    } else {
		transferButton.setDisable(false);
	    }
	});
    }

    @FXML
    private void handleSearch() {
	String option = searchOptionChoiceBox.getValue();

	switch (option) {
	    case "Imie":
		String name = searchInputField.getText();
		accountsTable.setItems(FXCollections.observableList(database.findByName(name)));
		break;
	    case "Nazwisko":
		String lastName = searchInputField.getText();
		accountsTable.setItems(FXCollections.observableList(database.findByLastName(lastName)));
		break;
	    case "Pesel":
		String pesel = searchInputField.getText();
		accountsTable.setItems(FXCollections.observableList(database.findByPesel(pesel)));
		break;
	    case "Id":
		Integer clientNumber = parseToInteger(searchInputField.getText());
		if (clientNumber != null) {
		    accountsTable.setItems(FXCollections.observableList(database.findByClientNumber(clientNumber)));
		}
		break;
	    case "Adres":
		String address = searchInputField.getText();
		String[] addressSplit = address.split(", ");

		if (!(addressSplit == null || addressSplit.length < 3)) {
		    Address addressToFind = new Address(addressSplit[0], addressSplit[1], addressSplit[2]);
		    accountsTable.setItems(FXCollections.observableList(database.findByAdress(addressToFind)));
		}
	}
    }

    @FXML
    private void handleTransfer() {
	if (transferAccounts.size() == 2) {
	    Double amount = parseToDouble(transactionAmountTextField.getText());
	    if (amount == null) {
		return;
	    }
	    if (showConfirmationAlert("Czy na pewno chcesz przelać gotówkę?")) {
		try {
		    database.transfer(transferAccounts.get(0), transferAccounts.get(1), amount);
		    refreshAccount(transferAccounts.get(0));
		    transferAccounts.clear();
		    transferDisableButtons(false);
		    transferDetailsAnchorPane.setVisible(false);
		    isTransferInProgres = false;
		} catch (NotEnoughtMoneyToTransactionException e) {
		    showNotEnoughMoneyToTransactionError();
		    refreshAccount(transferAccounts.get(0));
		}
	    }
	} else if (transferAccounts.isEmpty() || !Objects.equals(accountsTable.getSelectionModel().getSelectedItem().getClientNumber(), transferAccounts.get(0).getClientNumber())) {
	    transferAccounts.add(accountsTable.getSelectionModel().getSelectedItem());
	    transferAccountTable.setItems(transferAccounts);
	    transferDisableButtons(true);
	    transferDetailsAnchorPane.setVisible(true);
	    isTransferInProgres = true;
	}
    }

    @FXML
    private void handleShowAllAccounts() {
	accountsTable.setItems(FXCollections.observableList(database.getAccounts()));
    }

    @FXML
    private void handleWithdraw() {
	Double amount = parseToDouble(transactionAmountTextField.getText());

	if (amount != null) {
	    Account account = accountsTable.getSelectionModel().getSelectedItem();
	    if (showConfirmationAlert("Czy na pewno chcesz wypłacić gotówkę?")) {
		try {
		    database.withdraw(account, amount);
		} catch (NotEnoughtMoneyToTransactionException e) {
		    showNotEnoughMoneyToTransactionError();
		}
		refreshAccount(account);
	    }
	}

    }

    @FXML
    private void handleDeposit() {
	Double amount = parseToDouble(transactionAmountTextField.getText());

	if (amount != null) {
	    if (showConfirmationAlert("Czy na pewno chcesz wpłacić gotówkę?")) {
		Account account = accountsTable.getSelectionModel().getSelectedItem();
		account.deposit(amount);
		database.deposit(account);
	    }
	}
    }

    @FXML
    private void handleRemoveAccount() {
	if (showConfirmationAlert("Czy na pewno chcesz usunąć konto?")) {
	    Account account = accountsTable.getSelectionModel().getSelectedItem();
	    int selectedIndex = accountsTable.getSelectionModel().getSelectedIndex();
	    database.remove(account);
	    accountsTable.getItems().remove(selectedIndex);
	}
    }

    @FXML
    private void handleClearSearchInputField() {
	searchInputField.clear();
    }

    public void setMainApp(MainApp mainApp) {
	this.mainApp = mainApp;
	database = this.mainApp.getDatabase();
	accounts.addAll(FXCollections.observableList(database.getAccounts()));
	accountsTable.setItems(accounts);
    }

    @FXML
    private void handleCreateAccount() {
	Account account = mainApp.showCreateAccountDialog();

	if (account != null) {
	    if (showConfirmationAlert("Czy na pewno chcesz utworzyć nowe konto?")) {
		mainApp.getDatabase().add(account);
		accounts.add(account);
		accountsTable.setItems(accounts);
	    }
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

    private void refreshAccount(Account account) {
	account.setBalance(database.getAccount(account).getBalance());
    }

    private void transferDisableButtons(Boolean disable) {
	createAccountButton.setDisable(disable);
	removeAccountButton.setDisable(disable);
	depositButton.setDisable(disable);
	withdrawButton.setDisable(disable);
    }

    private boolean showConfirmationAlert(String text) {
	Alert alert = new Alert(AlertType.CONFIRMATION);
	alert.setTitle("Potwierdzenie wykonania operacji");
	alert.setHeaderText(text);

	Optional<ButtonType> result = alert.showAndWait();
	return result.get() == ButtonType.OK;
    }
}
