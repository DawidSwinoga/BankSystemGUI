package banksystem.view;

import banksystem.model.Account;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateAccountDialogController {

    @FXML
    TextField nameField;
    @FXML
    TextField lastNameField;
    @FXML
    TextField cityField;
    @FXML
    TextField streetField;
    @FXML
    TextField postalCodeField;
    @FXML
    TextField peselField;
    @FXML
    Button okButton;
    @FXML
    Button cancelButton;

    private Stage dialogStage;
    private Account account;

    public CreateAccountDialogController() {
        this.account = new Account();
    }

    public Account getAccount() {
        return account;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void handleCancel() {
        account = null;
        dialogStage.close();
    }

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            account.setName(nameField.getText());
            account.setLastName(lastNameField.getText());
            account.setPesel(peselField.getText());
            account.getAddress().setCity(cityField.getText());
            account.getAddress().setStreet(streetField.getText());
            account.getAddress().setPostalCode(postalCodeField.getText());

            dialogStage.close();
        }
    }

    private Boolean isInputValid() {
        String messageError = "";

        if (nameField.getText() == null || nameField.getText().length() == 0) {
            messageError = messageError + "Podaj imie.\n";
        }
        if (lastNameField.getText() == null || lastNameField.getText().length() == 0) {
            messageError = messageError + "Podaj nazwisko.\n";
        }
        if (peselField.getText() == null || !isPesel(peselField.getText())) {
            messageError = messageError + "Podaj prawidłowy pesel.\n";
        }
        if (cityField.getText() == null || cityField.getText().length() == 0) {
            messageError = messageError + "Podaj miasto.\n";
        }
        if (streetField.getText() == null || streetField.getText().length() == 0) {
            messageError = messageError + "Podaj ulice.\n";
        }
        if (postalCodeField.getText() == null || !isPostalCode(postalCodeField.getText())) {
            messageError = messageError + "Podaj kod pocztowy.\n";
        }

        if (messageError.length() == 0) {
            return true;
        } else {
            Alert invalidDataAlert = new Alert(Alert.AlertType.ERROR, messageError, ButtonType.OK);
            invalidDataAlert.setTitle("Nieprawidłowe dane");
            invalidDataAlert.setHeaderText("Uzupełnij brakujące dane.");

            invalidDataAlert.showAndWait();
            return false;
        }

    }
    
    private boolean isPesel(String pesel) {
	return checker(pesel, "\\d{11}?");
    }
    
    
    private boolean isPostalCode(String postalCode) {
	return checker(postalCode, "\\d{2}-?\\d{3}?");
    }
    
    private boolean checker(String data, String patter) {
	Pattern pattern = Pattern.compile(patter);
	Matcher matcher = pattern.matcher(data);
	return matcher.matches();
    }

}
